package com.sdbc.onepushlib.utils;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * UDP工具类
 * author:yangweiquan
 * create:2017/5/5
 */
public class UDPUtils {
    private static final String TAG = UDPUtils.class.getName();

    public static boolean send(DatagramSocket socket, byte[] dataBytes, int dataLen) {
        if (socket != null && dataBytes != null) {
            try {
                return send(socket, new DatagramPacket(dataBytes, dataLen));
            } catch (Exception e) {
                PushLogs.e(TAG, "send》》发送UDP数据报文时出错，remoteIp=" + socket.getInetAddress() +
                    ", remotePort=" + socket.getPort() + ". 原因：" + e.getMessage(), e);
                return false;
            }
        } else {
            if (socket == null) {
                PushLogs.e(TAG, "send》》参数socket == null");
            }
            if (dataBytes == null) {
                PushLogs.e(TAG, "send》》参数dataBytes == null");
            }

            return false;
        }
    }

    public static synchronized boolean send(DatagramSocket socket, DatagramPacket packet) {
        boolean sendSuccess = true;
        if (socket != null && packet != null) {
            if (socket.isConnected()) {
                try {
                    socket.send(packet);
                } catch (Exception e) {
                    sendSuccess = false;
                    PushLogs.e(TAG, "send(socket,packet)》》发送UDP数据报文时出错，原因：" +
                            e.getMessage(), e);
                }
            } else {
                sendSuccess = false;
            }
        } else {
            if (socket == null) {
                PushLogs.e(TAG, "send(socket,packet)》》参数socket == null");
            }
            if (packet == null) {
                PushLogs.e(TAG, "send(socket,packet)》》参数packet == null");
            }
            sendSuccess = false;
        }

        return sendSuccess;
    }
}
