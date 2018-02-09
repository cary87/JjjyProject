package com.sdbc.onepushlib.net.udp;

import android.content.Context;

import com.sdbc.onepushlib.OnePushService;
import com.sdbc.onepushlib.bean.Config;
import com.sdbc.onepushlib.bean.ErrorCodeEnum;
import com.sdbc.onepushlib.bean.UDPMessage;
import com.sdbc.onepushlib.utils.PushLogs;
import com.sdbc.onepushlib.utils.UDPUtils;

import java.io.UnsupportedEncodingException;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * UDP数据发送
 * author:yangweiquan
 * create:2017/5/5
 */
public class LocalUDPDataSender {
    private static final String TAG = LocalUDPDataSender.class.getName();

    private static LocalUDPDataSender mInstance = null;

    private Context mContext;

    public static LocalUDPDataSender getInstance(Context context) {
        if (mInstance == null) {
            synchronized (LocalUDPDataSender.class) {
                if (mInstance == null) {
                    mInstance = new LocalUDPDataSender(context);
                }
            }
        }

        return mInstance;
    }

    private LocalUDPDataSender(Context context) {
        this.mContext = context;
    }

    public ErrorCodeEnum sendKeepAlive() {
        try {
            byte[] heartBeatBytes = UDPMessage.getHeartBeatMessage().getBytes("utf-8");
            return send(heartBeatBytes, heartBeatBytes.length);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return ErrorCodeEnum.COMMON_DATA_ENCODE_FAIL;
        }
    }

    private ErrorCodeEnum send(byte[] fullProtocalBytes, int dataLen) {
        if (! Config.getInstance().isConfigOk()) {
            return ErrorCodeEnum.CLIENT_SDK_NOT_INITIALIZED;
        }
        if (! OnePushService.isLocalNetworkOk()) {
            PushLogs.e(TAG, "本地网络暂不能连接，数据没有发送！");
            return ErrorCodeEnum.LOCAL_NETWORK_NOT_WORKING;
        }
        DatagramSocket localUDPSocket = LocalUDPSocketProvider.getInstance().getLocalUDPSocket();
        if (localUDPSocket != null && !localUDPSocket.isConnected()) {
            try {
                if (!Config.getInstance().hasServerIpAndPort()) {
                    return ErrorCodeEnum.SERVER_IP_PORT_NOT_SETUP;
                }
                localUDPSocket.connect(InetAddress.getByName(Config.getInstance().getServerIP()),
                        Config.getInstance().getServerPort());
            } catch (Exception e) {
                PushLogs.w(TAG, "send时出错，原因：" + e.getMessage(), e);
                return ErrorCodeEnum.BAD_CONNECT_TO_SERVER;
            }
        }

        boolean result = UDPUtils.send(localUDPSocket, fullProtocalBytes, dataLen);

        return result ? ErrorCodeEnum.OK : ErrorCodeEnum.COMMON_DATA_SEND_FAIL;
    }
}
