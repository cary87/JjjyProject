package com.jiujiu.autosos.resp;

import com.jiujiu.autosos.common.http.BaseResp;

/**
 * Created by Administrator on 2018/1/5.
 */

public class UserResp extends BaseResp {

    /**
     * data : {"userId":18825048091,"name":"司机1","loginName":"18825048091","belongOrg":18825048111,"belongOrgName":"广东省服务公司1","state":0,"onlineState":0,"phone":"18520124568","identiferNo":null,"address":null,"sex":null,"birthDay":null,"nation":null,"wxAccount":"18825048091","picture":null,"identiferPic1":null,"identiferPic2":null,"driverLicensePic":null,"carId":null,"ordersNumber":null,"score":null,"driverServiceItems":"0|2|3|8|9","driverPlateType":null,"driverToGroundType":70,"driverAuxiliaryType":1,"driverLiftingType":2,"driverLiftingTonnageType":null,"driverLicensePlateNumber":"桂A88888","token":"2da3c8017c44bcb661d48406cb7e2401","passwordFlag":null,"password":"123456"}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * userId : 18825048091
         * name : 司机1
         * loginName : 18825048091
         * belongOrg : 18825048111
         * belongOrgName : 广东省服务公司1
         * state : 0
         * onlineState : 0
         * phone : 18520124568
         * identiferNo : null
         * address : null
         * sex : null
         * birthDay : null
         * nation : null
         * wxAccount : 18825048091
         * picture : null
         * identiferPic1 : null
         * identiferPic2 : null
         * driverLicensePic : null
         * carId : null
         * ordersNumber : null
         * score : null
         * driverServiceItems : 0|2|3|8|9
         * driverPlateType : null
         * driverToGroundType : 70
         * driverAuxiliaryType : 1
         * driverLiftingType : 2
         * driverLiftingTonnageType : null
         * driverLicensePlateNumber : 桂A88888
         * token : 2da3c8017c44bcb661d48406cb7e2401
         * passwordFlag : null
         * password : 123456
         */

        private String userId;
        private String name;
        private String loginName;
        private long belongOrg;
        private String belongOrgName;
        private int state;
        private int onlineState;
        private String phone;
        private String identiferNo;
        private String address;
        private String sex;
        private String birthDay;
        private String nation;
        private String wxAccount;
        private String picture;
        private String identiferPic1;
        private String identiferPic2;
        private String driverLicensePic;
        private String carId;
        private String ordersNumber;
        private String score;
        private String driverServiceItems;
        private String driverPlateType;
        private String driverToGroundType;
        private String driverAuxiliaryType;
        private String driverLiftingType;
        private String driverLiftingTonnageType;
        private String driverLicensePlateNumber;
        private String token;
        private String passwordFlag;
        private String password;
        private String province;

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLoginName() {
            return loginName;
        }

        public void setLoginName(String loginName) {
            this.loginName = loginName;
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

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public int getOnlineState() {
            return onlineState;
        }

        public void setOnlineState(int onlineState) {
            this.onlineState = onlineState;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getIdentiferNo() {
            return identiferNo;
        }

        public void setIdentiferNo(String identiferNo) {
            this.identiferNo = identiferNo;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getBirthDay() {
            return birthDay;
        }

        public void setBirthDay(String birthDay) {
            this.birthDay = birthDay;
        }

        public String getNation() {
            return nation;
        }

        public void setNation(String nation) {
            this.nation = nation;
        }

        public String getWxAccount() {
            return wxAccount;
        }

        public void setWxAccount(String wxAccount) {
            this.wxAccount = wxAccount;
        }

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }

        public String getIdentiferPic1() {
            return identiferPic1;
        }

        public void setIdentiferPic1(String identiferPic1) {
            this.identiferPic1 = identiferPic1;
        }

        public String getIdentiferPic2() {
            return identiferPic2;
        }

        public void setIdentiferPic2(String identiferPic2) {
            this.identiferPic2 = identiferPic2;
        }

        public String getDriverLicensePic() {
            return driverLicensePic;
        }

        public void setDriverLicensePic(String driverLicensePic) {
            this.driverLicensePic = driverLicensePic;
        }

        public String getCarId() {
            return carId;
        }

        public void setCarId(String carId) {
            this.carId = carId;
        }

        public String getOrdersNumber() {
            return ordersNumber;
        }

        public void setOrdersNumber(String ordersNumber) {
            this.ordersNumber = ordersNumber;
        }

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
        }

        public String getDriverServiceItems() {
            return driverServiceItems;
        }

        public void setDriverServiceItems(String driverServiceItems) {
            this.driverServiceItems = driverServiceItems;
        }

        public String getDriverPlateType() {
            return driverPlateType;
        }

        public void setDriverPlateType(String driverPlateType) {
            this.driverPlateType = driverPlateType;
        }

        public String getDriverToGroundType() {
            return driverToGroundType;
        }

        public void setDriverToGroundType(String driverToGroundType) {
            this.driverToGroundType = driverToGroundType;
        }

        public String getDriverAuxiliaryType() {
            return driverAuxiliaryType;
        }

        public void setDriverAuxiliaryType(String driverAuxiliaryType) {
            this.driverAuxiliaryType = driverAuxiliaryType;
        }

        public String getDriverLiftingType() {
            return driverLiftingType;
        }

        public void setDriverLiftingType(String driverLiftingType) {
            this.driverLiftingType = driverLiftingType;
        }

        public String getDriverLiftingTonnageType() {
            return driverLiftingTonnageType;
        }

        public void setDriverLiftingTonnageType(String driverLiftingTonnageType) {
            this.driverLiftingTonnageType = driverLiftingTonnageType;
        }

        public String getDriverLicensePlateNumber() {
            return driverLicensePlateNumber;
        }

        public void setDriverLicensePlateNumber(String driverLicensePlateNumber) {
            this.driverLicensePlateNumber = driverLicensePlateNumber;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getPasswordFlag() {
            return passwordFlag;
        }

        public void setPasswordFlag(String passwordFlag) {
            this.passwordFlag = passwordFlag;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
