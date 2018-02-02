package com.jiujiu.autosos.me;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.jiujiu.autosos.R;
import com.jiujiu.autosos.common.base.AbsBaseActivity;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/1/31.
 */

public class BankCardListActivity extends AbsBaseActivity implements BankCardListFragment.OptionMenuVisibilityCallback {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private Menu optionMenu;

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        tvTitle.setText("银行卡");
        setupToolbar(toolbar);
        BankCardListFragment cardListFragment = (BankCardListFragment) getSupportFragmentManager().findFragmentById(R.id.fg);
        cardListFragment.setCallback(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.choose_bank_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        optionMenu = menu;
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(this, AddBankCardActivity.class);
        startActivity(intent);
        return true;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_bank;
    }

    @Override
    public void optionMenuVisibility(boolean show) {
        if (optionMenu != null) {
            optionMenu.findItem(R.id.menu_save).setVisible(show);
        }
    }
}
