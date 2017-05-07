package com.android.launcher3.info;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.util.Log;

import com.android.launcher3.LauncherSettings;
import com.android.launcher3.helper.IconCache;

/**
 * Represents an app in AllAppsView.
 */
public class AppInfo extends ItemInfo
{
    private static final String TAG = "Launcher3.AppInfo";
    
    /**
     * The intent used to start the application.
     */
    public Intent intent;
    
    /**
     * A bitmap version of the application icon.
     */
    public Bitmap iconBitmap;
    
    /**
     * The time at which the app was first installed.
     */
    public long firstInstallTime;
    
    public ComponentName componentName;
    
    public static final int DOWNLOADED_FLAG = 1;
    
    static final int UPDATED_SYSTEM_APP_FLAG = 2;
    
    public int flags = 0;
    
    AppInfo()
    {
        itemType = LauncherSettings.BaseLauncherColumns.ITEM_TYPE_SHORTCUT;
    }
    
    public Intent getIntent()
    {
        return intent;
    }
    
    /**
     * Must not hold the Context.
     */
    public AppInfo(PackageManager pm, ResolveInfo info, IconCache iconCache, HashMap<Object, CharSequence> labelCache)
    {
        final String packageName = info.activityInfo.applicationInfo.packageName;
        
        this.componentName = new ComponentName(packageName, info.activityInfo.name);
        this.container = ItemInfo.NO_ID;
        this.setActivity(componentName, Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        
        try
        {
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            flags = initFlags(pi);
            firstInstallTime = initFirstInstallTime(pi);
        }
        catch (NameNotFoundException e)
        {
            Log.d(TAG, "PackageManager.getApplicationInfo failed for " + packageName);
        }
        
        iconCache.getTitleAndIcon(this, info, labelCache);
    }
    
    public static int initFlags(PackageInfo pi)
    {
        int appFlags = pi.applicationInfo.flags;
        int flags = 0;
        if ((appFlags & android.content.pm.ApplicationInfo.FLAG_SYSTEM) == 0)
        {
            flags |= DOWNLOADED_FLAG;
            
            if ((appFlags & android.content.pm.ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0)
            {
                flags |= UPDATED_SYSTEM_APP_FLAG;
            }
        }
        return flags;
    }
    
    public static long initFirstInstallTime(PackageInfo pi)
    {
        return pi.firstInstallTime;
    }
    
    public AppInfo(AppInfo info)
    {
        super(info);
        componentName = info.componentName;
        title = info.title.toString();
        intent = new Intent(info.intent);
        flags = info.flags;
        firstInstallTime = info.firstInstallTime;
    }
    
    /**
     * Creates the application intent based on a component name and various launch flags.
     * Sets {@link #itemType} to {@link LauncherSettings.BaseLauncherColumns#ITEM_TYPE_APPLICATION}.
     *
     * @param className the class name of the component representing the intent
     * @param launchFlags the launch flags
     */
    final void setActivity(ComponentName className, int launchFlags)
    {
        intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setComponent(className);
        intent.setFlags(launchFlags);
        itemType = LauncherSettings.BaseLauncherColumns.ITEM_TYPE_APPLICATION;
    }
    
    @Override
    public String toString()
    {
        return "ApplicationInfo(title=" + title.toString() + " id=" + this.id + " type=" + this.itemType
            + " container=" + this.container + " screen=" + screenId + " cellX=" + cellX + " cellY=" + cellY
            + " spanX=" + spanX + " spanY=" + spanY + " dropPos=" + dropPos + ")";
    }
    
    public static void dumpApplicationInfoList(String tag, String label, ArrayList<AppInfo> list)
    {
        Log.d(tag, label + " size=" + list.size());
        for (AppInfo info : list)
        {
            Log.d(tag, "   title=\"" + info.title + "\" iconBitmap=" + info.iconBitmap + " firstInstallTime="
                + info.firstInstallTime);
        }
    }
    
    public ShortcutInfo makeShortcut()
    {
        return new ShortcutInfo(this);
    }
}
