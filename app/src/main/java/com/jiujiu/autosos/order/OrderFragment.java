package com.jiujiu.autosos.order;

import android.view.View;
import android.widget.TextView;

import com.jiujiu.autosos.R;
import com.jiujiu.autosos.common.base.BaseFragment;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/12/21 0021.
 */

public class OrderFragment extends BaseFragment {
    @BindView(R.id.tv_title)
    TextView tvBarTitle;

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_order;
    }

    @Override
    protected void afterViewInited(View view) {
        tvBarTitle.setText("订单");
    }

}
