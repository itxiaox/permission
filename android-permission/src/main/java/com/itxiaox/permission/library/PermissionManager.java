package com.itxiaox.permission.library;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import com.itxiaox.permission.library.listener.RequestPermission;

public class PermissionManager {

    public static void request(Activity activity,String[] premissions){
        //创建需要生成的类名，通过反射生成对象
        String className = activity.getClass().getName() + "$Permission";
        try {
            //假设能获取到这个不存在的类
            Class<?> clazz = Class.forName(className);
            RequestPermission requestPermission = (RequestPermission) clazz.newInstance();
            requestPermission.requestPermission(activity,premissions);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void onRequestPermissionsResult(Activity activity,int requestCode,int[] grantResults){
        String className = activity.getClass().getName() + "$Permission";
        try {
            //假设能获取到这个不存在的类
            Class<?> clazz = Class.forName(className);
            RequestPermission requestPermission = (RequestPermission) clazz.newInstance();
            requestPermission.onRequestPermissionsResult(activity,requestCode,grantResults);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 跳转到设置界面
     * @param context
     */
    public static void goSetting(Context context){
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
        }
        //再次请求权限
        context.startActivity(localIntent);
    }
}
