package com.android.launcher3.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

import com.android.launcher3.DynamicGrid.DeviceProfile;
import com.android.launcher3.LauncherAnimUtils;
import com.android.launcher3.LauncherAppState;
import com.android.launcher3.R;
import com.android.launcher3.activity.Launcher;
import com.android.launcher3.base.DragSource;
import com.android.launcher3.helper.DragController;
import com.yline.base.BaseFrameLayout;
import com.yline.log.LogFileUtil;

public class SearchDropTargetBar extends BaseFrameLayout implements DragController.DragListener
{
    private static final int sTransitionInDuration = 200;
    
    private static final int sTransitionOutDuration = 175;
    
    private ObjectAnimator mDropTargetBarAnim;
    
    private ObjectAnimator mQSBSearchBarAnim;
    
    private static final AccelerateInterpolator sAccelerateInterpolator = new AccelerateInterpolator();
    
    private boolean mIsSearchBarHidden;
    
    private View mQSBSearchBar;
    
    private View mDropTargetBar;
    
    private ButtonDropTarget mInfoDropTarget;
    
    private ButtonDropTarget mDeleteDropTarget;
    
    private int mBarHeight;
    
    private boolean mDeferOnDragEnd = false;
    
    private Drawable mPreviousBackground;
    
    private boolean mEnableDropDownDropTargets;
    
    public SearchDropTargetBar(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }
    
    public SearchDropTargetBar(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }
    
    public void setup(Launcher launcher, DragController dragController)
    {
        LogFileUtil.v(Launcher.TAG, "searchDropTargetBar setup, the dragcontroller add listen list");
        dragController.addDragListener(this);
        dragController.addDragListener(mInfoDropTarget);
        dragController.addDragListener(mDeleteDropTarget);
        dragController.addDropTarget(mInfoDropTarget);
        dragController.addDropTarget(mDeleteDropTarget);
        dragController.setFlingToDeleteDropTarget(mDeleteDropTarget);
        mInfoDropTarget.setLauncher(launcher);
        mDeleteDropTarget.setLauncher(launcher);
        mQSBSearchBar = launcher.getQsbBar();
        if (mEnableDropDownDropTargets)
        {
            mQSBSearchBarAnim = LauncherAnimUtils.ofFloat(mQSBSearchBar, "translationY", 0, -mBarHeight);
        }
        else
        {
            mQSBSearchBarAnim = LauncherAnimUtils.ofFloat(mQSBSearchBar, "alpha", 1f, 0f);
        }
        setupAnimation(mQSBSearchBarAnim, mQSBSearchBar);
    }
    
    private void prepareStartAnimation(View v)
    {
        // Enable the hw layers before the animation starts (will be disabled in the onAnimationEnd
        // callback below)
        v.setLayerType(View.LAYER_TYPE_HARDWARE, null);
    }
    
