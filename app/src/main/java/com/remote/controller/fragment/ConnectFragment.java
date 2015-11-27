package com.remote.controller.fragment;


import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.remote.controller.R;
import com.remote.controller.adapter.CommonAdapter;
import com.remote.controller.adapter.ViewHolder;
import com.remote.controller.bean.Device;
import com.remote.controller.message.MessageEvent;
import com.remote.controller.network.ControllerManager;
import com.remote.controller.utils.L;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 联机主界面
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
    @Bind(R.id.connect_ip)
    TextView connectIp;
    @Bind(R.id.connect_name)
    TextView connectName;
    @Bind(R.id.connect_desc)
    TextView connectDesc;

    private ArrayList<Device> mDatas;
    private BaseAdapter mAdaper;
    private int mCurrentPos;
    private Device mCurrentDevice = null;

    public ConnectFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        L.d("onCreateView --> ConnectFragment");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_connect, container, false);
        ButterKnife.bind(this, view);

        List<Integer> ids = new ArrayList<>();
        ids.add(R.id.btn_connect);
        ids.add(R.id.btn_scan);
        ids.add(R.id.disconnect);
        setViewClickListener(ids, view);
        mDatas = new ArrayList<>();

        showCurrentConnection();

        refreshList();
        return view;
    }

    private void showCurrentConnection() {
        //TODO
    }

    private void refreshList() {

        //TODO test code
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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });


    }

    public void onEvent(final Message msg) {
        int msgEvent = msg.what;
        switch (msgEvent) {
            case MessageEvent.MSG_SOCKET_CONNECTED:
                //连接成功回调
                ControllerManager.getInstance(mContext).setConnected(true);
                //TODO 更新设备名称和描述
                refreshRemoteDeviceInfo();
                break;

            case MessageEvent.MSG_SOCKET_DISCONNECTED:
                //断开连接成功
                ControllerManager.getInstance(mContext).setConnected(false);
                refreshRemoteDeviceInfo();
                break;

        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_connect:
                connectDevice();
                break;
            case R.id.btn_scan:
                scanDevice();
                break;

            case R.id.disconnect:
                disconnect();
                break;
        }
    }

    private void disconnect() {
        if (!ControllerManager.getInstance(mContext).isConnected()) {
            showAlertDialog("尚未连接任何设备");
        }
        ControllerManager.getInstance(mContext).stop();
    }

    private void scanDevice() {
//        ControllerManager.getInstance(mContext).scanDevice();

        byte[] data = new byte[5];
        data[0] = 0x3;
        data[1] = 0x2;
        data[2] = 0x0;
        data[3] = 0x1;
        data[4] = 0x6;
        ControllerManager.getInstance(mContext).sendData(data);
    }

    private void connectDevice() {
//        if (mCurrentDevice == null || mCurrentPos < 0) {
//            new AlertDialog.Builder(mContext).setTitle(R.string.not_select_hint).setMessage(R.string.not_select_hint)
//                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            dialogInterface.dismiss();
//                        }
//                    }).show();
//        }

        if (ControllerManager.getInstance(mContext).isConnected()) {
            showAlertDialog("已连接设备，请先断开");
            return;
        }

        ControllerManager.getInstance(mContext).connectServer("172.19.6.92", 3000);
    }


    private void refreshRemoteDeviceInfo() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
