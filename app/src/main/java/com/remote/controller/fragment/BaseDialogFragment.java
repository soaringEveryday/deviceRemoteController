package com.remote.controller.fragment;

import android.support.v4.app.DialogFragment;
import android.view.View;

import com.remote.controller.CustomApplication;
import com.squareup.leakcanary.RefWatcher;

import java.util.ArrayList;

/**
 * Created by Chen Haitao on 2015/8/8.
 */
public abstract class BaseDialogFragment extends DialogFragment implements View.OnClickListener{

    @Override
    public void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = CustomApplication.getRefWatcher(getActivity());
        if (refWatcher != null) {
            refWatcher.watch(this);
        }
    }

    public void setViewClickListener(ArrayList<Integer> viewIds, View rootView) {
        for (int viewId : viewIds) {
            rootView.findViewById(viewId).setOnClickListener(this);
        }
    }

}
