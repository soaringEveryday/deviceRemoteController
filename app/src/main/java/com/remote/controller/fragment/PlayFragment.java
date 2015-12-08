package com.remote.controller.fragment;


import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import com.remote.controller.network.ControllerManager;
import com.remote.controller.network.EventGenerator;
import com.remote.controller.utils.L;
import com.remote.controller.utils.SPUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
    private CommonAdapter mAdapter;
    private boolean isNewFile = true;

    public PlayFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatas = new ArrayList<>();
        mAdapter = new CommonAdapter<FileLineItem>(mContext, mDatas, R.layout.file_list_item) {
            @Override
            public void convert(ViewHolder helper, FileLineItem item, int position, View convertView) {
                if (convertView != null) {
                    if (mCurrentItemPos == position) {
                        convertView.setBackgroundResource(R.drawable.shape_solid_gray);
                    } else {
                        convertView.setBackgroundResource(R.drawable.shape_solid_white);

                    }
                }
                helper.setText(R.id.no, String.valueOf(position + 1));
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
        updateRunningStatus(Constant.RunningStatus.NO_CONNECTION);
        return view;
    }

    private void initListView() {
        list.setAdapter(mAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mAdapter.setCurrentItemPos(i);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    public void onEventMainThread(final Message msg) {
        L.d("play onEvent");
        int msgEvent = msg.what;
        L.d("what:" + msgEvent);
        switch (msgEvent) {
            case MessageEvent.MSG_COMMAND_UPDATE:
                FileLineItem item = (FileLineItem) msg.obj;
                mDatas.add(item);
                mAdapter.notifyDataSetChanged();
                isNewFile = true;
                break;

            case MessageEvent.MSG_COMMAND_DELETE:
                int pos = msg.arg1;
                mDatas.remove(pos);
                mAdapter.notifyDataSetChanged();
                break;

            case MessageEvent.MSG_COMMAND_CLEAR:
                mDatas.clear();
                mAdapter.notifyDataSetChanged();
                isNewFile = true;
                break;

            case MessageEvent.MSG_FILE_SAVED:
                isNewFile = false;
                break;

            case MessageEvent.MSG_SOCKET_CONNECTED:
                updateRunningStatus(Constant.RunningStatus.IDLE);
                break;

            case MessageEvent.MSG_SOCKET_DISCONNECTED:
                //断开连接成功
                tvRunningStatus.setText("未连接");
                data1.setText("null");
                data2.setText("null");
                data3.setText("null");
                data4.setText("null");
                updateRunningStatus(Constant.RunningStatus.NO_CONNECTION);
                break;

            case MessageEvent.MSG_SOCKET_RECEIVE_DATA:
                //收到服务器回应
                int funcCode = msg.arg1;
                byte[] data = (byte[]) msg.obj;
                if (funcCode == Constant.EventCode.READ_RUNNING_STATE) {
                    int runningState = byte2int(data[0]);
                    updateRunningStatus(runningState);
                } else if (funcCode == Constant.EventCode.READ_DATA_ON_PLAY) {
                    data1.setText(String.valueOf(byte2int(Arrays.copyOfRange(data, 0, 4))));
                    data2.setText(String.valueOf(byte2int(Arrays.copyOfRange(data, 4, 8))));
                    data3.setText(String.valueOf(byte2int(Arrays.copyOfRange(data, 8, 12))));
                    data4.setText(String.valueOf(byte2int(Arrays.copyOfRange(data, 12, 16))));
                } else if (funcCode == Constant.EventCode.READ_PLAY_TIMES) {
                    tvFinishedTimes.setText(String.valueOf(byte2int(data)));
                }
                break;
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_launch:

                //发送文件数据包
                String path = (String) SPUtils.get(mContext, Constant.SPKEY.FILE_PATH, "");
                if (path == null || path.isEmpty()) {
                    L.e("file path is empty");
                    showAlertDialog("尚未保存文件");
                    return;
                }

                File tempFile = new File(path);
                if (tempFile != null) {
                    byte data[] = getBytesFromFile(tempFile);
                    if (data == null) {
                        showAlertDialog("文件数据读取错误");
                        return;
                    }
                    L.i("file data length : " + data.length);
                    ControllerManager.getInstance(mContext).sendData(EventGenerator.getInstance().generateFile(data));

                    L.i("sending launch command");
                    sendLaunchCommand();

                } else {
                    L.e("create temp file fail");
                }

                break;
            case R.id.btn_pause:
                ControllerManager.getInstance(mContext).sendData(EventGenerator.getInstance().generateData(Constant.EventCode.BTN_PLAY_PAUSE, null));

            case R.id.btn_stop:
                ControllerManager.getInstance(mContext).sendData(EventGenerator.getInstance().generateData(Constant.EventCode.BTN_PLAY_STOP, null));

                break;
            case R.id.btn_reset:
                ControllerManager.getInstance(mContext).sendData(EventGenerator.getInstance().generateData(Constant.EventCode.BTN_PLAY_RESET, null));

                break;
        }
    }

    private void sendLaunchCommand() {
        String str = etTimesRunTotal.getText().toString();
        if (str.isEmpty()) {
            showAlertDialog("请输入运行次数");
            return;
        }
        if (isNewFile) {
            showAlertDialog("请先到文件界面保存文件");
            return;
        }

        int runTimes = Integer.parseInt(str);
        L.d("请求运行" + runTimes + "次");
        ControllerManager.getInstance(mContext).sendData(EventGenerator.getInstance().generateData(Constant.EventCode.BTN_PLAY_LAUNCH, runTimes));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    //用event bus通知运行状态，更新其他界面的按钮有效性
    private void updateRunningStatus(int state) {
        runningStatus = state;
        switch (state) {
            case Constant.RunningStatus.NO_CONNECTION:
                btnLaunch.setEnabled(false);
                btnPause.setEnabled(false);
                btnStop.setEnabled(false);
                btnReset.setEnabled(true);
                tvRunningStatus.setText("未连接");
                break;
            case Constant.RunningStatus.ERROR:
                btnLaunch.setEnabled(false);
                btnPause.setEnabled(false);
                btnStop.setEnabled(false);
                btnReset.setEnabled(true);
                tvRunningStatus.setText("出错");
                break;
            case Constant.RunningStatus.IDLE:
                btnLaunch.setEnabled(true);
                btnPause.setEnabled(false);
                btnStop.setEnabled(false);
                btnReset.setEnabled(true);
                tvRunningStatus.setText("空闲");
                break;
            case Constant.RunningStatus.RUNNING:
                btnLaunch.setEnabled(false);
                btnPause.setEnabled(true);
                btnStop.setEnabled(true);
                btnReset.setEnabled(false);
                tvRunningStatus.setText("运行中");
                break;
        }
    }

    private int byte2int(byte b) {
        return  b & 0xff;
    }

    private int byte2int(byte[] res) {
        // 一个byte数据左移24位变成0x??000000，再右移8位变成0x00??0000

        int targets = (res[0] & 0xff) | ((res[1] << 8) & 0xff00) // | 表示安位或
                | ((res[2] << 24) >>> 8) | (res[3] << 24);
        return targets;
    }

    private static byte[] getBytesFromFile(File file) {
        byte[] ret = null;
        try {
            if (file == null) {
                // log.error("helper:the file is null!");
                return null;
            }
            FileInputStream in = new FileInputStream(file);
            ByteArrayOutputStream out = new ByteArrayOutputStream(4096);
            byte[] b = new byte[4096];
            int n;
            while ((n = in.read(b)) != -1) {
                out.write(b, 0, n);
            }
            in.close();
            out.close();
            ret = out.toByteArray();
        } catch (IOException e) {
            // log.error("helper:get bytes from file process error!");
            e.printStackTrace();
        }
        return ret;
    }
}
