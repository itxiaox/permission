package com.itxiaox.permission.library.listener;

import androidx.annotation.NonNull;

public interface RequestPermission<T> {
    /**
     * 请求权限组
     * @param target
     * @param permissions
     */
    void requestPermission(T target,String[] permissions);

    /**
     * 授权结果返回
     * @param target 目标对象
     * @param requestCode 请求码
     * @param grantResults 权限授权结果
     */
    void onRequestPermissionsResult(T target, int requestCode, @NonNull int[] grantResults);
}
