package com.remote.controller.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.remote.controller.R;
import com.remote.controller.utils.ScreenUtils;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CreateNewFileFragment extends BaseDialogFragment {


    @Bind(R.id.et_version)
    EditText etVersion;
    @Bind(R.id.et_desc)
    EditText etDesc;
    @Bind(R.id.btn_ok)
    Button btnOk;
    @Bind(R.id.btn_cancel)
    Button btnCancel;

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

//        Bundle args = getArguments();
//
//        if (args != null) {
//            String goodsStr = args.getString(ARG_GOODS_LIST);
//            Gson gson = new Gson();
//            mDataList = gson.fromJson(goodsStr, new TypeToken<List<GoodsUnitRel>>(){}.getType());
//            CashierDuplicateListAdapter adapter = new CashierDuplicateListAdapter(getActivity(), mDataList);
//            mList.setAdapter(adapter);
//            mCode.setText(getString(R.string.duplicate_code) + " " + args.getString(ARG_GOOD_CODE));
//        }
//        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                GoodsUnitRel good = mDataList.get(position);
//                Message msg = Message.obtain();
//                msg.what = MessageEvent.MSG_DUPLICATE_SCAN_GOODS_SELECTED;
//                msg.obj = good;
//                EventBus.getDefault().post(msg);
//                dismiss();
//            }
//        });

        viewIds.add(R.id.btn_ok);
        viewIds.add(R.id.btn_cancel);
        setViewClickListener(viewIds, view);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_ok:
                this.dismiss();
                break;

            case R.id.btn_cancel:
                this.dismiss();
                break;
        }
    }
}
