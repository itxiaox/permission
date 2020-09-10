package com.itxiaox.permission;

import android.Manifest;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.itxiaox.permission.annotation.NeedsPermission;
import com.itxiaox.permission.annotation.OnNeverAskAgain;
import com.itxiaox.permission.annotation.OnPermissionDenied;
import com.itxiaox.permission.annotation.OnShowRationale;
import com.itxiaox.permission.library.PermissionDialog;
import com.itxiaox.permission.library.PermissionManager;
import com.itxiaox.permission.library.listener.PermissionRequest;

public class Fragment extends DialogFragment {
    String[] permissions = new String[]{Manifest.permission.CAMERA};
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment,null);
        view.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PermissionManager.request(getActivity(), permissions);
            }
        });
        return view;
    }

    /**
     * 权限通过的注解
     */
    @NeedsPermission()
    void showCamera() {
        Toast.makeText(getActivity(), "获取到权限", Toast.LENGTH_SHORT).show();
    }

    /**
     * 权限拒绝的注解
     */
    @OnPermissionDenied()
    public void denied() {
        Toast.makeText(getActivity(), "摄像头权限被拒绝：", Toast.LENGTH_SHORT).show();
    }

    /**
     * 权限说明的拒绝，一般这里可以采用一个对话框说明该权限的作用
     *
     * @param request
     */
    @OnShowRationale()
    void showRationaleForCamera(final PermissionRequest request) {
        //再次请求权限
        //request.proceed();
        PermissionDialog.showRationale(getActivity(), request,
                "权限说明", "您需要此权限进行相关操作");
    }

    @OnNeverAskAgain()
    void onNeverAgain() {
        PermissionDialog.showNeverAgain(getActivity(), "权限已拒绝",
                "您已经拒接了相关权限，请去设置中开启");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.onRequestPermissionsResult(getActivity(), requestCode, grantResults);
    }
}
