package com.jiujiu.autosos.home;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jiujiu.autosos.R;
import com.jiujiu.autosos.api.OrderApi;
import com.jiujiu.autosos.api.UserApi;
import com.jiujiu.autosos.common.AppException;
import com.jiujiu.autosos.common.base.BaseFragment;
import com.jiujiu.autosos.common.http.BaseResp;
import com.jiujiu.autosos.common.model.User;
import com.jiujiu.autosos.common.storage.UserStorage;
import com.jiujiu.autosos.common.utils.LogUtils;
import com.jiujiu.autosos.nav.LocationManeger;
import com.jiujiu.autosos.order.CardHolder;
import com.jiujiu.autosos.order.OrderDetailActivity;
import com.jiujiu.autosos.order.OrderUtil;
import com.jiujiu.autosos.order.model.OnlineStateEnum;
import com.jiujiu.autosos.order.model.OrderItem;
import com.jiujiu.autosos.order.model.OrderModel;
import com.jiujiu.autosos.order.model.RefreshViewEvent;
import com.jiujiu.autosos.resp.FecthOrderResp;
import com.stone.card.library.CardAdapter;
import com.stone.card.library.CardSlidePanel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
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

public class WorkbenchFragment extends BaseFragment {

    @BindView(R.id.switch_online)
    Switch switchOnline;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_tips)
    TextView tvTips;
    @BindView(R.id.image_slide_panel)
    CardSlidePanel slidePanel;
    @BindView(R.id.layout_msg_error)
    LinearLayout mLayoutMsgError;

    private CardSlidePanel.CardSwitchListener cardSwitchListener;

    private List<OrderModel> dataList = new ArrayList<>();

    private static final long EXPIRE_TIME = 60 * 60 * 1000;//可接订单时效性

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
    public void receiveRefreshEvent(RefreshViewEvent event) {
        loadData();
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_work;
    }

    protected void loadData() {
        Disposable disposable = Single.fromCallable(new Callable<List<OrderModel>>() {
            @Override
            public List<OrderModel> call() throws Exception {
                //已收到推送但选择忽略的单
                List<OrderModel> list = DataSupport.where("driverId = ?", UserStorage.getInstance().getUser().getUserId()).find(OrderModel.class, false);
                for (OrderModel orderModel : list) {
                    String items = orderModel.getItems();
                    List<OrderItem> orderItems = new Gson().fromJson(items, new TypeToken<List<OrderItem>>() {
                    }.getType());
                    orderModel.setOrderItems(orderItems);
                }
                //省公司特地指派给当前救援司机的单
                try {
                    FecthOrderResp resp = OrderApi.syncFecthCanAcceptOrder(1, FecthOrderResp.class);
                    if (mActivity.isSuccessResp(resp)) {
                        for (OrderModel orderModel : resp.getData()) {
                            String items = orderModel.getItems();
                            List<OrderItem> orderItems = new Gson().fromJson(items, new TypeToken<List<OrderItem>>() {
                            }.getType());
                            orderModel.setOrderItems(orderItems);
                        }
                        list.addAll(resp.getData());
                    }
                } catch (Exception e) {
                    LogUtils.e("wzh", e.toString());
                }

                LinkedHashSet hashSet = new LinkedHashSet(list);
                list.clear();
                list.addAll(hashSet);
                Iterator<OrderModel> it = list.iterator();
                while (it.hasNext()) {//时效性为1小时
                    OrderModel order = it.next();
                    if (order.getOrderTime() + EXPIRE_TIME < System.currentTimeMillis()) {
                        it.remove();
                        order.delete();
                    }
                }
                return list;
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<OrderModel>>() {
                    @Override
                    public void accept(List<OrderModel> list) throws Exception {
                        dataList.clear();
                        dataList.addAll(list);
                        slidePanel.getAdapter().notifyDataSetChanged();
                        if (dataList.size() == 0) {
                            setEmptyView(true);
                        } else {
                            setEmptyView(false);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
        cd.add(disposable);
    }

    public void setEmptyView(boolean empty) {
        mLayoutMsgError.setVisibility(empty ? View.VISIBLE : View.GONE);
        final int visiable = empty ? View.GONE : View.VISIBLE;
        if (visiable == View.VISIBLE) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    tvTips.setVisibility(visiable);
                }
            }, 500);
        } else {
            tvTips.setVisibility(visiable);
        }
    }

    @Override
    protected void afterViewInited(View view) {
        switchOnline.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, final boolean b) {
                updateOnlineState(b);
            }
        });
        switchOnline.setChecked(UserStorage.getInstance().getUser().getOnlineState() == OnlineStateEnum.Online.getValue());
        setOnlineText(switchOnline.isChecked());

        mLayoutMsgError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadData();
            }
        });

        // 1. 左右滑动监听
        cardSwitchListener = new CardSlidePanel.CardSwitchListener() {

            @Override
            public void onShow(int index) {

            }

            @Override
            public void onCardVanish(final int index, int type) {
                if (type == CardSlidePanel.VANISH_TYPE_LEFT) {
                    dataList.get(index).deleteAsync().listen(null);
                } else if (type == CardSlidePanel.VANISH_TYPE_RIGHT) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(mActivity, OrderDetailActivity.class);
                            intent.putExtra(OrderUtil.KEY_ORDER, dataList.get(index));
                            startActivity(intent);
                        }
                    }, 200);
                }
                if (index == dataList.size() - 1) {
                    setEmptyView(true);
                }
            }
        };
        slidePanel.setCardSwitchListener(cardSwitchListener);


        // 2. 绑定Adapter
        slidePanel.setAdapter(new CardAdapter() {
            @Override
            public int getLayoutId() {
                return R.layout.card_item_order;
            }

            @Override
            public int getCount() {
                return dataList.size();
            }

            @Override
            public void bindView(View view, int index) {
                Object tag = view.getTag();
                CardHolder viewHolder;
                if (null != tag) {
                    viewHolder = (CardHolder) tag;
                } else {
                    viewHolder = new CardHolder(view);
                    view.setTag(viewHolder);
                }
                viewHolder.bindData(dataList.get(index));
            }

            @Override
            public Object getItem(int index) {
                return dataList.get(index);
            }

            @Override
            public Rect obtainDraggableArea(View view) {
                // 可滑动区域定制，该函数只会调用一次
                View contentView = view.findViewById(R.id.card_item_content);
                View topLayout = view.findViewById(R.id.card_top_layout);
                View bottomLayout = view.findViewById(R.id.card_bottom_layout);
                int left = view.getLeft() + contentView.getPaddingLeft() + topLayout.getPaddingLeft();
                int right = view.getRight() - contentView.getPaddingRight() - topLayout.getPaddingRight();
                int top = view.getTop() + contentView.getPaddingTop() + topLayout.getPaddingTop();
                int bottom = view.getBottom() - contentView.getPaddingBottom() - bottomLayout.getPaddingBottom();
                return new Rect(left, top, right, bottom);
            }
        });


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadData();
            }
        }, 500);
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
     *
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
                            User user = UserStorage.getInstance().getUser();
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
