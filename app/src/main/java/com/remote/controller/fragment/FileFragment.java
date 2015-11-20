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

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

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
    private BaseAdapter mAdaper;
    private static final int REQUEST_CHOOSER = 1234;
    private boolean isOpenOld = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        return view;
    }

    private void initListView() {
        mDatas = new ArrayList<>();
        listView.setAdapter(mAdaper = new CommonAdapter<FileLineItem>(mContext, mDatas, R.layout.file_list_item) {
            @Override
            public void convert(ViewHolder helper, FileLineItem item, int position) {
                helper.setText(R.id.no, String.valueOf(item.getNo()));
                helper.setText(R.id.command, item.getCommand());
                helper.setText(R.id.parameter, item.getParameter());
                helper.setText(R.id.memo, item.getMemo());
            }
        });
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

    }

    private void saveFile() {
        int cmdSize = mDatas.size();
        if (cmdSize <= 0) {
            Toast.makeText(mActivity, "请添加一些指令后再保存", Toast.LENGTH_SHORT).show();
            return;
        }

        if (isOpenOld) {
            //保存当前打开的文件
        } else {
            //表明示新创建的文件
            showCreateDialog();
        }
    }

    private void newFile() {

        //清空数据，新建文件仅仅创建一个新的指令集
        if (mDatas == null) {
            mDatas = new ArrayList<>();
        } else {
            mDatas.clear();
        }
        mAdaper.notifyDataSetChanged();

        Toast.makeText(mActivity, "新建成功", Toast.LENGTH_SHORT).show();

        refreshFileInfo();
//        try {
//            CSVUtils.getInstance().create();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//

//        Intent getContentIntent = FileUtils.createGetContentIntent();
//        Intent intent = Intent.createChooser(getContentIntent, "Select a file");
//        startActivityForResult(intent, REQUEST_CHOOSER);

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

    private void saveAsFile() {
        insertCommand();
    }

    private void refreshFileInfo() {
//        CmdFile file = CmdFile.findById(CmdFile.class, 1);
//        if (file != null) {
//            fileDesc.setText(file.getDescription());
//        } else {
//            L.d("文件不存在");
//            Toast.makeText(mActivity, "文件不存在", Toast.LENGTH_SHORT).show();
//        }
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
                } else {
                    Toast.makeText(mActivity, "保存文件成功", Toast.LENGTH_SHORT).show();

                }
                break;
        }
    }

    private int createCsvFile(String fileName, String desc) {
        L.d("文件名：" + fileName);
        L.d("文件描述：" + desc);
        ArrayList<String> columns = new ArrayList<>();
        columns.add(getString(R.string.file_list_no));
        columns.add(getString(R.string.file_list_cmd));
        columns.add(getString(R.string.file_list_param));
        columns.add(getString(R.string.file_list_memo));

        try {
            CSVUtils.getInstance().create(fileName, Constant.FileFormat.VERION_CSV, desc, columns, mDatas);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }

        return 0;

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
            case R.id.btn_new:
                newFile();
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
            case REQUEST_CHOOSER:
//                if (resultCode == RESULT_OK) {
                final Uri uri = data.getData();
                // Get the File path from the Uri
                String path = FileUtils.getPath(mActivity, uri);
                // Alternatively, use FileUtils.getFile(Context, Uri)
                if (path != null && FileUtils.isLocal(path)) {
                    L.d("path :" + path);
//                        File file = new File(path);
                }
//                }
                break;
        }
    }

}
