package com.remote.controller.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;

import com.remote.controller.R;

public class DelayActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delay);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayUseLogoEnabled(true);
            actionBar.setTitle(R.string.setting_btn_delay);
        }
    }

    @Override
    public void onClick(View view) {

    }
}
