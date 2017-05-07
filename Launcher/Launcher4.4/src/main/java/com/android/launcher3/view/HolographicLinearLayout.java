package com.android.launcher3.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.android.launcher3.R;
import com.android.launcher3.helper.HolographicViewHelper;
import com.yline.base.BaseLinearLayout;

public class HolographicLinearLayout extends BaseLinearLayout
{
    private final HolographicViewHelper mHolographicHelper;
    
    private ImageView mImageView;
    
    private int mImageViewId;
    
    private boolean mHotwordOn;
    
    private boolean mIsPressed;
    
    private boolean mIsFocused;
    
    public HolographicLinearLayout(Context context)
    {
        this(context, null);
    }
    
    public HolographicLinearLayout(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }
    
    public HolographicLinearLayout(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.HolographicLinearLayout, defStyle, 0);
        mImageViewId = a.getResourceId(R.styleable.HolographicLinearLayout_sourceImageViewId, -1);
        mHotwordOn = a.getBoolean(R.styleable.HolographicLinearLayout_stateHotwordOn, false);
        a.recycle();
        
        setWillNotDraw(false);
        mHolographicHelper = new HolographicViewHelper(context);
        
        setOnTouchListener(new OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (isPressed() != mIsPressed)
                {
                    mIsPressed = isPressed();
                    refreshDrawableState();
                }
                return false;
            }
        });
        
        setOnFocusChangeListener(new OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if (isFocused() != mIsFocused)
                {
                    mIsFocused = isFocused();
                    refreshDrawableState();
                }
            }
        });
    }
    
    @Override
    protected void drawableStateChanged()
    {
        super.drawableStateChanged();
        
        if (mImageView != null)
        {
            mHolographicHelper.generatePressedFocusedStates(mImageView);
            Drawable d = mImageView.getDrawable();
            if (d instanceof StateListDrawable)
            {
                StateListDrawable sld = (StateListDrawable)d;
                sld.setState(getDrawableState());
                sld.invalidateSelf();
            }
        }
    }
    
    public void invalidatePressedFocusedStates()
    {
        mHolographicHelper.invalidatePressedFocusedStates(mImageView);
        invalidate();
    }
    
    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        
        // One time call to generate the pressed/focused state -- must be called after
        // measure/layout
        if (mImageView == null)
        {
            mImageView = (ImageView)findViewById(mImageViewId);
        }
        mHolographicHelper.generatePressedFocusedStates(mImageView);
    }
    
    private boolean isHotwordOn()
    {
        return mHotwordOn;
    }
    
    public void setHotwordState(boolean on)
    {
        if (on == mHotwordOn)
        {
            return;
        }
        mHotwordOn = on;
        refreshDrawableState();
    }
    
    @Override
    public int[] onCreateDrawableState(int extraSpace)
    {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isHotwordOn())
        {
            mergeDrawableStates(drawableState, new int[] {R.attr.stateHotwordOn});
        }
        return drawableState;
    }
}
