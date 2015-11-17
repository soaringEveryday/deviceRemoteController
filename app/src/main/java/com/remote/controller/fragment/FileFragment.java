package com.remote.controller.fragment;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.remote.controller.R;
import com.remote.controller.bean.FileLineItem;
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
    ListView list;

    private ArrayList<FileLineItem> mDatas;
    private BaseAdapter mAdaper;


    public FileFragment() {
        // Required empty public constructor
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
        mDatas = new ArrayList<>();
        return view;
    }


    private void openFile() {
    }

    private void saveFile() {

    }

    private void newFile() {
//        try {
//            CSVUtils.getInstance().create();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        showCreateDialog();
    }

    private void showCreateDialog() {
            FragmentTransaction ft = mFragMgr.beginTransaction();
            Fragment fragment = mFragMgr.findFragmentByTag("dialog_duplicate");
            if (null != fragment) {
                ft.remove(fragment);
                ft.commit();
            }

            CreateNewFileFragment dialogDuplicate = new CreateNewFileFragment();
//            Bundle args = new Bundle();
//            args.putString(CashierDialogDuplicateFragment.ARG_GOOD_CODE, code);
//            Gson gson = new Gson();
//            args.putString(CashierDialogDuplicateFragment.ARG_GOODS_LIST, gson.toJson(dataList));
//            dialogDuplicate.setArguments(args);
            dialogDuplicate.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
            dialogDuplicate.show(mFragMgr, "create_new_file");
    }

    private void saveAsFile() {

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
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
