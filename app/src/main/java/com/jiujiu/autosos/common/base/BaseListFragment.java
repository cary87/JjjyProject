package com.jiujiu.autosos.common.base;

import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.jiujiu.autosos.R;
import com.jiujiu.autosos.common.AppException;
import com.jiujiu.autosos.common.Constant;
import com.jiujiu.autosos.common.ResultStatus;
import com.jiujiu.autosos.common.utils.DialogUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 列表fragment基类
 */
public abstract class BaseListFragment<T> extends BaseFragment {

    @BindView(R.id.listView)
    protected ListView mListView;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.layout_content)
    LinearLayout mLayoutContent;
    @BindView(R.id.layout_msg_error)
    LinearLayout mLayoutMsgError;
    @BindView(R.id.iv_icon)
    ImageView mIvIcon;
    @BindView(R.id.tv_content)
    TextView mTvContent;

    protected BaseListAdapter<T> mAdapter;
    protected View mFooter;

    /**
     * 是否正在加载数据
     */
    private boolean mIsLoading = false;

    /**
     * 当前页
     */
    protected int currentPage = 1;

    /**
     * 是否可以加载更多
     */
    protected boolean hasMore = true;

    @Override
    protected void afterViewInited(final View view) {
        initListView();
        initSwipeRefresh();
        initMsgLayout();
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_list;
    }

    /**
     * 初始化Listview
     */
    private void initListView() {
        if (supportLoadMore()) {
            mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView absListView, int i) {
                    if (i == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                        if (hasMore && !mIsLoading && absListView.getLastVisiblePosition() == absListView.getCount() - 1) {
                            mListView.addFooterView(mFooter);
                            loadData(false);
                            mIsLoading = true;
                        }
                    }
                }

                @Override
                public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                }
            });
        }
        mFooter = LayoutInflater.from(getContext()).inflate(R.layout.list_layout_footer, null);
        mFooter.setOnClickListener(null);
        if (mAdapter == null) {
            mAdapter = getListAdapter();
        }
        mListView.addFooterView(mFooter);
        mListView.setAdapter(mAdapter);
        mListView.removeFooterView(mFooter);
    }

    private void initSwipeRefresh() {
        mSwipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.white);
        int[] colors = getResources().getIntArray(R.array.google_colors);
        mSwipeRefreshLayout.setColorSchemeColors(colors);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData(true);
            }
        });
        mSwipeRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                autoRefresh();
            }
        }, 200);
    }

    /**
     * 初始化消息布局
     */
    private void initMsgLayout() {
        mLayoutMsgError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modifyUiState(true);
                mSwipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        autoRefresh();
                    }
                }, 200);
            }
        });
    }

    /**
     * 设置
     *
     * @param status
     */
    protected void setResultStatus(ResultStatus status) {
        switch (status) {
            case NODATA:
                noData();
                break;
            case NETWORKERR:
                netError();
                break;
            case NORMAL:
                modifyUiState(true);
                break;
        }
    }

    private void noData() {
        modifyUiState(false);
        mTvContent.setText(getResources().getString(R.string.no_data));
        mIvIcon.setImageResource(R.drawable.icon_no_data);
    }

    private void netError() {
        modifyUiState(false);
        mTvContent.setText(getResources().getString(R.string.net_error));
        mIvIcon.setImageResource(R.drawable.icon_net_error);
    }

    /**
     * 处理网络请求返回数据
     *
     * @param list
     * @param args 第一个传入是否下拉刷新，第二个传入是否还有更多数据
     */
    protected void handleResponse(List<T> list, boolean... args) {
        mIsLoading = false;
        if (getActivity() == null || !viewInited) {
            return;
        }
        if (args[0]) {
            mAdapter.getmDatas().clear();
        }
        if (list == null) {
            list = new ArrayList<>();
        }
        if(supportLoadMore()) {
            if(args.length > 1 && args[1]) {
                currentPage++;
                hasMore = true;
            } else if (list.size() >= 20) {
                currentPage++;
                hasMore = true;
            } else {
                hasMore = false;
            }
        }
        mAdapter.getmDatas().addAll(list);
        mAdapter.notifyDataSetChanged();
        mSwipeRefreshLayout.setRefreshing(false);
        mListView.removeFooterView(mFooter);
        if (mAdapter.getmDatas().size() == 0) {
            setResultStatus(ResultStatus.NODATA);
        } else {
            setResultStatus(ResultStatus.NORMAL);
        }
    }

    /**
     * 处理网路请求异常信息
     *
     * @param e
     */
    protected void handleError(Throwable e) {
        mIsLoading = false;
        if (getActivity() == null || !viewInited) {
            return;
        }
        if (e != null) {
            e.printStackTrace();
        }
        if (e != null && e instanceof AppException) {
            AppException appException = (AppException) e;
            String code = appException.getCode();
            if (Constant.CODE_SESSION_TIME_OUT.equals(code)) {
                DialogUtils.showConfirmDialog(mActivity, "该账号已在其他设备登录，请重新登录", new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                        switch (which) {
                            case POSITIVE:
                                break;
                            case NEGATIVE:
                                break;
                        }
                    }
                });
            } else {
                mActivity.showToast(e.getMessage());
            }
        }

        mListView.removeFooterView(mFooter);
        mSwipeRefreshLayout.setRefreshing(false);
        if (mAdapter.getmDatas().size() == 0) {
            setResultStatus(ResultStatus.NETWORKERR);
        }
    }

    /**
     * 修改布局界面
     * true:显示content页面
     * false:显示异常页面
     *
     * @param b
     */
    private void modifyUiState(boolean b) {
        if (b) {
            mLayoutContent.setVisibility(View.VISIBLE);
            mLayoutMsgError.setVisibility(View.GONE);
        } else {
            mLayoutContent.setVisibility(View.GONE);
            mLayoutMsgError.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 数据加载
     */
    protected abstract void loadData(boolean isPullToReflesh);

    protected abstract BaseListAdapter<T> getListAdapter();

    protected boolean supportLoadMore() {
        return true;
    }

    public void autoRefresh() {
        if (viewInited) {
            mSwipeRefreshLayout.setRefreshing(true);
            loadData(true);
        }
    }

}
