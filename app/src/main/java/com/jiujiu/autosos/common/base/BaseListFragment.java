package com.jiujiu.autosos.common.base;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.jiujiu.autosos.R;
import com.jiujiu.autosos.common.AppException;
import com.jiujiu.autosos.common.ResultStatus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * 列表fragment基类
 */
public abstract class BaseListFragment<T> extends BaseFragment {

    @BindView(R.id.listView)
    protected ListView mListView;
    @BindView(R.id.ptr_framelayout)
    PtrClassicFrameLayout mPtrFramelayout;
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
    public void afterViewInited(final View view) {
        initListView();
        initPtrFrame();
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

    private void initPtrFrame() {
        // the following are default settings
        mPtrFramelayout.setResistance(1.7f);
        mPtrFramelayout.setRatioOfHeaderHeightToRefresh(1.2f);
        mPtrFramelayout.setDurationToClose(200);
        mPtrFramelayout.setDurationToCloseHeader(1000);
        // default is false
        mPtrFramelayout.setPullToRefresh(false);
        // default is true
        mPtrFramelayout.setKeepHeaderWhenRefresh(true);
        mPtrFramelayout.setLastUpdateTimeRelateObject(this);
        mPtrFramelayout.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                loadData(true);
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }
        });
        mPtrFramelayout.postDelayed(new Runnable() {
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
                mPtrFramelayout.postDelayed(new Runnable() {
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
        if (getActivity() == null) {
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
        mPtrFramelayout.refreshComplete();
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
    protected void handleError(Exception e) {
        mIsLoading = false;
        if (getActivity() == null) {
            return;
        }
        if (e != null) {
            e.printStackTrace();
        }
        if (e != null && e instanceof AppException) {
            AppException appException = (AppException) e;
            String code = appException.getCode();
            /*if (Constant.CODE_SESSION_TIME_OUT.equals(code)) {
                DialogUtils.showConfirmDialog(mActivity, getString(R.string.account_other_device_login), new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                        switch (which) {
                            case POSITIVE:
                                //SystemUtil.logout(mActivity, LoginActivity.class);
                                break;
                            case NEGATIVE:
                                break;
                        }
                    }
                });
            } else {
                mActivity.showToast(e.getMessage());
            }*/
        }

        mListView.removeFooterView(mFooter);
        mPtrFramelayout.refreshComplete();
        if (mAdapter.getmDatas().size() == 0) {
            setResultStatus(ResultStatus.NETWORKERR);
        }
    }

    protected void handleEmpty() {
        mIsLoading = false;
        mListView.removeFooterView(mFooter);
        mPtrFramelayout.refreshComplete();
        setResultStatus(ResultStatus.NODATA);
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
            mPtrFramelayout.autoRefresh();
        }
    }

}
