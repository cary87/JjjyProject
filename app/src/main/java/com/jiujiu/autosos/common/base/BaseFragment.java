package com.jiujiu.autosos.common.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;

/**
 * 所有Fragment基类
 */
public abstract class BaseFragment extends Fragment {
    public AbsBaseActivity mActivity;
    protected boolean viewInited;
    protected CompositeDisposable cd = new CompositeDisposable();
    private Unbinder unbinder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (AbsBaseActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutID(), null, false);
        return  view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        viewInited = true;
        afterViewInited(view);
    }

    protected abstract int getLayoutID();

    protected abstract void afterViewInited(View view);

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        viewInited = false;
        unbinder.unbind();
        cd.clear();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivity = null;
    }
}
