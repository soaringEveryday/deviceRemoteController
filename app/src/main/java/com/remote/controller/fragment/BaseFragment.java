package com.remote.controller.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.remote.controller.R;
import com.remote.controller.utils.ScreenUtils;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by Chen Haitao on 2015/8/8.
 */
public abstract class BaseFragment extends Fragment implements View.OnClickListener {

    protected AlertDialog mAlertDialog;
    private ProgressDialog mLoadingDialog;
    public Activity mActivity;
    public Context mContext;
    public FragmentManager mFragMgr;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = getActivity();
        mContext = context;
        mFragMgr = getActivity().getSupportFragmentManager();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this, 1);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
            mLoadingDialog = null;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        mContext = null;
        mFragMgr = null;
        mActivity = null;
    }

    public void setViewClickListener(List<Integer> viewIds, View rootView) {
        for (int viewId : viewIds) {
            rootView.findViewById(viewId).setOnClickListener(this);
        }
    }

    public void showToast(@DrawableRes int drawableResId, @StringRes int StringResId, Context context, int duration) {
        showToast(drawableResId, getString(StringResId), context, duration);
    }

    public void showToast(@DrawableRes int drawableResId, String msgStr, Context context, int duration) {
//        if (toastLayout == null) {
//            LayoutInflater inflater = LayoutInflater.from(context);
//            toastLayout = inflater.inflate(R.layout.custom_toast_layout, null);
//            icon = (ImageView) toastLayout.findViewById(R.id.toast_icon);
//            text = (TextView) toastLayout.findViewById(R.id.toast_content);
//        }
//
//        icon.setImageResource(drawableResId);
//        text.setText(msgStr);
//        if (mToast == null) {
//            mToast = new Toast(context);
//        }
//        mToast.setGravity(Gravity.CENTER, 0, 0);
//        mToast.setDuration(duration);
//        mToast.setView(toastLayout);
//        mToast.show();
    }

    //alert dialog
    public void showAlertDialog(Context context, @StringRes int titleResId, @StringRes int contentResId, boolean isOk, boolean isCancel, View.OnClickListener okClickListener) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_comfirm_alert, null);
        TextView title = (TextView) view.findViewById(R.id.title);
        TextView content = (TextView) view.findViewById(R.id.content);
        if (titleResId != 0) {
            title.setText(titleResId);
        } else {
            title.setVisibility(View.GONE);
        }
        if (contentResId != 0) {
            content.setText(contentResId);
        } else {
            content.setVisibility(View.GONE);
        }
        view.findViewById(R.id.tv_ok).setOnClickListener(okClickListener);
        if (!isOk) {
            view.findViewById(R.id.tv_ok).setVisibility(View.GONE);
        }
        view.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAlertDialog.dismiss();
            }
        });
        if (!isCancel) {
            view.findViewById(R.id.tv_cancel).setVisibility(View.GONE);
        }

        mAlertDialog = new AlertDialog.Builder(getActivity()).create();
        mAlertDialog.setView(view, 0, 0, 0, 0);
        mAlertDialog.setCanceledOnTouchOutside(false);
        mAlertDialog.show();
        WindowManager.LayoutParams params = mAlertDialog.getWindow().getAttributes();
        params.width = ScreenUtils.dip2px(context, 200);
        params.height = ScreenUtils.dip2px(context, 120);
        mAlertDialog.getWindow().setAttributes(params);
    }

    public void showToast(@DrawableRes int drawableResId, @StringRes int StringResId, Context context) {
        showToast(drawableResId, StringResId, context, Toast.LENGTH_SHORT);
    }

    public void showToast(@DrawableRes int drawableResId, String msgStr, Context context) {
        showToast(drawableResId, msgStr, context, Toast.LENGTH_SHORT);
    }

    /**
     * show a normal loading dialog, everywhere on sub class can call this
     */
    public void showLoadingDialog(Context ctx) {
        mLoadingDialog = new ProgressDialog(ctx);
        mLoadingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mLoadingDialog.setMessage(getString(R.string.dialog_common_loading));
        mLoadingDialog.setIndeterminate(true);
        mLoadingDialog.setCancelable(false);
        mLoadingDialog.show();
    }

    /**
     * dismiss loading dialog
     */
    public void dismissLoadingDialog() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
            mLoadingDialog = null;
        }
    }

    public void showAlertDialog(Context context,View view) {
        mAlertDialog = new AlertDialog.Builder(context).create();
        mAlertDialog.setView(view, 0, 0, 0, 0);
        mAlertDialog.setCanceledOnTouchOutside(false);
        mAlertDialog.show();
        WindowManager.LayoutParams params = mAlertDialog.getWindow().getAttributes();
        params.width = ScreenUtils.dip2px(context, 400);
        params.height = ScreenUtils.dip2px(context, 220);
        mAlertDialog.getWindow().setAttributes(params);
    }

    public void showAlertDialog(String content) {
        new AlertDialog.Builder(mContext).setTitle(content).setMessage(content)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();
    }
}
