package com.jiujiu.autosos.order.model;
import java.io.Serializable;
import java.util.List;

public class OrderModel implements Serializable {
    /**
     * toRescueAdress : 的说法是短发沙发上
     * driverCar : 粤A000001
     * distance : 13
     * orderId : 157808867906007040
     * payTime : null
     * latitude : 23.16534
     * itemIds : 0
     * chargeType : 1
     * remark :
     * orderItems : null
     * pictures :
     * payableAmount : 1001
     * carOwnerId : 12345655454
     * flowAmount : 0
     * arriveTime : null
     * toRescueLongitude : 113.436108
     * postTime : 1515554146000
     * orderTime : 1515554166000
     * payType : null
     * province : 440000
     * carOwner : 12345655454
     * carNo : 粤A000001
     * bizCompId : 157427346464595968
     * state : 2
     * logs : []
     * longitude : 113.436108
     * crossBridgePayer : 1
     * svrId : 157174497826447360
     * specialReq :
     * finishTime : null
     * address : 发送到富士达发送
     * mobile : null
     * belongOrgName : 广东易到网络科技有限公司
     * acceptTime : 1515554153000
     * traces : []
     * svrName : 广东易到网络科技有限公司
     * driverType : 1
     * crossBridgeAmount : 0
     * driverId : 18825048091
     * toRescueLatitude : 23.16534
     * belongOrg : 157174497826447360
     * bizCompName : 广东业务单位001
     * driverName : 司机1
     * provinceName : 广东省
     * payCode : null
     * paidAmount : 0
     * items : [{"additional":0,"baseKilometre":0,"basePrice":0,"itemId":"0","itemName":"拖车","perKilometre":0,"perPrice":0}]
     */

    private String toRescueAdress;
    private String driverCar;
    private double distance;
    private long orderId;
    private String payTime;
    private double latitude;
    private String itemIds;
    private int chargeType;
    private String remark;
    private List<OrderItem> orderItems;
    private String pictures;
    private double payableAmount;
    private long carOwnerId;
    private double flowAmount;
    private String arriveTime;
    private double toRescueLongitude;
    private long postTime;
    private long orderTime;
    private String payType;
    private int province;
    private String carOwner;
    private String carNo;
    private long bizCompId;
    private int state;
    private double longitude;
    private int crossBridgePayer;
    private long svrId;
    private String specialReq;
    private String finishTime;
    private String address;
    private String mobile;
    private String belongOrgName;
    private long acceptTime;
    private String svrName;
    private String driverType;
    private double crossBridgeAmount;
    private long driverId;
    private double toRescueLatitude;
    private long belongOrg;
    private String bizCompName;
    private String driverName;
    private String provinceName;
    private String payCode;
    private double paidAmount;
    private String items;
    private List<?> logs;
    private List<OrderTrace> traces;

    public String getToRescueAdress() {
        return toRescueAdress;
    }

    public void setToRescueAdress(String toRescueAdress) {
        this.toRescueAdress = toRescueAdress;
    }

    public String getDriverCar() {
        return driverCar;
    }

    public void setDriverCar(String driverCar) {
        this.driverCar = driverCar;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getItemIds() {
        return itemIds;
    }

    public void setItemIds(String itemIds) {
        this.itemIds = itemIds;
    }

    public int getChargeType() {
        return chargeType;
    }

    public void setChargeType(int chargeType) {
        this.chargeType = chargeType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public String getPictures() {
        return pictures;
    }

    public void setPictures(String pictures) {
        this.pictures = pictures;
    }

    public double getPayableAmount() {
        return payableAmount;
    }

    public void setPayableAmount(double payableAmount) {
        this.payableAmount = payableAmount;
    }

    public long getCarOwnerId() {
        return carOwnerId;
    }

    public void setCarOwnerId(long carOwnerId) {
        this.carOwnerId = carOwnerId;
    }

    public double getFlowAmount() {
        return flowAmount;
    }

    public void setFlowAmount(double flowAmount) {
        this.flowAmount = flowAmount;
    }

    public String getArriveTime() {
        return arriveTime;
    }

    public void setArriveTime(String arriveTime) {
        this.arriveTime = arriveTime;
    }

    public double getToRescueLongitude() {
        return toRescueLongitude;
    }

    public void setToRescueLongitude(double toRescueLongitude) {
        this.toRescueLongitude = toRescueLongitude;
    }

    public long getPostTime() {
        return postTime;
    }

    public void setPostTime(long postTime) {
        this.postTime = postTime;
    }

    public long getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(long orderTime) {
        this.orderTime = orderTime;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public int getProvince() {
        return province;
    }

    public void setProvince(int province) {
        this.province = province;
    }

    public String getCarOwner() {
        return carOwner;
    }

    public void setCarOwner(String carOwner) {
        this.carOwner = carOwner;
    }

    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }

    public long getBizCompId() {
        return bizCompId;
    }

    public void setBizCompId(long bizCompId) {
        this.bizCompId = bizCompId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getCrossBridgePayer() {
        return crossBridgePayer;
    }

    public void setCrossBridgePayer(int crossBridgePayer) {
        this.crossBridgePayer = crossBridgePayer;
    }

    public long getSvrId() {
        return svrId;
    }

    public void setSvrId(long svrId) {
        this.svrId = svrId;
    }

    public String getSpecialReq() {
        return specialReq;
    }

    public void setSpecialReq(String specialReq) {
        this.specialReq = specialReq;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getBelongOrgName() {
        return belongOrgName;
    }

    public void setBelongOrgName(String belongOrgName) {
        this.belongOrgName = belongOrgName;
    }

    public long getAcceptTime() {
        return acceptTime;
    }

    public void setAcceptTime(long acceptTime) {
        this.acceptTime = acceptTime;
    }

    public String getSvrName() {
        return svrName;
    }

    public void setSvrName(String svrName) {
        this.svrName = svrName;
    }

    public String getDriverType() {
        return driverType;
    }

    public void setDriverType(String driverType) {
        this.driverType = driverType;
    }

    public double getCrossBridgeAmount() {
        return crossBridgeAmount;
    }

    public void setCrossBridgeAmount(double crossBridgeAmount) {
        this.crossBridgeAmount = crossBridgeAmount;
    }

    public long getDriverId() {
        return driverId;
    }

    public void setDriverId(long driverId) {
        this.driverId = driverId;
    }

    public double getToRescueLatitude() {
        return toRescueLatitude;
    }

    public void setToRescueLatitude(double toRescueLatitude) {
        this.toRescueLatitude = toRescueLatitude;
    }

    public long getBelongOrg() {
        return belongOrg;
    }

    public void setBelongOrg(long belongOrg) {
        this.belongOrg = belongOrg;
    }

    public String getBizCompName() {
        return bizCompName;
    }

    public void setBizCompName(String bizCompName) {
        this.bizCompName = bizCompName;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getPayCode() {
        return payCode;
    }

    public void setPayCode(String payCode) {
        this.payCode = payCode;
    }

    public double getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(double paidAmount) {
        this.paidAmount = paidAmount;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public List<?> getLogs() {
        return logs;
    }

    public void setLogs(List<?> logs) {
        this.logs = logs;
    }

    public List<OrderTrace> getTraces() {
        return traces;
    }

    public void setTraces(List<OrderTrace> traces) {
        this.traces = traces;
    }
}