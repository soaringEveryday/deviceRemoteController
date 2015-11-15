package com.remote.controller.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.remote.controller.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DelayActivity extends BaseActivity {

    @Bind(R.id.et_time)
    EditText etTime;
    @Bind(R.id.spinner_unit)
    Spinner spinnerUnit;
    @Bind(R.id.btn_ok)
    Button btnOk;
    @Bind(R.id.btn_cancel)
    Button btnCancel;

    private String[] units;
    private ArrayList<Integer> viewIds = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delay);
        ButterKnife.bind(this);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayUseLogoEnabled(true);
            actionBar.setTitle(R.string.setting_sub_delay);
        }

        initViews();
    }

    private void bindViewClicks() {
        //bind view click
        viewIds.add(R.id.btn_ok);
        viewIds.add(R.id.btn_cancel);

        setViewClickListener(viewIds, getWindow().getDecorView());
    }

    private void initViews() {
        bindViewClicks();
        units = getResources().getStringArray(R.array.units);
        ArrayAdapter<String> _Adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, units);
        spinnerUnit.setAdapter(_Adapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_ok:
                insertDelayCmd();
                break;

            case R.id.btn_cancel:
                finish();
                break;
        }
    }

    private void insertDelayCmd() {

    }
}
