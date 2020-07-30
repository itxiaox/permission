# Android动态注解处理框架

通过APT技术实现Android动态权限框处理架，采用编译期注解，动态生成代码，解决反射注解的性能问题。


[![](https://jitpack.io/v/itxiaox/permission.svg)](https://jitpack.io/#itxiaox/permission)


# 引用：

- 在根目录下build.gradle中添加如下代码
```
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
        google()
        jcenter()
        
    }
}
```

- 在需要动态权限的模块中添加依赖
```
  annotationProcessor  "com.github.itxiaox.permission:permission-processor:0.0.4"
    implementation  "com.github.itxiaox.permission:android-permission:0.0.4"

```

- 代码中使用
  ```
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

    /**
     * 权限通过的注解
     */
    @NeedsPermission()
    void showCamera() {
//        Log.d(TAG, "showCamera: ");
        Toast.makeText(MainActivity.this, "获取到权限", Toast.LENGTH_SHORT).show();
    }

    /**
     * 权限拒绝的注解
     */
    @OnPermissionDenied()
    public void denied() {
        Log.d(TAG, "denied: ");
        Toast.makeText(MainActivity.this, "权限被拒绝：", Toast.LENGTH_SHORT).show();

    }

    /**
     * 权限说明的拒绝，一般这里可以采用一个对话框说明该权限的作用
     * @param request
     */
    @OnShowRationale()
    void showRationaleForCamera(final PermissionRequest request) {
        Log.d(TAG, "showRationaleForCamera: ");
        new AlertDialog.Builder(MainActivity.this).setTitle("权限说明")
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
        Log.d(TAG, "onNeverAgain: ");
        //todo 这里可以用一个对话框，提示用户，然后跳转到设置界面
        Toast.makeText(MainActivity.this, "再次拒绝，请到设置中开启应用权限", Toast.LENGTH_SHORT).show();


    }

//    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "onRequestPermissionsResult: ");
        Toast.makeText(MainActivity.this, "获取权限结果："+grantResults.length, Toast.LENGTH_SHORT).show();
        PermissionManager.onRequestPermissonsResult(this,requestCode,grantResults);
    }
}

  
  ```