    private void setupAnimation(ObjectAnimator anim, final View v)
    {
        anim.setInterpolator(sAccelerateInterpolator);
        anim.setDuration(sTransitionInDuration);
        anim.addListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationEnd(Animator animation)
            {
                v.setLayerType(View.LAYER_TYPE_NONE, null);
            }
        });
    }
    
    @Override
    protected void onFinishInflate()
    {
        super.onFinishInflate();
        
        // Get the individual components
        mDropTargetBar = findViewById(R.id.drag_target_bar);
        mInfoDropTarget = (ButtonDropTarget)mDropTargetBar.findViewById(R.id.info_target_text);
        mDeleteDropTarget = (ButtonDropTarget)mDropTargetBar.findViewById(R.id.delete_target_text);
        
        mInfoDropTarget.setSearchDropTargetBar(this);
        mDeleteDropTarget.setSearchDropTargetBar(this);
        
        mEnableDropDownDropTargets = getResources().getBoolean(R.bool.config_useDropTargetDownTransition);
        
        // Create the various fade animations
        if (mEnableDropDownDropTargets)
        {
            LauncherAppState app = LauncherAppState.getInstance();
            DeviceProfile grid = app.getDynamicGrid().getDeviceProfile();
            mBarHeight = grid.searchBarSpaceHeightPx;
            mDropTargetBar.setTranslationY(-mBarHeight);
            mDropTargetBarAnim = LauncherAnimUtils.ofFloat(mDropTargetBar, "translationY", -mBarHeight, 0f);
            
        }
        else
        {
            mDropTargetBar.setAlpha(0f);
            mDropTargetBarAnim = LauncherAnimUtils.ofFloat(mDropTargetBar, "alpha", 0f, 1f);
        }
        setupAnimation(mDropTargetBarAnim, mDropTargetBar);
    }
    
    public void finishAnimations()
    {
        prepareStartAnimation(mDropTargetBar);
        mDropTargetBarAnim.reverse();
        prepareStartAnimation(mQSBSearchBar);
        mQSBSearchBarAnim.reverse();
    }
    
    /*
     * Shows and hides the search bar.
     */
    public void showSearchBar(boolean animated)
    {
        boolean needToCancelOngoingAnimation = mQSBSearchBarAnim.isRunning() && !animated;
        if (!mIsSearchBarHidden && !needToCancelOngoingAnimation)
            return;
        if (animated)
        {
            prepareStartAnimation(mQSBSearchBar);
            mQSBSearchBarAnim.reverse();
        }
        else
        {
            mQSBSearchBarAnim.cancel();
            if (mEnableDropDownDropTargets)
            {
                mQSBSearchBar.setTranslationY(0);
            }
            else
            {
                mQSBSearchBar.setAlpha(1f);
            }
        }
        mIsSearchBarHidden = false;
    }
    
    public void hideSearchBar(boolean animated)
    {
        boolean needToCancelOngoingAnimation = mQSBSearchBarAnim.isRunning() && !animated;
        if (mIsSearchBarHidden && !needToCancelOngoingAnimation)
            return;
        if (animated)
        {
            prepareStartAnimation(mQSBSearchBar);
            mQSBSearchBarAnim.start();
        }
        else
        {
            mQSBSearchBarAnim.cancel();
            if (mEnableDropDownDropTargets)
            {
                mQSBSearchBar.setTranslationY(-mBarHeight);
            }
            else
            {
                mQSBSearchBar.setAlpha(0f);
            }
        }
        mIsSearchBarHidden = true;
    }
    
    /*
     * Gets various transition durations.
     */
    public int getTransitionInDuration()
    {
        return sTransitionInDuration;
    }
    
    public int getTransitionOutDuration()
    {
        return sTransitionOutDuration;
    }
    
    /*
     * DragController.DragListener implementation
     */
    @Override
    public void onDragStart(DragSource source, Object info, int dragAction)
    {
        // Animate out the QSB search bar, and animate in the drop target bar
        prepareStartAnimation(mDropTargetBar);
        mDropTargetBarAnim.start();
        if (!mIsSearchBarHidden)
        {
            prepareStartAnimation(mQSBSearchBar);
            mQSBSearchBarAnim.start();
        }
    }
    
    public void deferOnDragEnd()
    {
        mDeferOnDragEnd = true;
    }
    
    @Override
    public void onDragEnd()
    {
        if (!mDeferOnDragEnd)
        {
            // Restore the QSB search bar, and animate out the drop target bar
            prepareStartAnimation(mDropTargetBar);
            mDropTargetBarAnim.reverse();
            if (!mIsSearchBarHidden)
            {
                prepareStartAnimation(mQSBSearchBar);
                mQSBSearchBarAnim.reverse();
            }
        }
        else
        {
            mDeferOnDragEnd = false;
        }
    }
    
    public void onSearchPackagesChanged(boolean searchVisible, boolean voiceVisible)
    {
        if (mQSBSearchBar != null)
        {
            Drawable bg = mQSBSearchBar.getBackground();
            if (bg != null && (!searchVisible && !voiceVisible))
            {
                // Save the background and disable it
                mPreviousBackground = bg;
                mQSBSearchBar.setBackgroundResource(0);
            }
            else if (mPreviousBackground != null && (searchVisible || voiceVisible))
            {
                // Restore the background
                mQSBSearchBar.setBackground(mPreviousBackground);
            }
        }
    }
    
    public Rect getSearchBarBounds()
    {
        if (mQSBSearchBar != null)
        {
            final int[] pos = new int[2];
            mQSBSearchBar.getLocationOnScreen(pos);
            
            final Rect rect = new Rect();
            rect.left = pos[0];
            rect.top = pos[1];
            rect.right = pos[0] + mQSBSearchBar.getWidth();
            rect.bottom = pos[1] + mQSBSearchBar.getHeight();
            return rect;
        }
        else
        {
            return null;
        }
    }
}
