package com.sdbc.onepushlib.net.udp;

import com.sdbc.onepushlib.utils.PushLogs;

import java.net.DatagramSocket;

/**
 * 本地UDPSocket提供者
 * author:yangweiquan
 * create:2017/5/5
 */
public class LocalUDPSocketProvider {
    private static final String TAG = LocalUDPSocketProvider.class.getName();

    private static LocalUDPSocketProvider mInstance = null;

    private DatagramSocket localUDPSocket = null;

    private LocalUDPSocketProvider() {}

    public static LocalUDPSocketProvider getInstance() {
        if (mInstance == null) {
            synchronized (LocalUDPSocketProvider.class) {
                if (mInstance == null) {
                    mInstance = new LocalUDPSocketProvider();
                }
            }
        }

        return mInstance;
    }

    public DatagramSocket getLocalUDPSocket() {
        if (isLocalUDPSocketReady()) {
            PushLogs.d(TAG, "isLocalUDPSocketReady() == true.");
            return this.localUDPSocket;
        }

        PushLogs.d(TAG, "isLocalUDPSocketReady() == false，需要先重置LocalUDPSocket。");

        return resetLocalUDPSocket();
    }

    public void closeLocalUDPSocket() {
        try {
            PushLogs.d(TAG, "正在closeLocalUDPSocket()...");
            if (this.localUDPSocket != null) {
                this.localUDPSocket.close();
                this.localUDPSocket = null;
            } else {
                PushLogs.d(TAG, "Socket处于未初始化状态，无需关闭。");
            }
        } catch (Exception e) {
            PushLogs.w(TAG, "closeLocalUDPSocket()过程中出错，原因：" + e.getMessage(), e);
        }
    }

    private DatagramSocket resetLocalUDPSocket() {
        try {
            closeLocalUDPSocket();
            PushLogs.d(TAG, "new DatagramSocket()中...");
            this.localUDPSocket = new DatagramSocket();
            PushLogs.d(TAG, "new DatagramSocket()完成.");

            return this.localUDPSocket;
        } catch (Exception e) {
            PushLogs.w(TAG, "localUDPSocket重置时出错，原因：" + e.getMessage(), e);
            closeLocalUDPSocket();
            return null;
        }
    }

    private boolean isLocalUDPSocketReady() {
        return (this.localUDPSocket != null) && (!this.localUDPSocket.isClosed());
    }

}
