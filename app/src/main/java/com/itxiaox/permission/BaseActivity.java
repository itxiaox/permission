package com.itxiaox.permission;

import android.content.DialogInterface;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.itxiaox.permission.annotation.OnNeverAskAgain;
import com.itxiaox.permission.annotation.OnShowRationale;
import com.itxiaox.permission.library.PermissionManager;
import com.itxiaox.permission.library.listener.PermissionRequest;

public class BaseActivity extends AppCompatActivity {


    /**
     * 权限说明的拒绝，一般这里可以采用一个对话框说明该权限的作用
     *
     * @param request
     */
    @OnShowRationale()
    void showRationaleForCamera(final PermissionRequest request) {
        new AlertDialog.Builder(this).setTitle("权限说明")
                .setMessage("拍照需要此权限，否则不能进行拍照")
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
        //再次请求权限
//        request.proceed();
    }

    @OnNeverAskAgain()
    void onNeverAgain() {
        //todo 这里可以用一个对话框，提示用户，然后跳转到设置界面
        Toast.makeText(this, "再次拒绝，请到设置中开启应用权限", Toast.LENGTH_SHORT).show();
        new AlertDialog.Builder(this).setTitle("权限已拒绝")
                .setMessage("您已经拒绝了此权限")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).setPositiveButton("去设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //再次请求权限
                PermissionManager.goSetting(BaseActivity.this);
            }
        }).create().show();
    }

}
