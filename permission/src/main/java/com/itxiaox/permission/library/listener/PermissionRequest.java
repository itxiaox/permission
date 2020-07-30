package com.itxiaox.permission.library.listener;

public interface PermissionRequest {

    /**
     * 继续请求接口，用户拒绝一次后，给出Dialog提示框
     */
    void proceed();
}
