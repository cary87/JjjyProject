package com.jiujiu.autosos.resp;

import com.jiujiu.autosos.common.http.BaseResp;
import com.jiujiu.autosos.common.model.User;

/**
 * Created by Administrator on 2018/1/5.
 */

public class UserResp extends BaseResp {

    /**
     * data : {"userId":18825048091,"name":"司机1","loginName":"18825048091","belongOrg":18825048111,"belongOrgName":"广东省服务公司1","state":0,"onlineState":0,"phone":"18520124568","identiferNo":null,"address":null,"sex":null,"birthDay":null,"nation":null,"wxAccount":"18825048091","picture":null,"identiferPic1":null,"identiferPic2":null,"driverLicensePic":null,"carId":null,"ordersNumber":null,"score":null,"driverServiceItems":"0|2|3|8|9","driverPlateType":null,"driverToGroundType":70,"driverAuxiliaryType":1,"driverLiftingType":2,"driverLiftingTonnageType":null,"driverLicensePlateNumber":"桂A88888","token":"2da3c8017c44bcb661d48406cb7e2401","passwordFlag":null,"password":"123456"}
     */

    private User data;

    public User getData() {
        return data;
    }

    public void setData(User data) {
        this.data = data;
    }

}
