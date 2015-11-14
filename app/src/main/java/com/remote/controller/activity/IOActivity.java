package com.remote.controller.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;

import com.remote.controller.R;

public class IOActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_io);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayUseLogoEnabled(true);
            actionBar.setTitle(R.string.setting_btn_io);
        }
    }

    @Override
    public void onClick(View view) {

    }
}
