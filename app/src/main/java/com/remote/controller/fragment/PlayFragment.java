package com.remote.controller.fragment;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.remote.controller.R;
import com.remote.controller.utils.L;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlayFragment extends BaseFragment {


    public PlayFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        L.d("onCreateView --> PlayFragment");

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_play, container, false);
    }


    @Override
    public void onClick(View view) {

    }
}
