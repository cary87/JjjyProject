package com.jiujiu.autosos.me;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jiujiu.autosos.R;
import com.jiujiu.autosos.common.base.BaseListAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/1/31.
 */

public class BankCardAdapter extends BaseListAdapter<BankCard> {
    public BankCardAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        CardViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.list_item_bank_card, null);
            holder = new CardViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (CardViewHolder) view.getTag();
        }
        holder.tvBankName.setText(getItem(i).getBankName());
        String bankAccount = getItem(i).getBankAccount();
        if (!TextUtils.isEmpty(bankAccount) && bankAccount.length() > 4) {
            String endCardNO = bankAccount.substring(bankAccount.length() - 4, bankAccount.length());
            holder.tvBankAccount.setText("尾号" + endCardNO + "储蓄卡");
        } else {
            holder.tvBankAccount.setText(bankAccount);
        }
        return view;
    }

    static class CardViewHolder {
        @BindView(R.id.tv_bank_name)
        TextView tvBankName;
        @BindView(R.id.tv_bank_account)
        TextView tvBankAccount;

        CardViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
