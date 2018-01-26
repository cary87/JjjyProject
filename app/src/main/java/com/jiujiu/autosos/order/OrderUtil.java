package com.jiujiu.autosos.order;

import com.google.gson.Gson;
import com.jiujiu.autosos.api.OrderApi;
import com.jiujiu.autosos.api.UserApi;
import com.jiujiu.autosos.common.AppException;
import com.jiujiu.autosos.common.AutososApplication;
import com.jiujiu.autosos.common.Constant;
import com.jiujiu.autosos.common.base.AbsBaseActivity;
import com.jiujiu.autosos.common.http.ApiCallback;
import com.jiujiu.autosos.common.http.BaseResp;
import com.jiujiu.autosos.common.model.AreaModel;
import com.jiujiu.autosos.common.storage.UserStorage;
import com.jiujiu.autosos.common.utils.AreaUtil;
import com.jiujiu.autosos.common.utils.LogUtils;
import com.jiujiu.autosos.order.model.OrderModel;

import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;

/**
 * Created by Administrator on 2018/1/23.
 */

public class OrderUtil {

    private OrderUtil() {
        
    }

    /**
     * 保存订单相关图片
     * @param activity
     * @param order
     * @param paths
     */
    public static void savePicturesForOrder(final AbsBaseActivity activity, OrderModel order, List<String> paths) {
        HashMap<String, String> params = new HashMap<>();
        params.put("orderId", order.getOrderId() + "");
        params.put("pictures", new Gson().toJson(paths));
        OrderApi.savePicFile(params, new ApiCallback<BaseResp>() {
            @Override
            public void onError(Call call, Exception e, int i) {
                activity.handleError(e);
            }

            @Override
            public void onResponse(BaseResp resp, int i) {

            }
        });
    }

    /**
     * 更新司机位置信息
     * @param longitude
     * @param latitude
     * @param province
     */
    public static void updateDriverPosition(final double longitude, final double latitude, final String province, final String address) {
        if (UserStorage.getInstance().isLogined() && UserStorage.getInstance().isOnline()) {
            Observable.create(new ObservableOnSubscribe<BaseResp>() {
                @Override
                public void subscribe(ObservableEmitter<BaseResp> emitter) throws Exception {
                    HashMap<String, String> param = new HashMap<>();
                    String provinceCode = "";
                    List<AreaModel> areaModels = AreaUtil.getArea(AutososApplication.getApp());
                    for (AreaModel areaModel : areaModels) {
                        if (areaModel.getName().contains(province) || (province.length() > 1 && areaModel.getName().contains(province.substring(0, 2)))) {
                            provinceCode = areaModel.getId();
                            break;
                        }
                    }
                    param.put("province", provinceCode);
                    param.put("oldlongitude", UserStorage.getInstance().getLastSubmitLongitude() == 0 ? "" : Double.toString(UserStorage.getInstance().getLastSubmitLongitude()));
                    param.put("oldlatitude", UserStorage.getInstance().getLastSubmitLatitude() == 0 ? "" : Double.toString(UserStorage.getInstance().getLastSubmitLatitude()));
                    param.put("longitude", Double.toString(longitude));
                    param.put("latitude", Double.toString(latitude));
                    BaseResp resp = UserApi.updatePosition(param, BaseResp.class);
                    if (Constant.CODE_SUCCESS.equals(resp.getCode())) {
                        UserStorage.getInstance().setLastSubmitLongitude(longitude);
                        UserStorage.getInstance().setLastSubmitLatitude(latitude);
                        UserStorage.getInstance().setNowLocationAddress(address);
                        emitter.onNext(resp);
                    } else {
                        emitter.onError(new AppException(resp.getCode(), resp.getMessage()));
                    }
                }
            }).observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Consumer<BaseResp>() {
                        @Override
                        public void accept(BaseResp o) throws Exception {
                            LogUtils.i("wzh", "-------------向Server更新当前位置信息成功-----------");
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            LogUtils.e("wzh", "-------------向Server更新当前位置信息失败----------- " + throwable.getMessage());
                        }
                    });
        }
    }
}
