package com.example.daniel.values;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.ViewConfiguration;
import android.view.WindowManager;

/**
 * Created by Daniel on 2017-07-16.
 */

public class Resolution {

    static public int width;
    static public int height;
    static public boolean hasNavigationBar = false;
    static public boolean hasBackKey;
    static public boolean hasMenuKey;
    static public boolean hasSoftKeys;

    static public Activity activity;
    public Resolution(Activity activity){
        this.activity=activity;
        getResolution();
        hasNavBar(activity.getResources());
    }
    public static Point availableSpaceInPixels(Context c, Activity a){
        Point p=new Point(0,0);
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.JELLY_BEAN_MR1){
            Display d = a.getWindowManager().getDefaultDisplay();

            DisplayMetrics realDisplayMetrics = new DisplayMetrics();
            d.getRealMetrics(realDisplayMetrics);

            int realHeight = realDisplayMetrics.heightPixels;
            int realWidth = realDisplayMetrics.widthPixels;

            DisplayMetrics displayMetrics = new DisplayMetrics();
            d.getMetrics(displayMetrics);

            int displayHeight = displayMetrics.heightPixels;
            int displayWidth = displayMetrics.widthPixels;
            p = new Point(realWidth,realHeight);

        }
        return p;
    }
    public static boolean hasSoftKeys(WindowManager windowManager, Context c, Activity a){
        boolean hasSoftwareKeys = true;

        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.JELLY_BEAN_MR1){
            Display d = a.getWindowManager().getDefaultDisplay();

            DisplayMetrics realDisplayMetrics = new DisplayMetrics();
            d.getRealMetrics(realDisplayMetrics);

            int realHeight = realDisplayMetrics.heightPixels;
            int realWidth = realDisplayMetrics.widthPixels;

            DisplayMetrics displayMetrics = new DisplayMetrics();
            d.getMetrics(displayMetrics);

            int displayHeight = displayMetrics.heightPixels;
            int displayWidth = displayMetrics.widthPixels;

            hasSoftwareKeys =  (realWidth - displayWidth) > 0 || (realHeight - displayHeight) > 0;
        }else{
            boolean hasMenuKey = ViewConfiguration.get(c).hasPermanentMenuKey();
            boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
            hasSoftwareKeys = !hasMenuKey && !hasBackKey;
        }
        return hasSoftwareKeys;
    }
    public static void getResolution(){
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;
    }

    /*public static void setHasNavigationBar(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
        {
            hasNavigationBar = !ViewConfiguration.get(activity).hasPermanentMenuKey();
        }
        else
        {
            hasNavigationBar = false;
        }
    }*/

    public static void hasNavBar (Resources resources)
    {
        int id = resources.getIdentifier("config_showNavigationBar", "bool", "android");
        hasNavigationBar= id > 0 && resources.getBoolean(id);
    }

}
