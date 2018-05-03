package com.example.view2picture;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements  EasyPermissions.PermissionCallbacks {

    private static final int RC_CAMERA_AND_LOCATION = 100;
    LinearLayout linearLayout;
    private ImageView ivTwoCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ivTwoCode = findViewById(R.id.qr);
    }

    private void getPic() {
        linearLayout = findViewById(R.id.ll);
        // 获取图片某布局
        linearLayout.setDrawingCacheEnabled(true);
        linearLayout.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        linearLayout.setDrawingCacheBackgroundColor(Color.WHITE);
        final Bitmap bmp = linearLayout.getDrawingCache(); // 获取图片
        savePicture(bmp, "test.jpg");// 保存图片
        // 已经申请过权限，做想做的事
    }


    public void savePicture(Bitmap bm, String fileName) {
        Log.i("xing", "savePicture: ------------------------");
        if (null == bm) {
            Log.i("xing", "savePicture: ------------------图片为空------");
            return;
        }
        File foder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/test");
        if (!foder.exists()) {
            foder.mkdirs();
        }
        File myCaptureFile = new File(foder, fileName);
        try {
            if (!myCaptureFile.exists()) {
                myCaptureFile.createNewFile();
            }
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
            //压缩保存到本地
            bm.compress(Bitmap.CompressFormat.JPEG, 90, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Toast.makeText(this, "保存成功!", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // 把执行结果的操作给EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        getPic();
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

    }

    public void qr(View view) {
        Bitmap bitmap = ZXingUtils.createQRImage("https://tx-php.xinhuifun.cn/appstatic/inviteShare.html?inviteCode=1GPIL&nickname=liaoli&from=timeline&isappinstalled=0", ivTwoCode.getWidth(), ivTwoCode.getHeight());
        ivTwoCode.setImageBitmap(bitmap);
    }

    public void view2Pic(View view) {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            getPic();
        } else {
            // 没有申请过权限，现在去申请
            EasyPermissions.requestPermissions(this, "",
                    RC_CAMERA_AND_LOCATION, perms);
        }
    }
}
