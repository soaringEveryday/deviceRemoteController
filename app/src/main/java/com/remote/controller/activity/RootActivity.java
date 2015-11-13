package com.remote.controller.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.remote.controller.R;
import com.remote.controller.fragment.ConnectFragment;
import com.remote.controller.fragment.FileFragment;
import com.remote.controller.fragment.PlayFragment;
import com.remote.controller.fragment.SettingFragment;
import com.remote.controller.utils.L;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Chen Haitao on 2015/11/13.
 */
public class RootActivity extends BaseActivity {

    @Bind(R.id.id_viewpager)
    ViewPager idViewpager;
    @Bind(R.id.icon1)
    ImageView icon1;
    @Bind(R.id.id_indicator_one)
    LinearLayout idIndicatorOne;
    @Bind(R.id.icon2)
    ImageView icon2;
    @Bind(R.id.id_indicator_two)
    LinearLayout idIndicatorTwo;
    @Bind(R.id.icon3)
    ImageView icon3;
    @Bind(R.id.id_indicator_three)
    LinearLayout idIndicatorThree;
    @Bind(R.id.icon4)
    ImageView icon4;
    @Bind(R.id.id_indicator_four)
    LinearLayout idIndicatorFour;
    @Bind(R.id.text1)
    TextView text1;
    @Bind(R.id.text2)
    TextView text2;
    @Bind(R.id.text3)
    TextView text3;
    @Bind(R.id.text4)
    TextView text4;

    private final int PAGE_SIZE = 4;
    private final int POS_SETTING = 0;
    private final int POS_PLAY = 1;
    private final int POS_FILE = 2;
    private final int POS_CONNECT = 3;

    private ArrayList<Integer> viewIds = new ArrayList<>();
    private Adapter mPagerAdapter;
    private Fragment[] rootFragments = new Fragment[PAGE_SIZE];
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_root);
        ButterKnife.bind(this);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayUseLogoEnabled(true);
        }
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            super.exitApplication(RootActivity.this);
        }
        return true;
    }

    private void initView() {
        //bind view click
        viewIds.add(R.id.id_indicator_one);
        viewIds.add(R.id.id_indicator_two);
        viewIds.add(R.id.id_indicator_three);
        viewIds.add(R.id.id_indicator_four);
        setViewClickListener(viewIds, getWindow().getDecorView());

        //init tab indicator

        //set up view pager
        if (idViewpager != null) {
            setupViewPager(idViewpager);
        }

        clickTab(POS_CONNECT);
    }

    private void setupViewPager(ViewPager viewPager) {
        mPagerAdapter = new Adapter(getSupportFragmentManager());
        for (int i = 0; i < PAGE_SIZE; i++) {
            L.v("rootActivity --- create fragment [pos " + i + "]");
            switch (i) {
                case POS_SETTING:
                    rootFragments[i] = new SettingFragment();
                    break;
                case POS_PLAY:
                    rootFragments[i] = new PlayFragment();
                    break;
                case POS_FILE:
                    rootFragments[i] = new FileFragment();
                    break;
                case POS_CONNECT:
                    rootFragments[i] = new ConnectFragment();
                    break;
            }
            rootFragments[i].setRetainInstance(true);
            mPagerAdapter.addFragment(rootFragments[i], getFragmentTag(i));
        }
        viewPager.setAdapter(mPagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                changeTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private String getFragmentTag(int pos) {
        String tag = "pos_default";
        switch (pos) {
            case POS_SETTING:
                tag = getString(R.string.tab_setting);
                break;
            case POS_PLAY:
                tag = getString(R.string.tab_run);
                break;
            case POS_FILE:
                tag = getString(R.string.tab_file);
                break;
            case POS_CONNECT:
                tag = getString(R.string.tab_connect);
                break;
        }
        return tag;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_indicator_one:
                clickTab(POS_SETTING);
                break;
            case R.id.id_indicator_two:
                clickTab(POS_PLAY);
                break;
            case R.id.id_indicator_three:
                clickTab(POS_FILE);
                break;
            case R.id.id_indicator_four:
                clickTab(POS_CONNECT);
                break;
        }
    }

    private void clickTab(int tabType) {
        idViewpager.setCurrentItem(tabType, true);
        changeTab(tabType);
    }

    private void changeTab(int tabType) {
        switch (tabType) {
            case POS_SETTING:
                icon1.setImageResource(R.drawable.iconfont_setting_select);
                icon2.setImageResource(R.drawable.iconfont_iconplay);
                icon3.setImageResource(R.drawable.iconfont_file);
                icon4.setImageResource(R.drawable.iconfont_connect);
                break;
            case POS_PLAY:
                icon1.setImageResource(R.drawable.iconfont_setting);
                icon2.setImageResource(R.drawable.iconfont_iconplay_select);
                icon3.setImageResource(R.drawable.iconfont_file);
                icon4.setImageResource(R.drawable.iconfont_connect);
                break;
            case POS_FILE:
                icon1.setImageResource(R.drawable.iconfont_setting);
                icon2.setImageResource(R.drawable.iconfont_iconplay);
                icon3.setImageResource(R.drawable.iconfont_file_select);
                icon4.setImageResource(R.drawable.iconfont_connect);
                break;
            case POS_CONNECT:
                icon1.setImageResource(R.drawable.iconfont_setting);
                icon2.setImageResource(R.drawable.iconfont_iconplay);
                icon3.setImageResource(R.drawable.iconfont_file);
                icon4.setImageResource(R.drawable.iconfont_connect_select);
                break;
        }

        updateToolbar(tabType);
    }

    private void updateToolbar(int tabType) {
        switch (tabType) {
            case POS_SETTING:
                if (actionBar != null) {
                    actionBar.setTitle(R.string.tab_setting);
                }
                break;
            case POS_PLAY:
                if (actionBar != null) {
                    actionBar.setTitle(R.string.tab_run);
                }
                break;
            case POS_FILE:
                if (actionBar != null) {
                    actionBar.setTitle(R.string.tab_file);
                }
                break;
            case POS_CONNECT:
                if (actionBar != null) {
                    actionBar.setTitle(R.string.tab_connect);
                }
                break;
        }
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
    }
}
