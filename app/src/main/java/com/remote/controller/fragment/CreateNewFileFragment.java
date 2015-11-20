package com.remote.controller.fragment;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.remote.controller.R;
import com.remote.controller.message.MessageEvent;
import com.remote.controller.utils.ScreenUtils;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

public class CreateNewFileFragment extends BaseDialogFragment {


    @Bind(R.id.et_desc)
    EditText etDesc;
    @Bind(R.id.btn_ok)
    Button btnOk;
    @Bind(R.id.btn_cancel)
    Button btnCancel;
    @Bind(R.id.et_file_name)
    EditText etFileName;

    private ArrayList<Integer> viewIds = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_create_file, container, false);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        ButterKnife.bind(this, view);
        initViewData(view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() == null) {
            return;
        }

        int height = ScreenUtils.getScreenHeight(getActivity());
        int width = ScreenUtils.getScreenWidth(getActivity());
        getDialog().getWindow().setLayout(width, height / 2);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private void initViewData(View view) {

        viewIds.add(R.id.btn_ok);
        viewIds.add(R.id.btn_cancel);
        setViewClickListener(viewIds, view);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_ok:
                sendOutFileInfo();
                this.dismiss();
                break;

            case R.id.btn_cancel:
                this.dismiss();
                break;
        }
    }

    private void sendOutFileInfo() {
        String description = etDesc.getText().toString();
        String fileName = etFileName.getText().toString();
        String sendStr = fileName + "&" + description;

        Message msg = Message.obtain();
        msg.what = MessageEvent.MSG_CREATE_NEW_FILE_SUCCESS;
        msg.obj = sendStr;
        EventBus.getDefault().post(msg);
    }
}
