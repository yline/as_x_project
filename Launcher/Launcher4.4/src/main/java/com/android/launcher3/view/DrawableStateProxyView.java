package com.android.launcher3.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.android.launcher3.R;
import com.yline.base.BaseLinearLayout;

public class DrawableStateProxyView extends BaseLinearLayout
{
    private View mView;
    
    private int mViewId;
    
    public DrawableStateProxyView(Context context)
    {
        this(context, null);
    }
    
    public DrawableStateProxyView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }
    
    public DrawableStateProxyView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DrawableStateProxyView, defStyle, 0);
        mViewId = a.getResourceId(R.styleable.DrawableStateProxyView_sourceViewId, -1);
        a.recycle();
        
        setFocusable(false);
    }
    
    @Override
    protected void drawableStateChanged()
    {
        super.drawableStateChanged();
        
        if (mView == null)
        {
            View parent = (View)getParent();
            mView = parent.findViewById(mViewId);
        }
        if (mView != null)
        {
            mView.setPressed(isPressed());
            mView.setHovered(isHovered());
        }
    }
    
    @Override
    public boolean onHoverEvent(MotionEvent event)
    {
        return false;
    }
}
