package com.android.launcher3.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.yline.base.BaseTabWidget;

public class FocusOnlyTabWidget extends BaseTabWidget
{
    public FocusOnlyTabWidget(Context context)
    {
        super(context);
    }
    
    public FocusOnlyTabWidget(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }
    
    public FocusOnlyTabWidget(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }
    
    public View getSelectedTab()
    {
        final int count = getTabCount();
        for (int i = 0; i < count; ++i)
        {
            View v = getChildTabViewAt(i);
            if (v.isSelected())
            {
                return v;
            }
        }
        return null;
    }
    
    public int getChildTabIndex(View v)
    {
        final int tabCount = getTabCount();
        for (int i = 0; i < tabCount; ++i)
        {
            if (getChildTabViewAt(i) == v)
            {
                return i;
            }
        }
        return -1;
    }
    
    public void setCurrentTabToFocusedTab()
    {
        View tab = null;
        int index = -1;
        final int count = getTabCount();
        for (int i = 0; i < count; ++i)
        {
            View v = getChildTabViewAt(i);
            if (v.hasFocus())
            {
                tab = v;
                index = i;
                break;
            }
        }
        if (index > -1)
        {
            super.setCurrentTab(index);
            super.onFocusChange(tab, true);
        }
    }
    
    public void superOnFocusChange(View v, boolean hasFocus)
    {
        super.onFocusChange(v, hasFocus);
    }
    
    @Override
    public void onFocusChange(android.view.View v, boolean hasFocus)
    {
        if (v == this && hasFocus && getTabCount() > 0)
        {
            getSelectedTab().requestFocus();
            return;
        }
    }
}
