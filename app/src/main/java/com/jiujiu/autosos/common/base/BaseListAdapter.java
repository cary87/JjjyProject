package com.jiujiu.autosos.common.base;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/26 0026.
 */
public class BaseListAdapter<T> extends BaseAdapter {
    protected List<T> mDatas = new ArrayList<>();
    protected Context mContext;

    public BaseListAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public int getCount() {
        if (mDatas == null) {
            return  0;
        }
        return mDatas.size();
    }

    @Override
    public T getItem(int i) {
        return mDatas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }

    public void setmDatas(List<T> mDatas) {
        this.mDatas = mDatas;
        notifyDataSetChanged();
    }

    public List<T> getmDatas() {
        return mDatas;
    }
}
