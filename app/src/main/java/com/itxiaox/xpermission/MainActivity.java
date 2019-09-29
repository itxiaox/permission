package com.itxiaox.xpermission;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.itxiaox.xpermission.annotation.NeedsPermission;
import com.itxiaox.xpermission.annotation.OnNeverAskAgain;
import com.itxiaox.xpermission.annotation.OnPermissionDenied;
import com.itxiaox.xpermission.annotation.OnShowRationale;
import com.itxiaox.xpermission.library.PermissionManager;
import com.itxiaox.xpermission.library.listener.PermissionRequest;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void camera(View view){
        PermissionManager.request(this,new String[]{Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.READ_CONTACTS});
    }

    @NeedsPermission()
    void showCamera(){
        Log.d(TAG, "showCamera: ");
    }

    @OnPermissionDenied()
    public void denied(){
        Log.d(TAG, "denied: ");
    }
    @OnShowRationale()
    void showRationaleForCamera(final PermissionRequest request){
        Log.d(TAG, "showRationaleForCamera: ");

        //再次请求权限
//        request.proceed();
    }

    @OnNeverAskAgain()
    void onNeverAgain(){
        Log.d(TAG, "onNeverAgain: ");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "onRequestPermissionsResult: ");
        PermissionManager.onRequestPermissonsResult(this,requestCode,grantResults);
    }
}
