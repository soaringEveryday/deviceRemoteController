package com.remote.controller.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.remote.controller.R;
import com.remote.controller.activity.DelayActivity;
import com.remote.controller.activity.IOActivity;
import com.remote.controller.activity.MotionActivity;
import com.remote.controller.adapter.CommonAdapter;
import com.remote.controller.adapter.ViewHolder;
import com.remote.controller.bean.FileLineItem;
import com.remote.controller.constant.Constant;
import com.remote.controller.message.MessageEvent;
import com.remote.controller.utils.L;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 示教主界面
 */
public class SettingFragment extends BaseFragment {


    @Bind(R.id.btn_motion)
    Button btnMotion;
    @Bind(R.id.btn_io)
    Button btnIo;
    @Bind(R.id.btn_delay)
    Button btnDelay;
    @Bind(R.id.btn_delete)
    Button btnDelete;
    @Bind(R.id.motion_btn_1)
    ImageButton motionBtn1;
    @Bind(R.id.motion_btn_2)
    ImageButton motionBtn2;
    @Bind(R.id.motion_btn_3)
    ImageButton motionBtn3;
    @Bind(R.id.motion_btn_4)
    ImageButton motionBtn4;
    @Bind(R.id.motion_btn_5)
    ImageButton motionBtn5;
    @Bind(R.id.motion_btn_6)
    ImageButton motionBtn6;
    @Bind(R.id.motion_btn_7)
    ImageButton motionBtn7;
    @Bind(R.id.motion_btn_8)
    ImageButton motionBtn8;
    @Bind(R.id.dis_1)
    RadioButton dis1;
    @Bind(R.id.dis_2)
    RadioButton dis2;
    @Bind(R.id.dis_3)
    RadioButton dis3;
    @Bind(R.id.dis_4)
    RadioButton dis4;
    @Bind(R.id.distance_zone)
    RadioGroup distanceZone;
    @Bind(R.id.param_x)
    TextView paramX;
    @Bind(R.id.param_y)
    TextView paramY;
    @Bind(R.id.param_z)
    TextView paramZ;
    @Bind(R.id.param_a)
    TextView paramA;
    @Bind(R.id.list)
    ListView list;

    private ArrayList<FileLineItem> mDatas;
    private BaseAdapter mAdaper;
    private int runningStatus = Constant.RunningStatus.NO_CONNECTION;
    public SettingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatas = new ArrayList<>();
        mAdaper = new CommonAdapter<FileLineItem>(mContext, mDatas, R.layout.file_list_item) {
            @Override
            public void convert(ViewHolder helper, FileLineItem item, int position) {
                helper.setText(R.id.no, String.valueOf(position));
                helper.setText(R.id.command, item.getCommand());
                helper.setText(R.id.parameter, item.getParameter());
                helper.setText(R.id.memo, item.getMemo());
            }
        };
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        L.d("onCreateView --> SettingFragment");

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        ButterKnife.bind(this, view);
        List<Integer> ids = new ArrayList<>();
        ids.add(R.id.btn_motion);
        ids.add(R.id.btn_io);
        ids.add(R.id.btn_delay);
        ids.add(R.id.btn_delete);
        setViewClickListener(ids, view);
        initListView();
        return view;
    }

    private void initListView() {
        list.setAdapter(mAdaper);
    }

    public void onEventMainThread(final Message msg) {
        L.d("setting onEvent");
        int msgEvent = msg.what;
        L.d("what:" + msgEvent);
        switch (msgEvent) {
            case MessageEvent.MSG_COMMAND_UPDATE:
                FileLineItem item = (FileLineItem) msg.obj;
                mDatas.add(item);
                mAdaper.notifyDataSetChanged();
                break;

            case MessageEvent.MSG_COMMAND_CLEAR:
                mDatas.clear();
                mAdaper.notifyDataSetChanged();
                break;

            case MessageEvent.MSG_SOCKET_DISCONNECTED:
                //断开连接成功
//                paramX.setText("null");
//                paramY.setText("null");
//                paramZ.setText("null");
//                paramA.setText("null");
                updateRunningStatus(Constant.RunningStatus.NO_CONNECTION);
                break;

            case MessageEvent.MSG_SOCKET_RECEIVE_DATA:
                //收到服务器回应
                int funcCode = msg.arg1;
                byte[] data = (byte[]) msg.obj;
                if (funcCode == Constant.EventCode.READ_RUNNING_STATE) {
                    int runningState = byte2int(data[0]);
                    updateRunningStatus(runningState);
                } else if (funcCode == Constant.EventCode.READ_DATA_ON_SETTING) {
                    paramX.setText(String.valueOf(byte2int(Arrays.copyOfRange(data, 0, 4))));
                    paramY.setText(String.valueOf(byte2int(Arrays.copyOfRange(data, 4, 8))));
                    paramZ.setText(String.valueOf(byte2int(Arrays.copyOfRange(data, 8, 12))));
                    paramA.setText(String.valueOf(byte2int(Arrays.copyOfRange(data, 12, 16))));
                }
                break;
        }
    }

    private void updateRunningStatus(int state) {
        runningStatus = state;
        if (state == Constant.RunningStatus.NO_CONNECTION) {
            motionBtn1.setEnabled(false);
            motionBtn2.setEnabled(false);
            motionBtn3.setEnabled(false);
            motionBtn4.setEnabled(false);
            motionBtn5.setEnabled(false);
            motionBtn6.setEnabled(false);
            motionBtn7.setEnabled(false);
            motionBtn8.setEnabled(false);
        } else {
            motionBtn1.setEnabled(true);
            motionBtn2.setEnabled(true);
            motionBtn3.setEnabled(true);
            motionBtn4.setEnabled(true);
            motionBtn5.setEnabled(true);
            motionBtn6.setEnabled(true);
            motionBtn7.setEnabled(true);
            motionBtn8.setEnabled(true);
        }
    }


    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.btn_motion:
                intent.setClass(mContext, MotionActivity.class);
                if (runningStatus != Constant.RunningStatus.NO_CONNECTION) {
                    intent.putExtra(Constant.Param.X, Integer.parseInt(paramX.getText().toString()));
                    intent.putExtra(Constant.Param.Y, Integer.parseInt(paramY.getText().toString()));
                    intent.putExtra(Constant.Param.Z, Integer.parseInt(paramZ.getText().toString()));
                    intent.putExtra(Constant.Param.A, Integer.parseInt(paramA.getText().toString()));
                }
                startActivity(intent);
                break;
            case R.id.btn_io:
                startActivity(new Intent(mContext, IOActivity.class));
                break;
            case R.id.btn_delay:
                startActivity(new Intent(mContext, DelayActivity.class));
                break;
            case R.id.btn_delete:

                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private int byte2int(byte b) {
        return b & 0xff;
    }

    private int byte2int(byte[] res) {
        // 一个byte数据左移24位变成0x??000000，再右移8位变成0x00??0000

        int targets = (res[0] & 0xff) | ((res[1] << 8) & 0xff00) // | 表示安位或
                | ((res[2] << 24) >>> 8) | (res[3] << 24);
        return targets;
    }
}
