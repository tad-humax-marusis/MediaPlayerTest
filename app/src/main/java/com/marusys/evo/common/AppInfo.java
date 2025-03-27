package common;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.util.HashMap;

public class AppInfo {
    public static String OWNER = "[TadZeila]";
    public static String CHANNEL_ID = "TAD";
    public static String CHANNEL_NAME = "ZEILA";

    private WindowManager mWindowManager = null;
    private Context mApplicationContext = null;
    private Resources mAppResource = null;
    private Resources mSystemService = null;

    private static AppInfo mInstance = null;
    private HashMap<String, Integer> mDrawableMap = new HashMap<String, Integer>();
    private AppInfo() {};

    public static AppInfo getInstance() {
        if(mInstance==null) {
            mInstance = new AppInfo();
        }
        return mInstance;
    }

    public Context getApplicationContext() {
        return mApplicationContext;
    }

    public void setApplicationContext(Context context) {
        mApplicationContext = context;
    }

    public WindowManager getWindowManager() {
        return mWindowManager;
    }

    public void setWindowManager(WindowManager w) {
        mWindowManager = w;
    }

    public Resources getAppResource() {
        if(mAppResource == null) {
            mAppResource = mApplicationContext.getResources();
        }
        return mAppResource;
    }

    public <T> T getSystemService(Class<T> serviceClass )
    {
        return (T)mApplicationContext.getSystemService(serviceClass);
    }

    public Object getSystemService(String serviceName)
    {
        return mApplicationContext.getSystemService(serviceName);
    }

    public void addDrawAbleId(String name, int ID) {
        mDrawableMap.put(name, ID);
    }

    public int getDrawAbleId(String name) {
        return (int)mDrawableMap.get(name);
    }
}