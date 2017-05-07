package com.android.launcher3.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Region;
import android.util.AttributeSet;
import android.util.TypedValue;

import com.android.launcher3.DynamicGrid.DeviceProfile;
import com.android.launcher3.info.AppInfo;
import com.android.launcher3.LauncherAppState;
import com.android.launcher3.Utilities;
import com.yline.base.BaseTextView;

/**
 * An icon on a PagedView, specifically for items in the launcher's paged view (with compound
 * drawables on the top).
 */
public class PagedViewIcon extends BaseTextView
{
    /** A simple callback interface to allow a PagedViewIcon to notify when it has been pressed */
    public static interface PressedCallback
    {
        void iconPressed(PagedViewIcon icon);
    }
    
    @SuppressWarnings("unused")
    private static final String TAG = "PagedViewIcon";
    
    private static final float PRESS_ALPHA = 0.4f;
    
    private PagedViewIcon.PressedCallback mPressedCallback;
    
    private boolean mLockDrawableState = false;
    
    private Bitmap mIcon;
    
    public PagedViewIcon(Context context)
    {
        this(context, null);
    }
    
    public PagedViewIcon(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }
    
    public PagedViewIcon(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }
    
    public void onFinishInflate()
    {
        super.onFinishInflate();
        
        // Ensure we are using the right text size
        LauncherAppState app = LauncherAppState.getInstance();
        DeviceProfile grid = app.getDynamicGrid().getDeviceProfile();
        setTextSize(TypedValue.COMPLEX_UNIT_SP, grid.iconTextSize);
    }
    
    public void applyFromApplicationInfo(AppInfo info, boolean scaleUp, PagedViewIcon.PressedCallback cb)
    {
        mIcon = info.iconBitmap;
        mPressedCallback = cb;
        setCompoundDrawables(null, Utilities.createIconDrawable(mIcon), null, null);
        setText(info.title);
        setTag(info);
    }
    
    public void lockDrawableState()
    {
        mLockDrawableState = true;
    }
    
    public void resetDrawableState()
    {
        mLockDrawableState = false;
        post(new Runnable()
        {
            @Override
            public void run()
            {
                refreshDrawableState();
            }
        });
    }
    
    protected void drawableStateChanged()
    {
        super.drawableStateChanged();
        
        // We keep in the pressed state until resetDrawableState() is called to reset the press
        // feedback
        if (isPressed())
        {
            setAlpha(PRESS_ALPHA);
            if (mPressedCallback != null)
            {
                mPressedCallback.iconPressed(this);
            }
        }
        else if (!mLockDrawableState)
        {
            setAlpha(1f);
        }
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public void draw(Canvas canvas)
    {
        // If text is transparent, don't draw any shadow
        if (getCurrentTextColor() == getResources().getColor(android.R.color.transparent))
        {
            getPaint().clearShadowLayer();
            super.draw(canvas);
            return;
        }
        
        // We enhance the shadow by drawing the shadow twice
        getPaint().setShadowLayer(BubbleTextView.SHADOW_LARGE_RADIUS,
            0.0f,
            BubbleTextView.SHADOW_Y_OFFSET,
            BubbleTextView.SHADOW_LARGE_COLOUR);
        super.draw(canvas);
        canvas.save(Canvas.CLIP_SAVE_FLAG);
        canvas.clipRect(getScrollX(), getScrollY() + getExtendedPaddingTop(), getScrollX() + getWidth(), getScrollY()
            + getHeight(), Region.Op.INTERSECT);
        getPaint().setShadowLayer(BubbleTextView.SHADOW_SMALL_RADIUS, 0.0f, 0.0f, BubbleTextView.SHADOW_SMALL_COLOUR);
        super.draw(canvas);
        canvas.restore();
    }
}
