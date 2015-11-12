package com.qianmi.epos.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.qianmi.epos.CustomApplication;
import com.qianmi.epos.R;

import java.util.ArrayList;

/**
 * Created by Chen Haitao on 2015/7/9.
 */
public class ViewUtils {

    public static void setListViewHeightMeasureOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    public static void setTypeFaceList(@NonNull ArrayList<Integer> viewIds, String typeFaceName, Context context, View dockingView) {
        Typeface typeFace = Typeface.createFromAsset(context.getAssets(), typeFaceName);
        for (int viewId : viewIds) {
            TextView textView = (TextView) dockingView.findViewById(viewId);
            textView.setTypeface(typeFace);
        }
    }

    public static void setTypeFace(@NonNull TextView view, String typeFaceName, Context context) {
        Typeface typeFace = Typeface.createFromAsset(context.getAssets(), typeFaceName);
        view.setTypeface(typeFace);
    }


    /**
     * 浮点数;
     *
     * @param editText
     */
    public static void setDigitalNumber(EditText editText) {
        String digits = CustomApplication.getInstance().getString(R.string.digital_number_point);
        editText.setKeyListener(DigitsKeyListener.getInstance(digits));
    }

    /**
     * 整数;
     *
     * @param editText
     */
    public static void setDigital(EditText editText) {
        String digits = CustomApplication.getInstance().getString(R.string.digital_number);
        editText.setKeyListener(DigitsKeyListener.getInstance(digits));
    }
}
