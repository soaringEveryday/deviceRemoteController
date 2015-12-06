package com.remote.controller.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;

import com.remote.controller.R;
import com.remote.controller.bean.FileLineItem;
import com.remote.controller.constant.Constant;
import com.remote.controller.message.MessageEvent;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

public class MotionActivity extends BaseActivity {

    @Bind(R.id.btn_ok)
    Button btnOk;
    @Bind(R.id.btn_cancel)
    Button btnCancel;
    @Bind(R.id.et_x)
    EditText etX;
    @Bind(R.id.check_x)
    CheckBox checkX;
    @Bind(R.id.et_y)
    EditText etY;
    @Bind(R.id.check_y)
    CheckBox checkY;
    @Bind(R.id.et_z)
    EditText etZ;
    @Bind(R.id.check_z)
    CheckBox checkZ;
    @Bind(R.id.et_a)
    EditText etA;
    @Bind(R.id.check_a)
    CheckBox checkA;
    @Bind(R.id.et_v)
    EditText etV;
    @Bind(R.id.check_v_g)
    CheckBox checkVG;
    @Bind(R.id.et_g)
    EditText etG;
    @Bind(R.id.radio_line)
    RadioButton radioLine;
    @Bind(R.id.radio_move)
    RadioButton radioMove;

    private ArrayList<Integer> viewIds = new ArrayList<>();

    private int paramX;//32位有符号数
    private int paramY;
    private int paramZ;
    private int paramA;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motion);
        ButterKnife.bind(this);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayUseLogoEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.setting_sub_motion);
        }

        Intent intent = getIntent();
        if (intent != null) {
            //init parameter
            paramX = intent.getIntExtra(Constant.Param.X, 1);
            paramY = intent.getIntExtra(Constant.Param.Y, 1);
            paramZ = intent.getIntExtra(Constant.Param.Z, 1);
            paramA = intent.getIntExtra(Constant.Param.A, 1);
        }

        initViews();
    }

    private void initViews() {
        bindViewClicks();

        //初始化xyza
        etX.setText(String.valueOf(paramX));
        etY.setText(String.valueOf(paramY));
        etZ.setText(String.valueOf(paramZ));
        etA.setText(String.valueOf(paramA));

        checkX.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!b) {
                    etX.setEnabled(true);
                } else {
                    etX.setEnabled(false);
                }
            }
        });

        checkY.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!b) {
                    etY.setEnabled(true);
                } else {
                    etY.setEnabled(false);
                }
            }
        });

        checkZ.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!b) {
                    etZ.setEnabled(true);
                } else {
                    etZ.setEnabled(false);
                }
            }
        });

        checkA.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!b) {
                    etA.setEnabled(true);
                } else {
                    etA.setEnabled(false);
                }
            }
        });

        checkVG.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!b) {
                    etV.setEnabled(true);
                    etG.setEnabled(true);
                } else {
                    etV.setEnabled(false);
                    etG.setEnabled(false);
                }
            }
        });


    }

    private void bindViewClicks() {
        //bind view click
        viewIds.add(R.id.btn_ok);
        viewIds.add(R.id.btn_cancel);

        setViewClickListener(viewIds, getWindow().getDecorView());
    }

    private void insertMotionCmd() {
        String param = "";
        FileLineItem command = new FileLineItem();
        if (radioLine.isChecked()) {
            //直线运动
            if (checkVG.isChecked()) {
                //速度默认
                command.setCommand(Constant.Command.LINE_TO);
                param = etX.getText() + "," + etY.getText() + "," + etZ.getText() + "," + etA.getText();
            } else {
                //速度不默认
                command.setCommand(Constant.Command.LINE_TO_VEL);
                param = etX.getText() + "," + etY.getText() + "," + etZ.getText() + "," + etA.getText() + "," + etV.getText() + "," +etG.getText();

            }

        } else if (radioMove.isChecked()) {
            //点位运动
            if (checkVG.isChecked()) {
                //速度默认
                command.setCommand(Constant.Command.MOVE_TO);
                param = etX.getText() + "," + etY.getText() + "," + etZ.getText() + "," + etA.getText();
            } else {
                //速度不默认
                command.setCommand(Constant.Command.MOVE_TO_VEL);
                param = etX.getText() + "," + etY.getText() + "," + etZ.getText() + "," + etA.getText() + "," + etV.getText() + "," +etG.getText();

            }
        }
        command.setParameter(param);

        Message msg = Message.obtain();
        msg.what = MessageEvent.MSG_COMMAND_UPDATE;
        command.setNo(1);
        command.setMemo("");
        msg.obj = command;
        EventBus.getDefault().post(msg);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_ok:
                insertMotionCmd();
                finish();
                break;

            case R.id.btn_cancel:
                finish();
                break;
        }
    }

}
