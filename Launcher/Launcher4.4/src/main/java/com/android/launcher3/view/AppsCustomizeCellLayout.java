package com.android.launcher3.view;

import android.content.Context;
import android.view.View;

public class AppsCustomizeCellLayout extends CellLayout implements Page
{
    public AppsCustomizeCellLayout(Context context)
    {
        super(context);
    }
    
    @Override
    public void removeAllViewsOnPage()
    {
        removeAllViews();
        setLayerType(LAYER_TYPE_NONE, null);
    }
    
    @Override
    public void removeViewOnPageAt(int index)
    {
        removeViewAt(index);
    }
    
    @Override
    public int getPageChildCount()
    {
        return getChildCount();
    }
    
    @Override
    public View getChildOnPageAt(int i)
    {
        return getChildAt(i);
    }
    
    @Override
    public int indexOfChildOnPage(View v)
    {
        return indexOfChild(v);
    }
    
    /**
     * Clears all the key listeners for the individual icons.
     */
    public void resetChildrenOnKeyListeners()
    {
        ShortcutAndWidgetContainer children = getShortcutsAndWidgets();
        int childCount = children.getChildCount();
        for (int j = 0; j < childCount; ++j)
        {
            children.getChildAt(j).setOnKeyListener(null);
        }
    }
}