package com.flight.application;

import com.yline.application.BaseApplication;
import com.yline.application.SDKConfig;

import android.os.Message;

public class MainApplication extends BaseApplication
{
    public static final String TAG = "WCFlight";
    
    @Override
    protected void handlerDefault(Message msg)
    {
        
    }
    
    @Override
    protected SDKConfig initConfig()
    {
        SDKConfig sdkConfig = new SDKConfig();
        sdkConfig.setLogFilePath("WCFlight"); // 默认开启日志,并写到文件中
        return sdkConfig;
    }
}
