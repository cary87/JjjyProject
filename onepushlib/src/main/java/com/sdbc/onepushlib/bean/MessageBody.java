package com.sdbc.onepushlib.bean;

import com.sdbc.onepushlib.utils.StringUtils;

import org.json.JSONObject;

/**
 * UDP发送消息的消息体的body部分
 * author:yangweiquan
 * create:2017/5/5
 */
public class MessageBody {
    /**
     * 消息发送者(用户ID，sys)
     */
    private String from;
    /**
     * 消息接收者(用户ID)
     */
    private String to;
    /**
     * 消息内容
     */
    private String content;
    /**
     * 消息发生时间
     */
    private String time;
    /**
     * 业务码
     */
    private String bizCode;
    /**
     * 业务类型
     * JdoctorConsulSendToPatientMsg 基层医院医生给患者的会诊消息
     * DoctorConsulSendToPatientMsg 平台医生给患者的会诊消息
     * PatientConsulSendToJdoctorMsg 患者给基层医生的会诊消息
     * DoctorConsulSendToJdoctorMsg 平台医生给基层医生的会诊消息
     * DoctorConsulSendToOtherDoctorMsg 平台医生给其他平台医生的会诊消息
     * JdoctorConsulSendToSysMsg 基层医生给系统的会诊信息
     */
    private String bizType;
    /**
     * 操作类型
     */
    private String operation;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getBizCode() {
        return bizCode;
    }

    public void setBizCode(String bizCode) {
        this.bizCode = bizCode;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String toJSONString() {
        return toJSON().toString();
    }

    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        try {
            if (StringUtils.isNotBlank(from)) {
                jsonObject.put("from", from);
            }
            if (StringUtils.isNotBlank(to)) {
                jsonObject.put("to", to);
            }
            if (StringUtils.isNotBlank(content)) {
                jsonObject.put("content", content);
            }
            if (StringUtils.isNotBlank(time)) {
                jsonObject.put("time", time);
            }
            if (StringUtils.isNotBlank(bizCode)) {
                jsonObject.put("bizCode", bizCode);
            }
            if (StringUtils.isNotBlank(bizType)) {
                jsonObject.put("bizType", bizType);
            }
            if (StringUtils.isNotBlank(operation)) {
                jsonObject.put("operation", operation);
            }
        } catch (Exception  e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    @Override
    public String toString() {
        return "MessageBody{" +
                "from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", content='" + content + '\'' +
                ", time='" + time + '\'' +
                ", bizCode='" + bizCode + '\'' +
                ", bizType='" + bizType + '\'' +
                ", operation='" + operation + '\'' +
                '}';
    }
}
