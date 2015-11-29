package com.remote.controller.fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.remote.controller.constant.Constant;
import com.remote.controller.message.MessageEvent;
import com.remote.controller.network.BroadCastUdpThread;
import com.remote.controller.network.ControllerManager;
import com.remote.controller.network.EventGenerator;
import com.remote.controller.service.SyncService;
import com.remote.controller.utils.L;
import com.remote.controller.utils.SPUtils;

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
    private int mCurrentPos = -1;
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

//        //TODO test code
//        mDatas.add(new Device("192.168.1.1", "device 1", "desc"));
//        mDatas.add(new Device("192.168.1.2", "device 2", "desc"));
//        mDatas.add(new Device("192.168.1.3", "device 3", "desc"));

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
                listView.setSelection(i);
                mCurrentPos = i;
            }
        });


    }

    public void onEventMainThread(final Message msg) {
        int msgEvent = msg.what;
        switch (msgEvent) {
            case MessageEvent.MSG_SCAN_PAIR:
                String ip = (String) msg.obj;
                if (!ip.isEmpty()) {
                    mDatas.add(new Device(ip, "", ""));
                    mAdaper.notifyDataSetChanged();
                }
                break;

            case MessageEvent.MSG_SOCKET_CONNECTED:
                //连接成功回调
                ControllerManager.getInstance(mContext).setConnected(true);
                //请求设备名和描述
                ControllerManager.getInstance(mContext).sendData(EventGenerator.getInstance().generateData(Constant.EventCode.READ_DEVICE_NAME, null));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ControllerManager.getInstance(mContext).sendData(EventGenerator.getInstance().generateData(Constant.EventCode.READ_DEVICE_DESC, null));
                showAlertDialog("已连接");

                //启动后台定时服务
                Intent serviceIntent = new Intent(mActivity, SyncService.class);
                mActivity.startService(serviceIntent);
                break;

            case MessageEvent.MSG_SOCKET_DISCONNECTED:
                //断开连接成功
                ControllerManager.getInstance(mContext).setConnected(false);
                refreshRemoteDeviceInfo();
                showAlertDialog("已断开");

                //停止后台定时服务
                Intent stopServiceIntent = new Intent(mActivity, SyncService.class);
                mActivity.stopService(stopServiceIntent);
                break;

            case MessageEvent.MSG_SOCKET_RECEIVE_DATA:
                //收到服务器回应
                int funcCode = msg.arg1;
                byte[] data = (byte[]) msg.obj;
                if (funcCode == Constant.EventCode.READ_DEVICE_NAME) {
                    connectName.setText(new String(data).trim());
                    connectIp.setText((String) SPUtils.get(mContext, "ip", ""));
                }
                if (funcCode == Constant.EventCode.READ_DEVICE_DESC) {
                    connectDesc.setText(new String(data).trim());
                }

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
        mCurrentPos = -1;
        mDatas.clear();
        mAdaper.notifyDataSetChanged();
//        showLoadingDialog(mContext);

        BroadCastUdpThread udpThread = new BroadCastUdpThread(Constant.ScanText.REQ);
        udpThread.start();
//        ControllerManager.getInstance(mContext).scanDevice();

        //读取设备运行状态
//        ControllerManager.getInstance(mContext).sendData(EventGenerator.getInstance().generateData(Constant.EventCode.READ_DATA_ON_SETTING, null));

//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

    }

    private void connectDevice() {
        if (mCurrentPos < 0) {
            new AlertDialog.Builder(mContext).setTitle(R.string.not_select_hint).setMessage(R.string.not_select_hint)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).show();

            return;
        }

        if (ControllerManager.getInstance(mContext).isConnected()) {
            showAlertDialog("已连接设备，请先断开");
            return;
        }

        Device device = mDatas.get(mCurrentPos);
        if (device != null) {
            ControllerManager.getInstance(mContext).connectServer(device.getIp(), 3000);
        }


//        ControllerManager.getInstance(mContext).connectServer("192.168.1.100", 3000);
    }


    private void refreshRemoteDeviceInfo() {
        connectDesc.setText("");
        connectIp.setText("");
        connectName.setText("");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
