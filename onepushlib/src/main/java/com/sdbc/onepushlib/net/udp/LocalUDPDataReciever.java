package com.sdbc.onepushlib.net.udp;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.sdbc.onepushlib.OnePushAbsReceiver;
import com.sdbc.onepushlib.utils.PushLogs;

import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * 本地UDP的数据接收器
 * author:yangweiquan
 * create:2017/5/5
 */
public class LocalUDPDataReciever {
    private static final String TAG = LocalUDPDataReciever.class.getName();

    private static LocalUDPDataReciever mInstance = null;
    private static MessageHandler mHandler = null;

    private Context mContext;
    private Thread mThread = null;

    public static LocalUDPDataReciever getInstance(Context context) {
        if (mInstance == null) {
            synchronized (LocalUDPDataReciever.class) {
                if (mInstance == null) {
                    mInstance = new LocalUDPDataReciever(context);
                    mHandler = new MessageHandler(context);
                }
            }
        }

        return mInstance;
    }

    private LocalUDPDataReciever(Context context) {
        this.mContext = context;
    }

    /**
     * 本地UDP监听开启
     */
    public void startup() {
        stop();
        try {
            this.mThread = new Thread() {
                @Override
                public void run() {
                    super.run();
                    try {
                        LocalUDPDataReciever.this.listenerImpl();
                    } catch (Exception e) {
                        PushLogs.w(TAG, "本地UDP监听停止了，原因：" + e.hashCode(), e);
                    }
                }
            };
            this.mThread.start();
        } catch (Exception e) {

        }
    }

    private void listenerImpl() throws Exception {
        Message message;
        while (this.mThread != null && !this.mThread.isInterrupted()) {
            byte[] buffer = new byte[1024*100];
            // 接收数据报的包
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            DatagramSocket localUDPSocket = LocalUDPSocketProvider.getInstance().getLocalUDPSocket();
            if (localUDPSocket == null || localUDPSocket.isClosed()) {
                try {
                    Thread.sleep(500);
                } catch (Exception e) {
                }
                continue;
            }
            try {
                PushLogs.d(TAG, "本地UDP准备监听");
                localUDPSocket.receive(packet);
                message = Message.obtain();
                message.obj = packet;
                mHandler.sendMessage(message);
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    Thread.sleep(5000);
                } catch (Exception e1) {}
            }
        }
    }

    /**
     * 停止数据接收
     */
    public void stop() {
        if (this.mThread != null) {
            this.mThread.interrupt();
            this.mThread = null;
        }
    }

    private static class MessageHandler extends Handler {
        private Context mContext = null;

        public MessageHandler(Context context) {
            this.mContext = context;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            DatagramPacket packet = (DatagramPacket) msg.obj;
            if (packet == null) {
                return;
            }
            byte[] dataBytes = packet.getData();
            try {
                String content = new String(dataBytes, "utf-8");
                content = content.trim();
                PushLogs.d(TAG, "收到内容：" + content);
                if (!TextUtils.isEmpty(content)) {
                    Intent broadcastData = new Intent(OnePushAbsReceiver.ACTION_MESSAGE_RECEIVE);
                    broadcastData.putExtra(OnePushAbsReceiver.KEY_CONTENT, content);
                    mContext.sendBroadcast(broadcastData);
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }
}
