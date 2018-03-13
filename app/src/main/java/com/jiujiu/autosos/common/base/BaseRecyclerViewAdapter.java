package com.jiujiu.autosos.common.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/20 0020.
 */
public abstract class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter<BaseRecyclerViewAdapter.ClickableViewHolder> {

    private Context context;

    private List<T> dataSet = new ArrayList<>();

    public BaseRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public interface OnItemClickListener
    {

        void onItemClick(int position, ClickableViewHolder holder);
    }

    public interface OnItemLongClickListener
    {

        boolean onItemLongClick(int position, ClickableViewHolder holder);
    }

    private OnItemClickListener itemClickListener;

    private OnItemLongClickListener itemLongClickListener;

    public void setOnItemClickListener(OnItemClickListener listener)
    {

        this.itemClickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener)
    {

        this.itemLongClickListener = listener;
    }

    public List<T> getDataSet() {
        return dataSet;
    }

    public void setDataSet(List<T> dataSet) {
        this.dataSet = dataSet;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(final ClickableViewHolder holder, final int position)
    {

        holder.getParentView().setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {

                if (itemClickListener != null)
                {
                    itemClickListener.onItemClick(position, holder);
                }
            }
        });
        holder.getParentView().setOnLongClickListener(new View.OnLongClickListener()
        {

            @Override
            public boolean onLongClick(View v)
            {

                if (itemLongClickListener != null)
                {
                    return itemLongClickListener.onItemLongClick(position, holder);
                } else
                {
                    return false;
                }
            }
        });
    }

    public static class ClickableViewHolder extends RecyclerView.ViewHolder
    {

        private View parentView;

        public ClickableViewHolder(View itemView)
        {

            super(itemView);
            this.parentView = itemView;
        }

        public View getParentView()
        {

            return parentView;
        }
    }

    @Override
    public int getItemCount() {
        if (dataSet != null) {
            return dataSet.size();
        }
        return 0;
    }

    public T getItem(int position) {
        return dataSet.get(position);
    }
}
