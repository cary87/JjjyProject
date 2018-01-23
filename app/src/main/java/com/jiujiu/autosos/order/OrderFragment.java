package com.jiujiu.autosos.order;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.code19.library.DensityUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jiujiu.autosos.R;
import com.jiujiu.autosos.api.OrderApi;
import com.jiujiu.autosos.common.AppException;
import com.jiujiu.autosos.common.base.BaseListAdapter;
import com.jiujiu.autosos.common.base.BaseListFragment;
import com.jiujiu.autosos.order.model.OrderItem;
import com.jiujiu.autosos.order.model.OrderModel;
import com.jiujiu.autosos.resp.FecthOrderResp;

import java.util.List;

import butterknife.BindView;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/12/21 0021.
 */

public class OrderFragment extends BaseListFragment<OrderModel> {
    @BindView(R.id.tv_title)
    TextView tvBarTitle;

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_order;
    }

    @Override
    protected void loadData(final boolean isPullToReflesh) {
        if (isPullToReflesh) {
            currentPage = 1;
        }
        Disposable disposable = Single.create(new SingleOnSubscribe<FecthOrderResp>() {
            @Override
            public void subscribe(SingleEmitter<FecthOrderResp> emitter) throws Exception {
                FecthOrderResp resp = OrderApi.syncfecthOrder(currentPage, FecthOrderResp.class);
                if (mActivity.isSuccessResp(resp)) {
                    for (OrderModel orderModel : resp.getData()) {
                        String items = orderModel.getItems();
                        List<OrderItem> orderItems = new Gson().fromJson(items, new TypeToken<List<OrderItem>>() {}.getType());
                        orderModel.setOrderItems(orderItems);
                    }
                    emitter.onSuccess(resp);
                } else {
                    emitter.onError(new AppException(resp.getCode(), resp.getMessage()));
                }
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<FecthOrderResp>() {
                    @Override
                    public void accept(FecthOrderResp resp) throws Exception {
                        handleResponse(resp.getData(), isPullToReflesh);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        handleError(throwable);
                    }
                });
        cd.add(disposable);
    }

    @Override
    protected BaseListAdapter<OrderModel> getListAdapter() {
        return new OrderAdapter(mActivity);
    }

    @Override
    protected void afterViewInited(View view) {
        super.afterViewInited(view);
        tvBarTitle.setText("订单");
        mListView.setDivider(new ColorDrawable(ContextCompat.getColor(mActivity, R.color.colorBg)));
        mListView.setDividerHeight(DensityUtil.dip2px(mActivity, 10.0f));
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mActivity, OrderDetailActivity.class);
                intent.putExtra("order", mAdapter.getItem(position));
                startActivity(intent);
            }
        });
    }

}
