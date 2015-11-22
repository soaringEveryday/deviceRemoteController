package com.remote.controller.fragment;


import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.remote.controller.R;
import com.remote.controller.adapter.CommonAdapter;
import com.remote.controller.adapter.ViewHolder;
import com.remote.controller.bean.FileLineItem;
import com.remote.controller.constant.Constant;
import com.remote.controller.message.MessageEvent;
import com.remote.controller.utils.L;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 运行主界面
 */
public class PlayFragment extends BaseFragment {


    @Bind(R.id.btn_launch)
    Button btnLaunch;
    @Bind(R.id.btn_pause)
    Button btnPause;
    @Bind(R.id.btn_stop)
    Button btnStop;
    @Bind(R.id.btn_reset)
    Button btnReset;
    @Bind(R.id.et_times_run_total)
    EditText etTimesRunTotal;
    @Bind(R.id.tv_finished_times)
    TextView tvFinishedTimes;
    @Bind(R.id.tv_running_status)
    TextView tvRunningStatus;
    @Bind(R.id.list)
    ListView list;
    @Bind(R.id.param_x)
    TextView data1;
    @Bind(R.id.param_y)
    TextView data2;
    @Bind(R.id.param_z)
    TextView data3;
    @Bind(R.id.param_a)
    TextView data4;

    private int runningStatus = Constant.RunningStatus.NO_CONNECTION;
    private ArrayList<FileLineItem> mDatas;
    private BaseAdapter mAdaper;

    public PlayFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatas = new ArrayList<>();
        mAdaper = new CommonAdapter<FileLineItem>(mContext, mDatas, R.layout.file_list_item) {
            @Override
            public void convert(ViewHolder helper, FileLineItem item, int position) {
                helper.setText(R.id.no, String.valueOf(item.getNo()));
                helper.setText(R.id.command, item.getCommand());
                helper.setText(R.id.parameter, item.getParameter());
                helper.setText(R.id.memo, item.getMemo());
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        L.d("onCreateView --> PlayFragment");

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_play, container, false);
        ButterKnife.bind(this, view);

        List<Integer> ids = new ArrayList<>();
        ids.add(R.id.btn_launch);
        ids.add(R.id.btn_pause);
        ids.add(R.id.btn_stop);
        ids.add(R.id.btn_reset);
        setViewClickListener(ids, view);
        initListView();
        return view;
    }

    private void initListView() {
        list.setAdapter(mAdaper);
    }

    //根据runningStatus设置四个btn的有效性
    private void refreshActionBtns() {

    }


    //TODO get running status from socket engine
    private void getRunningStatus() {

    }

    //TODO 从PC读取数据返回显示
    private void refreshDataPane() {

    }

    public void onEvent(final Message msg) {
        L.d("play onEvent");
        int msgEvent = msg.what;
        L.d("what:" + msgEvent);
        switch (msgEvent) {
            case MessageEvent.MSG_COMMAND_UPDATE:
                mDatas = (ArrayList<FileLineItem>) msg.obj;
                for (FileLineItem item : mDatas) {
                    L.v("" + item.getNo() + "," + item.getCommand() + "," + item.getParameter() + "," + item.getMemo());
                }
                if (mDatas != null) {
                    L.d("data size : " + mDatas.size());
                } else {
                    L.d("data is null");
                }
                mAdaper.notifyDataSetChanged();
                break;
        }
    }

    private void insertCommand() {
        L.d("insertCommand");
        FileLineItem command = new FileLineItem();
        command.setCommand("LineTo");
        command.setParameter("100,500");
        command.setNo(1);
        command.setMemo("无");
        mDatas.add(command);
        mAdaper.notifyDataSetChanged();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_launch:
                insertCommand();
                break;
            case R.id.btn_pause:

                break;
            case R.id.btn_stop:

                break;
            case R.id.btn_reset:

                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    //用event bus通知运行状态，更新其他界面的按钮有效性
    private void updateRunningStatus() {

    }
}
