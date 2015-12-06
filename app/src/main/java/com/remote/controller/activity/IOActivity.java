package com.remote.controller.activity;

import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.remote.controller.R;
import com.remote.controller.bean.FileLineItem;
import com.remote.controller.constant.Constant;
import com.remote.controller.message.MessageEvent;
import com.remote.controller.utils.L;
import com.remote.controller.utils.SPUtils;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

public class IOActivity extends BaseActivity {

    @Bind(R.id.btn_ok)
    Button btnOk;
    @Bind(R.id.btn_cancel)
    Button btnCancel;
    @Bind(R.id.port1)
    Spinner port1;
    @Bind(R.id.status1)
    Spinner status1;
    @Bind(R.id.pc_status1)
    Spinner pcStatus1;
    @Bind(R.id.port2)
    Spinner port2;
    @Bind(R.id.status2)
    Spinner status2;
    @Bind(R.id.pc_status2)
    Spinner pcStatus2;
    @Bind(R.id.port3)
    Spinner port3;
    @Bind(R.id.status3)
    Spinner status3;
    @Bind(R.id.pc_status3)
    Spinner pcStatus3;
    @Bind(R.id.port4)
    Spinner port4;
    @Bind(R.id.status4)
    Spinner status4;
    @Bind(R.id.pc_status4)
    Spinner pcStatus4;
    @Bind(R.id.btn_output)
    Button btnOutput;
    @Bind(R.id.radio_input)
    RadioButton radioInput;
    @Bind(R.id.radio_output)
    RadioButton radioOutput;

    private final int MODE_INPUT = 1;
    private final int MODE_OUTPUT = 2;

