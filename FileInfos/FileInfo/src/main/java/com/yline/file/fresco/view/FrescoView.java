package com.yline.file.fresco.view;

import android.content.Context;
import android.util.AttributeSet;

import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Fresco View，隔离一层
 *
 * @author yline 2017/9/23 -- 14:41
 * @version 1.0.0
 */
public class FrescoView extends SimpleDraweeView {
    public FrescoView(Context context) {
        super(context);
    }

    public FrescoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FrescoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public FrescoView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDetach() {
        super.onDetach();
        if (null != getController()) {
            getController().onDetach();
        }
    }
}
