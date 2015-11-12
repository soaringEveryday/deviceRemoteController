package com.qianmi.epos.activity;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.qianmi.epos.R;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Chen Haitao on 2015/7/7.
 */
public class LoginActivity extends BaseActivity {

    @Bind(R.id.et_login_username)
    EditText mInputUsername;

    @Bind(R.id.et_login_pwd)
    EditText mInputPwd;

    @Bind(R.id.btn_login)
    Button mLoginBtn;

    @Bind(R.id.til_username_wrapper)
    TextInputLayout mWrapperUsername;

    @Bind(R.id.til_pwd_wrapper)
    TextInputLayout mWrapperPwd;

    @OnClick(R.id.btn_login)
    public void tryLogin(View view) {
    }

    @Override
    public boolean needInitRequestQueue() {
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mWrapperUsername.setErrorEnabled(true);
        mWrapperPwd.setErrorEnabled(true);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            super.exitApplication(LoginActivity.this);
        }
        return true;
    }

    @Override
    public void onClick(View v) {

    }
}
