package com.itxiaox.xpermission.library;

import android.app.Activity;

import com.itxiaox.xpermission.library.listener.RequestPermission;

public class PermissionManager {

    public static void request(Activity activity,String[] premissions){
        //创建需要生成的类名，通过反射生成对象
        String className = activity.getClass().getName() + "$Permissions";
        try {
            //假设能获取到这个不存在的类
            Class<?> clazz = Class.forName(className);
            RequestPermission requestPermission = (RequestPermission) clazz.newInstance();
            requestPermission.requestPermission(activity,premissions);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void onRequestPermissonsResult(Activity activity,int requestCode,int[] grantResults){
        String className = activity.getClass().getName() + "$Permissions";
        try {
            //假设能获取到这个不存在的类
            Class<?> clazz = Class.forName(className);
            RequestPermission requestPermission = (RequestPermission) clazz.newInstance();
            requestPermission.onRequestPermissionsResult(activity,requestCode,grantResults);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
