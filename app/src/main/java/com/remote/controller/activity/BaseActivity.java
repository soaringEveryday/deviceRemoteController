package com.remote.controller.activity;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.remote.controller.CustomApplication;
import com.remote.controller.R;
import com.remote.controller.utils.ScreenUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Chen Haitao on 2015/7/6.
 */
public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener  {

    private CommonReceiver mCommonReceiver = null;
    private boolean mNetworkOK = false;
    private ProgressDialog mLoadingDialog;
    protected AlertDialog mAlertDialog;
    private Context mContext;

    @Nullable
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Nullable
    @Bind(R.id.toolbar_title)
    TextView mToolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        mNetworkOK = isNetworkConnected();
        register();
        mContext = this;

        CustomApplication.getInstance().addActivity(this);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
        setupToolbar();
        String title = getTitle().toString();
        if (mToolbarTitle != null) {
            mToolbarTitle.setText(title);
        }
        setTitle("");
    }

    protected void setupToolbar() {
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.drawable.toolbar_back);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            ActionBar ab = getActionBar();
            if (ab != null) {
                ab.setDisplayHomeAsUpEnabled(true);
            }

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAlertDialog != null) {
            mAlertDialog.dismiss();
            mAlertDialog = null;
        }
        unregister();
        destroyResources();
        ButterKnife.unbind(this);
        mContext = null;
        CustomApplication.getInstance().removeActivity(this);
    }

    public void setViewClickListener(List<Integer> viewIds, View rootView) {
        for (int viewId : viewIds) {
            rootView.findViewById(viewId).setOnClickListener(this);
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
        }
        return super.onOptionsItemSelected(item);
    }

    private void destroyResources() {


        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
            mLoadingDialog = null;
        }
    }

    private class CommonReceiver extends BroadcastReceiver {
        boolean success = false;

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = null;
            if (intent != null) {
                action = intent.getAction();
            }

            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                if (context != null) {
                    mNetworkOK = isNetworkConnected();
                    if (!mNetworkOK) {
//                        Snackbar.make(getWindow().getDecorView(), context.getResources().getString(R.string.netConnectedError), Snackbar.LENGTH_LONG).show();
                    }
                }
            }
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) this
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
        return mNetworkInfo != null;
    }

    private void register() {
        mCommonReceiver = new CommonReceiver();
        // Registry network monitoring
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        // filter.addAction(Constant.ACTION_FINISHI_ACTION);
        registerReceiver(mCommonReceiver, filter);
    }

    private void unregister() {
        unregisterReceiver(mCommonReceiver);
    }


    /**
     * exit all application
     * all activities will finished itself one by one
     *
     * @param context
     */
    public void exitApplication(@NonNull Context context) {
        showAlertDialog(context, R.string.dialog_exit_confirm_content, 0, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAlertDialog.dismiss();
                mAlertDialog = null;
                CustomApplication application = (CustomApplication) getApplication();
                application.exit();
            }
        });
    }


    /* overview dialog */

    /**
     * show a normal loading dialog, everywhere on sub class can call this
     */
    public void showLoadingDialog() {
        mLoadingDialog = new ProgressDialog(this);
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

    /* utils */

    /**
     * show a SnackBar message(instead of old Toast)
     *
     * @param view docking view
     * @param msg
     */
    public void showSnackMsg(@NonNull View view, String msg) {
        if (view != null) {
            Snackbar.make(view, msg, Snackbar.LENGTH_LONG).show();
        } else {
            Snackbar.make(getWindow().getDecorView(), msg, Snackbar.LENGTH_LONG).show();
        }
    }


    /**
     * show a Snackbar message
     *
     * @param msg
     */
    public void showSnackMsg(@NonNull String msg) {
        showSnackMsg(getWindow().getDecorView(), msg);
    }

    /**
     * show a Snackbar message by a string id
     *
     * @param resId
     */
    public void showSnackMsg(@StringRes int resId) {
        showSnackMsg(getWindow().getDecorView(), getResources().getString(resId));
    }

    /**
     * call the to close opening software keyboard
     * after click a button, some condition would hide the software keyboard to keep better UE
     */
    public void hideSoftKeyboard() {
        View focusingView = this.getCurrentFocus();
        if (focusingView != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(focusingView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    public boolean isConnectNetwork() {
        boolean flag = Boolean.FALSE;
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            return flag;
        }
        if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
            flag = Boolean.TRUE;
        }
        return flag;
    }

    public void showToast(@DrawableRes int drawableResId, @StringRes int StringResId) {
        showToast(drawableResId, getString(StringResId));
    }

    public void showToast(@DrawableRes int drawableResId,String str) {
//        LayoutInflater inflater = getLayoutInflater();
//        View layout = inflater.inflate(R.layout.custom_toast_layout, (ViewGroup) findViewById(R.id.rl_toast));
//        ImageView icon = (ImageView) layout.findViewById(R.id.toast_icon);
//        TextView text = (TextView) layout.findViewById(R.id.toast_content);
//        icon.setImageResource(drawableResId);
//        text.setText(str);
//        Toast toast = new Toast(getApplicationContext());
//        toast.setGravity(Gravity.CENTER, 0, 0);
//        toast.setDuration(Toast.LENGTH_SHORT);
//        toast.setView(layout);
//        toast.show();
    }

    //alert dialog
    public void showAlertDialog(Context context, @StringRes int titleResId, @StringRes int contentResId, View.OnClickListener okClickListener) {
        View view = getLayoutInflater().inflate(R.layout.dialog_comfirm_alert, null);
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
        view.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAlertDialog.dismiss();
            }
        });
        mAlertDialog = new AlertDialog.Builder(this).create();
        mAlertDialog.setView(view, 0, 0, 0, 0);
        mAlertDialog.setCanceledOnTouchOutside(false);
        mAlertDialog.show();
        WindowManager.LayoutParams params = mAlertDialog.getWindow().getAttributes();
        params.width = ScreenUtils.dip2px(context, 400);
        params.height = ScreenUtils.dip2px(context, 230);
        mAlertDialog.getWindow().setAttributes(params);
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
}
