package com.jiujiu.autosos.resp;

/**
 * Created by Administrator on 2018/1/8.
 */

public class PushMsg {


    /**
     * content : {"address":"发送的发生","belongOrg":157082263059832832,"belongOrgName":"广西易到道路救援有限公司","bizCompId":157127264049586176,"bizCompName":"业务单位002","carNo":"A1233","carOwner":"15232121111","carOwnerId":15232121111,"chargeType":0,"crossBridgePayer":0,"distance":14,"driverType":"1","items":"[{"additional":0,"baseKilometre":0,"basePrice":0,"itemId":"0","itemName":"拖车","perKilometre":0,"perPrice":0}]","latitude":23.16534,"longitude":113.436108,"orderId":157438621600821248,"orderTime":1515465943453,"pictures":"","province":440000,"provinceName":"广东省","specialReq":"全落地,起吊功能","state":0,"toRescueAdress":"方式的发生的方式的","toRescueLatitude":23.16534,"toRescueLongitude":113.436108}
     * time : 2018-01-09 10:52:07
     * to : 18825048091
     * bizCode : 157438621600821248
     * operation : add
     * bizType : order
     * from : sys
     */

    private String content;
    private String time;
    private String to;
    private String bizCode;
    private String operation;
    private String bizType;
    private String from;

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

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getBizCode() {
        return bizCode;
    }

    public void setBizCode(String bizCode) {
        this.bizCode = bizCode;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }
}
