package com.jiujiu.autosos.order;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jiujiu.autosos.api.OrderApi;
import com.jiujiu.autosos.api.UserApi;
import com.jiujiu.autosos.common.AppException;
import com.jiujiu.autosos.common.AutososApplication;
import com.jiujiu.autosos.common.Constant;
import com.jiujiu.autosos.common.base.AbsBaseActivity;
import com.jiujiu.autosos.common.http.ApiCallback;
import com.jiujiu.autosos.common.http.BaseResp;
import com.jiujiu.autosos.common.model.AreaModel;
import com.jiujiu.autosos.common.model.ServiceItemEnum;
import com.jiujiu.autosos.common.storage.UserStorage;
import com.jiujiu.autosos.common.utils.AreaUtil;
import com.jiujiu.autosos.common.utils.LogUtils;
import com.jiujiu.autosos.nav.LocationManeger;
import com.jiujiu.autosos.order.model.OrderItem;
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

    public static final String KEY_ORDER = "order";

    private OrderUtil() {
        
    }

    public static String getOrderTypeName(OrderModel orderModel) {
        List<OrderItem> orderItems = orderModel.getOrderItems();
        if (orderItems != null && orderItems.size() > 0) {
            StringBuffer buffer = new StringBuffer();
            for (OrderItem orderItem : orderItems) {
                buffer.append(orderItem.getItemName() + "-");
            }
            String itemsName = buffer.toString().substring(0, buffer.toString().length() - 1);
            return itemsName;
        }
        return "";
    }

    /**
     * 查看是否拖车服务
     * @param item
     * @return
     */
    public static boolean checkIsDragcar(OrderModel item) {
        List<OrderItem> orderItems = item.getOrderItems();
        if (orderItems != null && orderItems.size() > 0) {
            boolean found = false;
            for (OrderItem orderItem : orderItems) {
                if (String.valueOf(ServiceItemEnum.DragCar.getValue().intValue()).equals(orderItem.getItemId())
                        || String.valueOf(ServiceItemEnum.NotAccidentDragCar.getValue().intValue()).equals(orderItem.getItemId())) {
                    found = true;
                    break;
                }
            }
            return found;
        } else {
            return false;
        }
    }

    /**
     * 为订单添加以|为分隔符的图片路径
     * @param orderModel
     * @param paths
     */
    public static void addPicturePathsForOrder(OrderModel orderModel, List<String> paths) {
        StringBuffer stringBuffer = new StringBuffer();
        for (String path : paths) {
            stringBuffer.append(path);
            stringBuffer.append("|");
        }
        String pathStr = stringBuffer.toString().substring(0, stringBuffer.toString().length() - 1);
        if (TextUtils.isEmpty(orderModel.getPictures())) {
            orderModel.setPictures(pathStr);
        } else if (orderModel.getPictures().endsWith("|")) {
            orderModel.setPictures(orderModel.getPictures() + pathStr);
        } else {
            orderModel.setPictures(orderModel.getPictures() + "|" + pathStr);
        }
    }

    /**
     * 保存订单相关图片
     * @param activity
     * @param order
     * @param paths
     */
    public static void savePicturesForOrder(final AbsBaseActivity activity, OrderModel order, final List<String> paths, final ActionListener listener) {
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
                if (listener != null) {
                    listener.onSuccess();
                }
            }
        });
    }

    /**
     * 接单
     * @param order
     */
    public static void acceptOrder(final AbsBaseActivity context, final OrderModel order, final ActionListener listener) {
        context.showLoadingDialog("接受订单中");
        final HashMap<String, String> params = new HashMap<>();
        params.put("province", order.getProvince() + "");
        params.put("orderId", order.getOrderId() + "");
        params.put("svrId", UserStorage.getInstance().getUser().getBelongOrg() + "");
        params.put("svrName", UserStorage.getInstance().getUser().getBelongOrgName() + "");
        params.put("driverType", order.getDriverType() + "");
        params.put("driverCar", order.getCarNo() + "");
        params.put("toRescueAdress", order.getToRescueAdress() + "");
        params.put("toRescueLongitude", order.getToRescueLongitude() + "");
        params.put("toRescueLatitude", order.getToRescueLatitude() + "");
        Gson gson = new GsonBuilder().serializeNulls().create();
        params.put("items", gson.toJson(order.getOrderItems()));
        params.put("driverAdress", TextUtils.isEmpty(UserStorage.getInstance().getNowLocationAddress()) ? "未知" : UserStorage.getInstance().getNowLocationAddress());
        params.put("driverLongitude", String.valueOf(UserStorage.getInstance().getLastSubmitLongitude()));
        params.put("driverLatitude", String.valueOf(UserStorage.getInstance().getLastSubmitLatitude()));
        OrderApi.driverAcceptOrder(params, new ApiCallback<BaseResp>() {
            @Override
            public void onError(Call call, Exception e, int i) {
                context.handleError(e);
                if (listener != null) {
                    listener.onFail();
                }
            }

            @Override
            public void onResponse(BaseResp resp, int i) {
                context.hideLoadingDialog();
                context.showToast("接单成功");
                if (listener != null) {
                    listener.onSuccess();
                }
                LocationManeger.getInstance().stopLocation();//接单成功，关掉位置信息更新，当前司机为忙碌状态，不可再接单
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
                    param.put("oldlongitude", "");
                    param.put("oldlatitude", "");
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

    public interface ActionListener {
        void onSuccess();

        void onFail();
    }
}
