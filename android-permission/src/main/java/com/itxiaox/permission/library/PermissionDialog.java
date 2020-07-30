package com.itxiaox.permission.library;

import android.app.Activity;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

import com.itxiaox.permission.library.listener.PermissionRequest;
import com.itxiaox.permission.library.listener.RequestPermission;

public class PermissionDialog {

    /**
     * 权限拒绝，去设置对话框
     * @param activity
     * @param title
     * @param msg
     */
    public static void showNeverAgain(final Activity activity, String title, String msg) {
        new AlertDialog.Builder(activity).setTitle(title)
                .setMessage(msg)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).setPositiveButton("去设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //再次请求权限
                PermissionManager.goSetting(activity);
            }
        }).create().show();
    }

    public static void showRationale(Activity activity, final PermissionRequest request,String title
            ,String msg){
        new AlertDialog.Builder(activity).setTitle(title)
                .setMessage(msg)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).setPositiveButton("再次请求", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //再次请求权限
                request.proceed();
            }
        }).create().show();
    }
}
