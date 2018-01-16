package com.jiujiu.autosos.resp;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/1/8.
 */

public class Order implements Serializable {

    /**
     * address : dgdg
     * belongOrg : 157082263059832832
     * belongOrgName : 广西易到道路救援有限公司
     * bizCompId : 157127264049586176
     * bizCompName : 业务单位002
     * carNo : 12345
     * carOwner : 18555555555
     * carOwnerId : 18555555555
     * chargeType : 0
     * crossBridgePayer : 0
     * distance : 14
     * driverType : 1
     * itemsList : [{"additional":0,"baseKilometre":0,"basePrice":0,"itemId":"0","itemName":"拖车","perKilometre":0,"perPrice":0}]
     * latitude : 23.16534
     * longitude : 113.436108
     * orderId : 157491604992671744
     * orderTime : 1515478524682
     * pictures :
     * province : 440000
     * provinceName : 广东省
     * specialReq : 全落地
     * state : 0
     * toRescueAdress : dgdfg
     * toRescueLatitude : 23.16534
     * toRescueLongitude : 113.436108
     */

    private String address;
    private long belongOrg;
    private String belongOrgName;
    private long bizCompId;
    private String bizCompName;
    private String carNo;
    private String carOwner;
    private long carOwnerId;
    private int chargeType;
    private int crossBridgePayer;
    private double distance;
    private String driverType;
    private double latitude;
    private double longitude;
    private long orderId;
    private long orderTime;
    private String pictures;
    private int province;
    private String provinceName;
    private String specialReq;
    private int state;
    private String toRescueAdress;
    private double toRescueLatitude;
    private double toRescueLongitude;
    private List<Item> itemsList;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getBelongOrg() {
        return belongOrg;
    }

    public void setBelongOrg(long belongOrg) {
        this.belongOrg = belongOrg;
    }

    public String getBelongOrgName() {
        return belongOrgName;
    }

    public void setBelongOrgName(String belongOrgName) {
        this.belongOrgName = belongOrgName;
    }

    public long getBizCompId() {
        return bizCompId;
    }

    public void setBizCompId(long bizCompId) {
        this.bizCompId = bizCompId;
    }

    public String getBizCompName() {
        return bizCompName;
    }

    public void setBizCompName(String bizCompName) {
        this.bizCompName = bizCompName;
    }

    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }

    public String getCarOwner() {
        return carOwner;
    }

    public void setCarOwner(String carOwner) {
        this.carOwner = carOwner;
    }

    public long getCarOwnerId() {
        return carOwnerId;
    }

    public void setCarOwnerId(long carOwnerId) {
        this.carOwnerId = carOwnerId;
    }

    public int getChargeType() {
        return chargeType;
    }

    public void setChargeType(int chargeType) {
        this.chargeType = chargeType;
    }

    public int getCrossBridgePayer() {
        return crossBridgePayer;
    }

    public void setCrossBridgePayer(int crossBridgePayer) {
        this.crossBridgePayer = crossBridgePayer;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getDriverType() {
        return driverType;
    }

    public void setDriverType(String driverType) {
        this.driverType = driverType;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public long getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(long orderTime) {
        this.orderTime = orderTime;
    }

    public String getPictures() {
        return pictures;
    }

    public void setPictures(String pictures) {
        this.pictures = pictures;
    }

    public int getProvince() {
        return province;
    }

    public void setProvince(int province) {
        this.province = province;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getSpecialReq() {
        return specialReq;
    }

    public void setSpecialReq(String specialReq) {
        this.specialReq = specialReq;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getToRescueAdress() {
        return toRescueAdress;
    }

    public void setToRescueAdress(String toRescueAdress) {
        this.toRescueAdress = toRescueAdress;
    }

    public double getToRescueLatitude() {
        return toRescueLatitude;
    }

    public void setToRescueLatitude(double toRescueLatitude) {
        this.toRescueLatitude = toRescueLatitude;
    }

    public double getToRescueLongitude() {
        return toRescueLongitude;
    }

    public void setToRescueLongitude(double toRescueLongitude) {
        this.toRescueLongitude = toRescueLongitude;
    }

    public List<Item> getItemsList() {
        return itemsList;
    }

    public void setItemsList(List<Item> itemsList) {
        this.itemsList = itemsList;
    }

    public static class Item implements Serializable {
        /**
         * additional : 0
         * baseKilometre : 0
         * basePrice : 0
         * itemId : 0
         * itemName : 拖车
         * perKilometre : 0
         * perPrice : 0
         */

        private double additional;
        private double baseKilometre;
        private double basePrice;
        private String itemId;
        private String itemName;
        private double perKilometre;
        private double perPrice;

        public double getAdditional() {
            return additional;
        }

        public void setAdditional(double additional) {
            this.additional = additional;
        }

        public double getBaseKilometre() {
            return baseKilometre;
        }

        public void setBaseKilometre(double baseKilometre) {
            this.baseKilometre = baseKilometre;
        }

        public double getBasePrice() {
            return basePrice;
        }

        public void setBasePrice(double basePrice) {
            this.basePrice = basePrice;
        }

        public String getItemId() {
            return itemId;
        }

        public void setItemId(String itemId) {
            this.itemId = itemId;
        }

        public String getItemName() {
            return itemName;
        }

        public void setItemName(String itemName) {
            this.itemName = itemName;
        }

        public double getPerKilometre() {
            return perKilometre;
        }

        public void setPerKilometre(double perKilometre) {
            this.perKilometre = perKilometre;
        }

        public double getPerPrice() {
            return perPrice;
        }

        public void setPerPrice(double perPrice) {
            this.perPrice = perPrice;
        }
    }
}
