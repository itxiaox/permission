package com.itxiaox.permission;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.itxiaox.permission.annotation.NeedsPermission;
import com.itxiaox.permission.annotation.OnNeverAskAgain;
import com.itxiaox.permission.annotation.OnPermissionDenied;
import com.itxiaox.permission.annotation.OnShowRationale;
import com.itxiaox.permission.library.PermissionManager;
import com.itxiaox.permission.library.listener.PermissionRequest;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void camera(View view) {
        PermissionManager.request(this, new String[]{Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
                , Manifest.permission.READ_EXTERNAL_STORAGE});
    }

    @NeedsPermission()
    void showCamera() {
//        Log.d(TAG, "showCamera: ");
        Toast.makeText(MainActivity.this, "获取到权限", Toast.LENGTH_SHORT).show();
    }

    @OnPermissionDenied()
    public void denied() {
        Log.d(TAG, "denied: ");
        Toast.makeText(MainActivity.this, "权限被拒绝：", Toast.LENGTH_SHORT).show();

    }

    @OnShowRationale()
    void showRationaleForCamera(final PermissionRequest request) {


        Log.d(TAG, "showRationaleForCamera: ");
        Toast.makeText(MainActivity.this, "权限说明showRationaleForCamera", Toast.LENGTH_SHORT).show();

        new AlertDialog.Builder(MainActivity.this).setTitle("权限被拒绝了")
                .setMessage("拍照需要此权限")
                .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).setPositiveButton("允许", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                request.proceed();
            }
        }).create().show();
        //再次请求权限
//        request.proceed();
    }

    @OnNeverAskAgain()
    void onNeverAgain() {
        Log.d(TAG, "onNeverAgain: ");
        Toast.makeText(MainActivity.this, "再次请求onNeverAgain", Toast.LENGTH_SHORT).show();

    }

//    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "onRequestPermissionsResult: ");
        Toast.makeText(MainActivity.this, "获取权限结果："+grantResults.length, Toast.LENGTH_SHORT).show();
        PermissionManager.onRequestPermissonsResult(this,requestCode,grantResults);
    }
}
