package com.remote.controller.fragment;

import android.support.v4.app.DialogFragment;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by Chen Haitao on 2015/8/8.
 */
public abstract class BaseDialogFragment extends DialogFragment implements View.OnClickListener{

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void setViewClickListener(ArrayList<Integer> viewIds, View rootView) {
        for (int viewId : viewIds) {
            rootView.findViewById(viewId).setOnClickListener(this);
        }
    }

}
