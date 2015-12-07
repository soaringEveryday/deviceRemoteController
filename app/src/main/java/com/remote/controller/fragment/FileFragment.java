package com.remote.controller.fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.remote.controller.R;
import com.remote.controller.adapter.CommonAdapter;
import com.remote.controller.adapter.ViewHolder;
import com.remote.controller.bean.FileLineItem;
import com.remote.controller.constant.Constant;
import com.remote.controller.filechooser.ipaulpro.afilechooser.utils.FileUtils;
import com.remote.controller.message.MessageEvent;
import com.remote.controller.utils.CSVUtils;
import com.remote.controller.utils.L;
import com.remote.controller.utils.SPUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * 文件主界面
 */
public class FileFragment extends BaseFragment {


    @Bind(R.id.btn_new)
    Button btnNew;
    @Bind(R.id.btn_open)
    Button btnOpen;
    @Bind(R.id.btn_save)
    Button btnSave;
    @Bind(R.id.btn_save_as)
    Button btnSaveAs;
    @Bind(R.id.list)
    ListView listView;
    @Bind(R.id.file_desc)
    TextView fileDesc;
    @Bind(R.id.file_name)
    TextView fileName;

    private ArrayList<FileLineItem> mDatas;
    private BaseAdapter mAdapter;
    private static final int REQUEST_CHOOSER = 1234;
    private boolean isOpenOld = false;
    private String currentFileName = "";
    private String currentFileDesc = "";
    ArrayList<String> columns = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatas = new ArrayList<>();
        mAdapter = new CommonAdapter<FileLineItem>(mContext, mDatas, R.layout.file_list_item) {
            @Override
            public void convert(ViewHolder helper, FileLineItem item, int position, View convertView) {
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
        L.d("onCreateView --> FileFragment");

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_file, container, false);
        ButterKnife.bind(this, view);

        List<Integer> ids = new ArrayList<>();
        ids.add(R.id.btn_new);
        ids.add(R.id.btn_open);
        ids.add(R.id.btn_save);
        ids.add(R.id.btn_save_as);
        setViewClickListener(ids, view);

        initListView();

        columns.add(getString(R.string.file_list_no));
        columns.add(getString(R.string.file_list_cmd));
        columns.add(getString(R.string.file_list_param));
        columns.add(getString(R.string.file_list_memo));

        newFile(false);

        return view;
    }