    private String[] ports_input;
    private String[] ports_output;
    private String[] status;
    private ArrayList<Integer> viewIds = new ArrayList<>();
    private ArrayAdapter<String> portsAdapter;
    private ArrayAdapter<String> statusAdapter;
    private int mode = MODE_INPUT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_io);
        ButterKnife.bind(this);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayUseLogoEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.setting_sub_io);
        }

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this, 1);
        }

        initViews();

        SPUtils.put(this, Constant.SPKEY.SYNC_IO_CMD, 1);//默认进入开始获取输入实时数据
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        SPUtils.put(this, Constant.SPKEY.SYNC_IO_CMD, 0);//默认进入开始获取输入实时数据

    }

    private void bindViewClicks() {
        //bind view click
        viewIds.add(R.id.btn_ok);
        viewIds.add(R.id.btn_cancel);
        viewIds.add(R.id.btn_output);

        setViewClickListener(viewIds, getWindow().getDecorView());

        radioInput.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    btnOutput.setEnabled(false);
                    portsAdapter = new ArrayAdapter<String>(IOActivity.this, android.R.layout.simple_spinner_dropdown_item, ports_input);
                    port1.setAdapter(portsAdapter);
                    port2.setAdapter(portsAdapter);
                    port3.setAdapter(portsAdapter);
                    port4.setAdapter(portsAdapter);
                    portsAdapter.notifyDataSetChanged();
                    mode = MODE_INPUT;
                    SPUtils.put(IOActivity.this, Constant.SPKEY.SYNC_IO_CMD, 1);

                } else {
                    btnOutput.setEnabled(true);
                    portsAdapter = new ArrayAdapter<String>(IOActivity.this, android.R.layout.simple_spinner_dropdown_item, ports_output);
                    port1.setAdapter(portsAdapter);
                    port2.setAdapter(portsAdapter);
                    port3.setAdapter(portsAdapter);
                    port4.setAdapter(portsAdapter);
                    portsAdapter.notifyDataSetChanged();
                    mode = MODE_OUTPUT;
                    SPUtils.put(IOActivity.this, Constant.SPKEY.SYNC_IO_CMD, 2);
                }
            }
        });
    }

    private void initViews() {

        ports_input = getResources().getStringArray(R.array.port_input);
        ports_output = getResources().getStringArray(R.array.port_output);
        status = getResources().getStringArray(R.array.status);
        portsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, ports_input);
        statusAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, status);

        port1.setAdapter(portsAdapter);
        port2.setAdapter(portsAdapter);
        port3.setAdapter(portsAdapter);
        port4.setAdapter(portsAdapter);

        status1.setAdapter(statusAdapter);
        status2.setAdapter(statusAdapter);
        status3.setAdapter(statusAdapter);
        status4.setAdapter(statusAdapter);
        pcStatus1.setAdapter(statusAdapter);
        pcStatus2.setAdapter(statusAdapter);
        pcStatus3.setAdapter(statusAdapter);
        pcStatus4.setAdapter(statusAdapter);

        pcStatus1.setEnabled(false);
        pcStatus2.setEnabled(false);
        pcStatus3.setEnabled(false);
        pcStatus4.setEnabled(false);



        port1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    status1.setSelection(0);
                    pcStatus1.setSelection(0);
                    status1.setEnabled(false);
                    pcStatus1.setEnabled(false);
                } else {
                    status1.setEnabled(true);
//                    pcStatus1.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        port2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    status2.setSelection(0);
                    pcStatus2.setSelection(0);
                    status2.setEnabled(false);
                    pcStatus2.setEnabled(false);
                } else {
                    status2.setEnabled(true);
//                    pcStatus2.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        port3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    status3.setSelection(0);
                    pcStatus3.setSelection(0);
                    status3.setEnabled(false);
                    pcStatus3.setEnabled(false);
                } else {
                    status3.setEnabled(true);
//                    pcStatus3.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        port4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    status4.setSelection(0);
                    pcStatus4.setSelection(0);
                    status4.setEnabled(false);
                    pcStatus4.setEnabled(false);
                } else {
                    status4.setEnabled(true);
//                    pcStatus4.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        bindViewClicks();
    }

    public void onEventMainThread(final Message msg) {
        int msgEvent = msg.what;
        switch (msgEvent) {
            case MessageEvent.MSG_SOCKET_RECEIVE_DATA:
                //收到服务器回应
                int funcCode = msg.arg1;
                byte[] data = (byte[]) msg.obj;
                if (funcCode == Constant.EventCode.READ_INPUT) {
                    refreshCurrentInputState(data);
                } else if (funcCode == Constant.EventCode.READ_OUTPUT) {
                    refreshCurrentInputState(data);
                }
                break;
        }
    }

    private void refreshCurrentInputState(byte[] data) {
        byte[] status = new byte[32];
        for (int i = 0 ; i < 8 ; i++) {
            //取出data[0] 的 0 ~ 7位， 即0~ 7bit
            status[i] = (byte)((data[0] >> i) & 0x1);
        }

        for (int i = 0 ; i < 8 ; i++) {
            //取出data[1] 的 0 ~ 7位， 即8~ 15bit
            status[8 + i] = (byte)((data[1] >> i) & 0x1);
        }

        for (int i = 0 ; i < 8 ; i++) {
            //取出data[2] 的 0 ~ 7位， 即16~ 23bit
            status[16 + i] = (byte)((data[2] >> i) & 0x1);
        }

        for (int i = 0 ; i < 8 ; i++) {
            //取出data[3] 的 0 ~ 7位， 即24~ 31bit
            status[23 + i] = (byte)((data[3] >> i) & 0x1);
        }

        int pos1 = port1.getSelectedItemPosition();
        int pos2 = port2.getSelectedItemPosition();
        int pos3 = port3.getSelectedItemPosition();
        int pos4 = port4.getSelectedItemPosition();

        if (pos1 != 0) {
            pcStatus1.setSelection((status[pos1 - 1]) == 1 ? 1 : 2);
        }

        if (pos2 != 0) {
            pcStatus2.setSelection((status[pos2 - 1]) == 1 ? 1 : 2);
        }

        if (pos3 != 0) {
            pcStatus3.setSelection((status[pos3 - 1]) == 1 ? 1 : 2);
        }

        if (pos4 != 0) {
            pcStatus4.setSelection((status[pos4 - 1]) == 1 ? 1 : 2);
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_ok:
                insertIOCmd();
                finish();
                break;

            case R.id.btn_cancel:
                finish();
                break;

            case R.id.btn_output:
                sendOutput();
                break;
        }

    }

    private void insertIOCmd() {
        String param = "";
        FileLineItem command = new FileLineItem();
        int pos1 = port1.getSelectedItemPosition();
        int pos2 = port2.getSelectedItemPosition();
        int pos3 = port3.getSelectedItemPosition();
        int pos4 = port4.getSelectedItemPosition();
        int s1 = status1.getSelectedItemPosition();
        int s2 = status2.getSelectedItemPosition();
        int s3 = status3.getSelectedItemPosition();
        int s4 = status4.getSelectedItemPosition();
        if (radioInput.isChecked()) {
            //插入等待输入指令
            command.setCommand(Constant.Command.WAIT_DI);

            if (pos1 != 0 && s1 != 0) {
                //等待输入状态下，端口位置为0表示无效
                param = param + String.valueOf(pos1 - 1) + "," + String.valueOf(s1 - 1) + ",";
            }
            if (pos2 != 0 && s2 != 0) {
                param = param + String.valueOf(pos2 - 1) + "," + String.valueOf(s2 - 1) + ",";
            }
            if (pos3 != 0 && s3 != 0) {
                param = param + String.valueOf(pos3 - 1) + "," + String.valueOf(s3 - 1) + ",";
            }
            if (pos4 != 0 && s4 != 0) {
                param = param + String.valueOf(pos4 - 1) + "," + String.valueOf(s4 - 1) + ",";
            }

        } else {
            //插入设置输出指令
            command.setCommand(Constant.Command.SET_DO);
            if (s1 != 0) {
                //等待输入状态下，端口位置为0表示无效
                param = param + String.valueOf(pos1) + "," + String.valueOf(s1 - 1) + ",";
            }
            if (s2 != 0) {
                param = param + String.valueOf(pos2) + "," + String.valueOf(s2 - 1) + ",";
            }
            if (s3 != 0) {
                param = param + String.valueOf(pos3) + "," + String.valueOf(s3 - 1) + ",";
            }
            if (s4 != 0) {
                param = param + String.valueOf(pos4) + "," + String.valueOf(s4 - 1) + ",";
            }
        }
        if (param.isEmpty()) {
            L.e("param is empty");
            return;
        }
        //去掉末尾的","
        L.d("param(before) :" + param);
        param = param.substring(0, param.length() - 1);
        L.d("param(after) :" + param);
        command.setParameter(param);
        command.setNo(1);
        command.setMemo("");

        Message msg = Message.obtain();
        msg.what = MessageEvent.MSG_COMMAND_UPDATE;
        msg.obj = command;
        EventBus.getDefault().post(msg);
    }

    private void sendOutput() {

    }
}
