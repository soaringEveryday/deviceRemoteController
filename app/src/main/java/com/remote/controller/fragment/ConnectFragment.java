package com.remote.controller.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.remote.controller.R;
import com.remote.controller.adapter.CommonAdapter;
import com.remote.controller.adapter.ViewHolder;
import com.remote.controller.bean.Device;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConnectFragment extends BaseFragment {


    @Bind(R.id.btn_scan)
    Button btnScan;
    @Bind(R.id.btn_connect)
    Button btnConnect;
    @Bind(R.id.disconnect)
    Button disconnect;
    @Bind(R.id.btns)
    LinearLayout btns;
    @Bind(R.id.list)
    ListView listView;

    private ArrayList<Device> mDatas;
    private BaseAdapter mAdaper;

    public ConnectFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_connect, container, false);
        ButterKnife.bind(this, view);

        List<Integer> ids = new ArrayList<>();
        ids.add(R.id.btn_connect);
        ids.add(R.id.btn_scan);
        ids.add(R.id.disconnect);
        setViewClickListener(ids, view);
        mDatas = new ArrayList<>();

        refreshList();
        return view;
    }

    private void refreshList() {

        mDatas.add(new Device("192.168.1.1", "device 1", "desc"));
        mDatas.add(new Device("192.168.1.2", "device 2", "desc"));
        mDatas.add(new Device("192.168.1.3", "device 3", "desc"));

        listView.setAdapter(mAdaper = new CommonAdapter<Device>(mContext, mDatas, R.layout.connect_list) {
            @Override
            public void convert(ViewHolder helper, Device item, int position) {
                //normal
                helper.setText(R.id.no, String.valueOf(position));
                helper.setText(R.id.ip, mDatas.get(position).getIp());
                helper.setText(R.id.name, mDatas.get(position).getDeviceName());
                helper.setText(R.id.desc, mDatas.get(position).getDesc());
            }
        });


    }


    @Override
    public void onClick(View view) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