    private void initListView() {
        listView.setAdapter(mAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public FileFragment() {
        // Required empty public constructor
    }


    private void openFile() {
        Intent getContentIntent = FileUtils.createGetContentIntent();
        Intent intent = Intent.createChooser(getContentIntent, "Select a file");
        startActivityForResult(intent, REQUEST_CHOOSER);
    }

    private void saveFile() {
        int cmdSize = mDatas.size();
        if (cmdSize <= 0) {
            Toast.makeText(mActivity, "请添加一些指令后再保存", Toast.LENGTH_SHORT).show();
            return;
        }

        if (isOpenOld) {
            //保存当前打开的文件

            if (saveCsvFile() < 0) {
                Toast.makeText(mActivity, "保存失败", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mActivity, "保存成功", Toast.LENGTH_SHORT).show();
                Message m = Message.obtain();
                m.what = MessageEvent.MSG_FILE_SAVED;
                EventBus.getDefault().postSticky(m);
            }

        } else {
            //表明示新创建的文件
            showCreateDialog();
        }
    }

    private void newFile(boolean showHint) {

        //清空数据，新建文件仅仅创建一个新的指令集
        if (mDatas == null) {
            mDatas = new ArrayList<>();
        } else {
            mDatas.clear();
        }
        mAdapter.notifyDataSetChanged();

        currentFileName = "";
        currentFileDesc = "";

        isOpenOld = false;
        if (showHint)
            Toast.makeText(mActivity, "新建成功", Toast.LENGTH_SHORT).show();

        Message msg = Message.obtain();
        msg.what = MessageEvent.MSG_COMMAND_CLEAR;
        EventBus.getDefault().post(msg);

        refreshFileInfo();
    }


    private void saveAsFile() {
//        insertCommand();
    }

    private void showCreateDialog() {
        FragmentTransaction ft = mFragMgr.beginTransaction();
        Fragment fragment = mFragMgr.findFragmentByTag("dialog_duplicate");
        if (null != fragment) {
            ft.remove(fragment);
            ft.commit();
        }

        CreateNewFileFragment dialogDuplicate = new CreateNewFileFragment();
        dialogDuplicate.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        dialogDuplicate.show(mFragMgr, "create_new_file");
    }


    private void refreshFileInfo() {
        fileDesc.setText(currentFileDesc);
        fileName.setText(currentFileName);
    }

    public void onEvent(final Message msg) {
        int msgEvent = msg.what;
        switch (msgEvent) {
            case MessageEvent.MSG_CREATE_NEW_FILE_SUCCESS:
                L.v("recv new file info, and then will to create csv file");
                String fileStr = (String) msg.obj;
                if (fileStr == null) {
                    L.e("fileStr is null");
                    return;
                }
                String[] strs = fileStr.split("&");
                if (createCsvFile(strs[0], strs[1]) < 0) {
                    Toast.makeText(mActivity, "保存文件失败", Toast.LENGTH_SHORT).show();
                    currentFileName = strs[0];
                    currentFileDesc = strs[1];
                    refreshFileInfo();
                } else {
                    Toast.makeText(mActivity, "保存文件成功", Toast.LENGTH_SHORT).show();
                    Message m = Message.obtain();
                    m.what = MessageEvent.MSG_FILE_SAVED;
                    EventBus.getDefault().postSticky(m);
                }
                break;

            case MessageEvent.MSG_COMMAND_UPDATE:
                FileLineItem item = (FileLineItem) msg.obj;
                mDatas.add(item);
                mAdapter.notifyDataSetChanged();
                break;

            case MessageEvent.MSG_COMMAND_DELETE:
                int pos = msg.arg1;
                mDatas.remove(pos);
                mAdapter.notifyDataSetChanged();
                break;
        }
    }

    /**
     * 创建新文件
     * @param fileName
     * @param desc
     * @return
     */
    private int createCsvFile(String fileName, String desc) {
        L.d("文件名：" + fileName);
        L.d("文件描述：" + desc);

        try {
            CSVUtils.getInstance().create(fileName, Constant.FileFormat.VERION_CSV, desc, columns, mDatas);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }

        return 0;
    }

    private int saveCsvFile() {
        try {
            CSVUtils.getInstance().save((String) SPUtils.get(mContext, Constant.SPKEY.FILE_PATH, ""), Constant.FileFormat.VERION_CSV, currentFileDesc, columns, mDatas);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }

        return 0;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_new:
                newFile(true);
                break;

            case R.id.btn_open:
                openFile();
                break;

            case R.id.btn_save:
                saveFile();
                break;

            case R.id.btn_save_as:
                saveAsFile();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            //读取文件返回
            case REQUEST_CHOOSER:
                if (data == null) {
                    return;
                }
                try {
                    final Uri uri = data.getData();
                    // Get the File path from the Uri
                    String path = FileUtils.getPath(mActivity, uri);
                    // Alternatively, use FileUtils.getFile(Context, Uri)
                    if (path != null && FileUtils.isLocal(path)) {
                        L.d("path :" + path);
                        ArrayList<FileLineItem> temp = CSVUtils.getInstance().read(mContext, path);
                        if (temp != null) {
                            L.d("打开文件" + path + "成功");
                            Toast.makeText(mActivity, "打开文件" + path + "成功", Toast.LENGTH_SHORT).show();
                            SPUtils.put(mActivity, Constant.SPKEY.FILE_PATH, path);
                            mDatas.clear();

                            //发送数据到别的fragment
                            Message msg = Message.obtain();
                            msg.what = MessageEvent.MSG_COMMAND_UPDATE;

                            for (FileLineItem item : temp) {
                                msg.obj = item;
                                EventBus.getDefault().post(msg);
                            }
                            isOpenOld = true;
                            currentFileName = (String) SPUtils.get(mContext, Constant.SPKEY.FILE_NAME, "");
                            currentFileDesc = (String) SPUtils.get(mContext, Constant.SPKEY.FILE_DESC, "");

                            refreshFileInfo();
                        } else {
                            Toast.makeText(mActivity, "打开文件" + path + "失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(mActivity, "文件格式错误", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

}
