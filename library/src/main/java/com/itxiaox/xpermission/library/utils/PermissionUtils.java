package com.itxiaox.xpermission.library.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/**
 * 权限工具类
 */
public class PermissionUtils {
    private PermissionUtils(){}


    /**
     * 检查所有权限是否已允许
     * @param grantResults 授权结果
     * @return 如果所有权限已允许，则返回true
     */
    public static boolean verifyPermissions(int... grantResults){
        if (grantResults.length == 0) {
            return false;
        }

        for (int grantResult : grantResults) {
            if (grantResult != PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }

    /**
     * 用户申请权限组
     * @param context 上下文对象
     * @param permissions 权限集合
     * @return 如果所有权限已经允许，则返回true
     */
    public static boolean hasSelfPermission(Context context,String... permissions){
        for (String permission : permissions) {
            if (!hasSelfPermission(context,permission)){
                return false;
            }
        }
        return true;
    }

    /**
     * 用户申请权限组
     * @param context 上下文
     * @param permission 权限
     * @return 如果所有权限已允许，则返回true （6.0以下直接返回true）
     */
    private static boolean hasSelfPermission(Context context,String permission){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)return true;
        try{
            return ContextCompat.checkSelfPermission(context,permission) == PackageManager.PERMISSION_GRANTED;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 检查被拒绝的权限组中，是否有点击了“不再询问”的权限
     * <p>
     *     第一次打开app的时候 返回 false
     *     上次弹出权限请求点击了拒绝，但没有勾选“不再询问” 返回 true
     *     上次弹出了权限请求点击了拒绝，勾选了“不再询问” 返回 false
     *     点击了拒绝，但没有勾选“不再询问” 则返回 true, 点击了拒绝，并且勾选了“不再询问” 则返回 false
     * </p>
     * @param activity 上下文对象
     * @param permissions 被拒绝时候的权限组
     * @return 如果有任一 “不再询问”的权限组，则返回true,反之返回false
     */
    public static boolean shouldShowRequestPermissionRationale(Activity activity,String... permissions){
        for (String permission : permissions) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,permission)){
                return true;
            }
        }
        return false;
    }
}
