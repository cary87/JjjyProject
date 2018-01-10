package com.jiujiu.autosos.order;

import android.view.View;
import android.widget.TextView;

import com.jiujiu.autosos.R;
import com.jiujiu.autosos.api.OrderApi;
import com.jiujiu.autosos.common.base.BaseListAdapter;
import com.jiujiu.autosos.common.base.BaseListFragment;
import com.jiujiu.autosos.common.http.ApiCallback;
import com.jiujiu.autosos.resp.FecthOrderResp;

import butterknife.BindView;
import okhttp3.Call;

/**
 * Created by Administrator on 2017/12/21 0021.
 */

public class OrderFragment extends BaseListFragment<FecthOrderResp.DataBean> {
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
        OrderApi.fecthOrder(currentPage, new ApiCallback<FecthOrderResp>() {
            @Override
            public void onError(Call call, Exception e, int i) {
                handleError(e);
            }

            @Override
            public void onResponse(FecthOrderResp resp, int i) {
                handleResponse(resp.getData(), isPullToReflesh);
            }
        });

    }

    @Override
    protected BaseListAdapter<FecthOrderResp.DataBean> getListAdapter() {
        return new OrderAdapter(mActivity);
    }

    @Override
    protected void afterViewInited(View view) {
        super.afterViewInited(view);
        tvBarTitle.setText("订单");
    }

}
