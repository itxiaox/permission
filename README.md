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
  annotationProcessor  "com.github.itxiaox.permission:permission-processor:1.0.0"
    implementation  "com.github.itxiaox.permission:android-permission:1.0.0"

```

- 代码中使用
  ```
    package com.itxiaox.permission;

    import android.Manifest;
    import android.os.Bundle;
    import android.view.View;
    import android.widget.Toast;

    import androidx.annotation.NonNull;

    import com.itxiaox.permission.annotation.NeedsPermission;
    import com.itxiaox.permission.annotation.OnNeverAskAgain;
    import com.itxiaox.permission.annotation.OnPermissionDenied;
    import com.itxiaox.permission.annotation.OnShowRationale;
    import com.itxiaox.permission.library.PermissionDialog;
    import com.itxiaox.permission.library.PermissionManager;
    import com.itxiaox.permission.library.listener.PermissionRequest;

    public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";

    String[] permissions = new String[]{Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
            , Manifest.permission.READ_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void camera(View view) {
        PermissionManager.request(this, permissions);
    }

    /**
     * 权限通过的注解
     */
    @NeedsPermission()
    void showCamera() {
        Toast.makeText(MainActivity.this, "获取到权限", Toast.LENGTH_SHORT).show();
    }

    /**
     * 权限拒绝的注解
     */
    @OnPermissionDenied()
    public void denied() {
        Toast.makeText(MainActivity.this, "权限被拒绝：", Toast.LENGTH_SHORT).show();

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
        PermissionDialog.showRationale(this, request,
                "权限说明", "您需要此权限进行相关操作");
    }

    @OnNeverAskAgain()
    void onNeverAgain() {
        PermissionDialog.showNeverAgain(this, "权限已拒绝",
                "您已经拒接了相关权限，请去设置中开启");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.onRequestPermissionsResult(this, requestCode, grantResults);
    }

}


  
  ```
