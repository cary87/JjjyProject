package com.sdbc.onepushlib.bean;

import com.sdbc.onepushlib.utils.PushLogs;

import org.json.JSONObject;

/**
 * UDP发送消息的消息体
 * author:yangweiquan
 * create:2017/5/4
 */
public class UDPMessage {

    public static final String TAG = UDPMessage.class.getName();

    private MessageHead head;

    private MessageBody body;

    public MessageHead getHead() {
        return head;
    }

    public void setHead(MessageHead head) {
        this.head = head;
    }

    public MessageBody getBody() {
        return body;
    }

    public void setBody(MessageBody body) {
        this.body = body;
    }

    public static String getHeartBeatMessage() {
        UDPMessage message = new UDPMessage();
        MessageHead head = new MessageHead();
        head.setMtype(MessageTypeEnum.HEARTBEAT.getValue());
        head.setOtype(Config.getInstance().getOsType().getValue());
        head.setUserid(Config.getInstance().getUserId());
        head.setToken(Config.getInstance().getToken());
        head.setAtype(Config.getInstance().getAppType());
        message.setHead(head);

        PushLogs.d(TAG, "心跳包数据：" + message.toJSONString());
        return message.toJSONString();
    }

    public String toJSONString() {
        JSONObject jsonObject = new JSONObject();
        try {
            if (head != null) {
                jsonObject.put("head", head.toJSON());
            }
            if (body != null) {
                jsonObject.put("body", body.toJSON());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonObject.toString();
    }
}
