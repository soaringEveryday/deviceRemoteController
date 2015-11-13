package com.remote.controller.widget;

/**
 * Created by Chen Haitao on 2015/7/7.
 */

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

public class ClearEditText extends EditText{
    private Drawable dRight;
    private Rect rBounds;

    public ClearEditText(Context paramContext) {
        super(paramContext);
        initEditText();
    }

    public ClearEditText(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        initEditText();
    }

    public ClearEditText(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
        super(paramContext, paramAttributeSet, paramInt);
        initEditText();
    }

    private void initEditText() {
        setEditTextDrawable();
        addTextChangedListener(new TextWatcher() { //listen the input content
            @Override
            public void afterTextChanged(Editable paramEditable) {
            }

            @Override
            public void beforeTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3) {
            }

            @Override
            public void onTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3) {
                ClearEditText.this.setEditTextDrawable();
            }
        });
    }

    public void setEditTextDrawable() {
        if (getText().toString().length() == 0) {
            setCompoundDrawables(null, null, null, null);
        } else {
            setCompoundDrawables(null, null, this.dRight, null);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.dRight = null;
        this.rBounds = null;

    }

    @Override
    public boolean onTouchEvent(MotionEvent paramMotionEvent) {
        if ((this.dRight != null) && (paramMotionEvent.getAction() == 1)) {
            this.rBounds = this.dRight.getBounds();
            int i = (int) paramMotionEvent.getRawX();
            // int i = (int) paramMotionEvent.getX();
            if (i > getRight() - 3 * this.rBounds.width()) {
                setText("");
                paramMotionEvent.setAction(MotionEvent.ACTION_CANCEL);
            }
        }
        return super.onTouchEvent(paramMotionEvent);
    }

    @Override
    public void setCompoundDrawables(Drawable paramDrawable1, Drawable paramDrawable2, Drawable paramDrawable3, Drawable paramDrawable4) {
        if (paramDrawable3 != null)
            this.dRight = paramDrawable3;
        super.setCompoundDrawables(paramDrawable1, paramDrawable2, paramDrawable3, paramDrawable4);
    }


}