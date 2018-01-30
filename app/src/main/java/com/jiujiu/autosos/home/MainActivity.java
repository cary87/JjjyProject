package com.jiujiu.autosos.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.jiujiu.autosos.R;
import com.jiujiu.autosos.common.base.AbsBaseActivity;
import com.jiujiu.autosos.common.model.BottomTabEntity;
import com.jiujiu.autosos.common.utils.PushUtils;
import com.jiujiu.autosos.me.MeFragment;
import com.jiujiu.autosos.nav.TTSController;
import com.jiujiu.autosos.order.OrderDialog;
import com.jiujiu.autosos.order.OrderFragment;
import com.jiujiu.autosos.order.OrderUtil;
import com.jiujiu.autosos.order.model.OrderModel;
import com.jiujiu.autosos.setting.SettingFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import butterknife.BindView;

public class MainActivity extends AbsBaseActivity {
    @BindView(R.id.vp_container)
    ViewPager mViewPagerContainer;
    @BindView(R.id.bottom_layout)
    CommonTabLayout mBottomLayout;
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private String[] mTitles;
    private int[] mIconUnselectIds = {
            R.drawable.workbench_normal, R.drawable.order_normal,
            R.drawable.me_normal, R.drawable.setting_normal};
    private int[] mIconSelectIds = {
            R.drawable.workbench_press, R.drawable.order_press,
            R.drawable.me_press, R.drawable.setting_press};

    protected TTSController mTtsManager;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);

        //实例化语音引擎
        mTtsManager = TTSController.getInstance(getApplicationContext());
        mTtsManager.setTTSType(TTSController.TTSType.SYSTEMTTS);
        mTtsManager.init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        PushUtils.handlePush(this, getIntent());
        PushUtils.initPush(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mTtsManager.stopSpeaking();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        mTtsManager.destroy();
    }

    @Subscribe
    public void onReceiveOrder(final OrderModel order) {
        mTtsManager.playText(getString(R.string.order_coming));
        OrderDialog dialog = new OrderDialog(this, order, new OrderDialog.OnAcceptOrderListener() {
            @Override
            public void onAcceptOrder() {
                OrderUtil.acceptOrder(MainActivity.this, order, null);
            }
        });
        dialog.show();
    }

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        mFragments.add(new WorkbenchFragment());
        mFragments.add(new OrderFragment());
        mFragments.add(new MeFragment());
        mFragments.add(new SettingFragment());
        mTitles = new String[] {getString(R.string.tab_work), getString(R.string.tab_order), getString(R.string.tab_profile), getString(R.string.tab_setting)};
        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new BottomTabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }
        mViewPagerContainer.setOffscreenPageLimit(3);
        mViewPagerContainer.setAdapter(new AppPagerAdapter(getSupportFragmentManager()));
        mBottomLayout.setTabData(mTabEntities);
        mBottomLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mViewPagerContainer.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });

        mViewPagerContainer.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mBottomLayout.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private class AppPagerAdapter extends FragmentPagerAdapter {
        public AppPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
