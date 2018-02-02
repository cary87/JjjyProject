package com.jiujiu.autosos.me;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.jiujiu.autosos.common.base.BaseListAdapter;
import com.jiujiu.autosos.common.base.BaseListFragment;
import com.jiujiu.autosos.common.storage.UserStorage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.litepal.crud.DataSupport;
import org.litepal.crud.callback.FindMultiCallback;

import java.util.List;

/**
 * Created by Administrator on 2018/1/31.
 */

public class BankCardListFragment extends BaseListFragment<BankCard> {
    private OptionMenuVisibilityCallback  callback;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onReceiveNewCard(BankCard card) {
        autoRefresh();
    }

    @Override
    protected void loadData(final boolean isPullToReflesh) {
        DataSupport.where("userId = ?", UserStorage.getInstance().getUser().getUserId()).findAsync(BankCard.class).listen(new FindMultiCallback() {
            @Override
            public <T> void onFinish(List<T> t) {
                List<BankCard> list = (List<BankCard>) t;
                handleResponse(list, isPullToReflesh);
                if (callback != null && list.size() > 0) {
                    callback.optionMenuVisibility(false);
                }
            }
        });
    }

    @Override
    protected BaseListAdapter getListAdapter() {
        return new BankCardAdapter(mActivity);
    }

    public interface OptionMenuVisibilityCallback {
        void optionMenuVisibility(boolean show);
    }

    public void setCallback(OptionMenuVisibilityCallback callback) {
        this.callback = callback;
    }
}
