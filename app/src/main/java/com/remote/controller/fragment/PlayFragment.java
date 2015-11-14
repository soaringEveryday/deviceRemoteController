package com.remote.controller.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.remote.controller.R;
import com.remote.controller.bean.FileLineItem;
import com.remote.controller.constant.Constant;
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
    @Bind(R.id.data1)
    TextView data1;
    @Bind(R.id.data2)
    TextView data2;
    @Bind(R.id.data3)
    TextView data3;
    @Bind(R.id.data4)
    TextView data4;

    private int runningStatus = Constant.RunningStatus.NO_CONNECTION;
    private ArrayList<FileLineItem> mDatas;
    private BaseAdapter mAdaper;

    public PlayFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        L.d("onCreateView --> PlayFragment");

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_play, container, false);
        ButterKnife.bind(this, view);
        initView(view);
        return view;
    }

    private void initView(View view) {
        List<Integer> ids = new ArrayList<>();
        ids.add(R.id.btn_new);
        ids.add(R.id.btn_open);
        ids.add(R.id.btn_save);
        ids.add(R.id.btn_save_as);
        setViewClickListener(ids, view);
        mDatas = new ArrayList<>();
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

    @Override
    public void onClick(View view) {

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
