package com.remote.controller.fragment;


import android.content.Intent;
import android.os.Bundle;
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
import com.remote.controller.bean.FileLineItem;
import com.remote.controller.utils.L;

import java.util.ArrayList;
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
    @Bind(R.id.data1)
    TextView data1;
    @Bind(R.id.data2)
    TextView data2;
    @Bind(R.id.data3)
    TextView data3;
    @Bind(R.id.data4)
    TextView data4;
    @Bind(R.id.list)
    ListView list;

    private ArrayList<FileLineItem> mDatas;
    private BaseAdapter mAdaper;

    public SettingFragment() {
        // Required empty public constructor
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
        mDatas = new ArrayList<>();
        return view;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_motion:
                startActivity(new Intent(mContext, MotionActivity.class));
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
}
