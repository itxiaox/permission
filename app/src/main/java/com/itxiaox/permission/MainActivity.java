package com.itxiaox.permission;

import android.Manifest;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.itxiaox.permission.annotation.NeedsPermission;
import com.itxiaox.permission.annotation.OnNeverAskAgain;
import com.itxiaox.permission.annotation.OnPermissionDenied;
import com.itxiaox.permission.annotation.OnShowRationale;
import com.itxiaox.permission.library.PermissionDialog;
import com.itxiaox.permission.library.PermissionManager;
import com.itxiaox.permission.library.listener.PermissionRequest;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    String[] permissions = new String[]{Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
            , Manifest.permission.READ_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new Fragment();
                fragment.show(getSupportFragmentManager(),"diaglog_premission");
            }
        });

    }

    public void camera(View view) {
        PermissionManager.request(this,
                permissions);
    }

    /**
     * 权限通过的注解
     */
    @NeedsPermission()
    void showCamera() {
        Toast.makeText(MainActivity.this,
                "获取到权限", Toast.LENGTH_SHORT).show();
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
//        request.proceed();
        PermissionDialog.showRationale(this, request,
                "权限说明", "您需要此权限进行相关操作");
    }

    @OnNeverAskAgain()
    void onNeverAgain() {
        PermissionDialog.showNeverAgain(this, "权限已拒绝",
                "您已经拒接了相关权限，请去设置中开启");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.onRequestPermissionsResult(this, requestCode, grantResults);
    }

}
