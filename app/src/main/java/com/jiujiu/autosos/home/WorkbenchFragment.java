package com.jiujiu.autosos.home;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jiujiu.autosos.R;
import com.jiujiu.autosos.api.OrderApi;
import com.jiujiu.autosos.api.UserApi;
import com.jiujiu.autosos.common.AppException;
import com.jiujiu.autosos.common.base.BaseListAdapter;
import com.jiujiu.autosos.common.base.BaseListFragment;
import com.jiujiu.autosos.common.http.BaseResp;
import com.jiujiu.autosos.common.storage.UserStorage;
import com.jiujiu.autosos.nav.LocationManeger;
import com.jiujiu.autosos.order.OrderAdapter;
import com.jiujiu.autosos.order.OrderDetailActivity;
import com.jiujiu.autosos.order.model.OnlineStateEnum;
import com.jiujiu.autosos.order.model.OrderItem;
import com.jiujiu.autosos.order.model.OrderModel;
import com.jiujiu.autosos.resp.FecthOrderResp;
import com.jiujiu.autosos.resp.UserResp;

import org.litepal.crud.DataSupport;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;

import butterknife.BindView;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/12/21 0021.
 */

public class WorkbenchFragment extends BaseListFragment<OrderModel> {

    @BindView(R.id.switch_online)
    Switch switchOnline;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_work;
    }

    @Override
    protected void loadData(final boolean isPullToReflesh) {
        if (isPullToReflesh) {
            currentPage = 1;
        }

        Single.fromCallable(new Callable<List<OrderModel>>() {
            @Override
            public List<OrderModel> call() throws Exception {
                List<OrderModel> list = DataSupport.where("driverId = ?", UserStorage.getInstance().getUser().getUserId()).find(OrderModel.class, false);
                for (OrderModel orderModel : list) {
                    String items = orderModel.getItems();
                    List<OrderItem> orderItems = new Gson().fromJson(items, new TypeToken<List<OrderItem>>() {}.getType());
                    orderModel.setOrderItems(orderItems);
                }
                FecthOrderResp resp = OrderApi.syncFecthCanAcceptOrder(currentPage, FecthOrderResp.class);
                if (mActivity.isSuccessResp(resp)) {
                    for (OrderModel orderModel : resp.getData()) {
                        String items = orderModel.getItems();
                        List<OrderItem> orderItems = new Gson().fromJson(items, new TypeToken<List<OrderItem>>() {}.getType());
                        orderModel.setOrderItems(orderItems);
                    }
                    if (list != null) {
                        list.addAll(resp.getData());
                    }
                }
                return list;
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<OrderModel>>() {
                    @Override
                    public void accept(List<OrderModel> list) throws Exception {
                        handleResponse(list, isPullToReflesh);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        handleError(throwable);
                    }
                });

    }

    @Override
    protected boolean supportLoadMore() {
        return false;
    }

    @Override
    protected BaseListAdapter<OrderModel> getListAdapter() {
        return new OrderAdapter(mActivity);
    }

    @Override
    protected void afterViewInited(View view) {
        super.afterViewInited(view);
        switchOnline.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, final boolean b) {
                updateOnlineState(b);
            }
        });
        switchOnline.setChecked(UserStorage.getInstance().getUser().getOnlineState() == OnlineStateEnum.Online.getValue());
        setOnlineText(switchOnline.isChecked());
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mActivity, OrderDetailActivity.class);
                intent.putExtra("order", mAdapter.getItem(position));
                startActivity(intent);
            }
        });
    }

    public void setOnlineText(boolean online) {
        if (online) {
            tvTitle.setText("在线");
        } else {
            tvTitle.setText("离线");
        }
    }

    /**
     * 更新在线状态
     * @param b
     */
    public void updateOnlineState(final boolean b) {
        Disposable disposable = Single.fromCallable(new Callable<BaseResp>() {
            @Override
            public BaseResp call() throws Exception {
                HashMap<String, String> param = new HashMap<>();
                param.put("onlineState", b ? OnlineStateEnum.Online.getValue() + "" : OnlineStateEnum.Offline.getValue() + "");
                return UserApi.setOnlineState(param, BaseResp.class);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<BaseResp>() {
                    @Override
                    public void accept(BaseResp o) throws Exception {
                        if (mActivity.isSuccessResp(o)) {
                            UserResp.DataBean user = UserStorage.getInstance().getUser();
                            user.setOnlineState(b ? OnlineStateEnum.Online.getValue() : OnlineStateEnum.Offline.getValue());
                            UserStorage.getInstance().setUser(user);
                            setOnlineText(b);
                            if (b) {
                                LocationManeger.getInstance().startLocation();
                            } else {
                                LocationManeger.getInstance().stopLocation();
                            }
                        } else {
                            mActivity.handleError(new AppException(o.getCode(), o.getMessage()));
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mActivity.handleError(throwable);
                    }
                });
        cd.add(disposable);
    }
}
