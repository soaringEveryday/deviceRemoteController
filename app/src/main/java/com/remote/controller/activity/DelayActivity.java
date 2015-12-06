package com.remote.controller.activity;

import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.remote.controller.R;
import com.remote.controller.bean.FileLineItem;
import com.remote.controller.constant.Constant;
import com.remote.controller.message.MessageEvent;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

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
            actionBar.setDisplayHomeAsUpEnabled(true);
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
                finish();
                break;

            case R.id.btn_cancel:
                finish();
                break;
        }
    }

    private void insertDelayCmd() {
        long time = Long.parseLong(etTime.getText().toString());
        if (time <= 0) {
            Toast.makeText(DelayActivity.this, "延时时间不可小于0", Toast.LENGTH_SHORT).show();
            return;
        }
        FileLineItem command = new FileLineItem();
        command.setCommand(Constant.Command.DELAY);
        command.setParameter(String.valueOf(time) + "," + String.valueOf(spinnerUnit.getSelectedItemPosition()));
        command.setNo(1);
        command.setMemo("");

        Message msg = Message.obtain();
        msg.what = MessageEvent.MSG_COMMAND_UPDATE;
        msg.obj = command;
        EventBus.getDefault().post(msg);
    }
}
