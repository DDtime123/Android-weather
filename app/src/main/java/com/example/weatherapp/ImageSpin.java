package com.example.weatherapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

public class ImageSpin extends AppCompatActivity implements View.OnClickListener {
    // 向指导窗口发送的请求码
    private static final int Accept_Or_Cancel = 1;
    // 请求权限的请求码
    private static final int GrantedWriteOrNot = 2;

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.DownloadImage:{
                // 无需请求存储权限
                // 下载图片到程序缓存区域 from imageView cache
                saveImageFromImageView();
            }
            case R.id.DownloadImageIntoExternal:{
                // 请求存储权限
                getWriteGranted();
                // 下载图片到系统相册 from imageView cache
                saveImageFromImageViewIntoExterStorage();
            }
            break;
            case R.id.Image1:{
                index = 0;
                LoadImage(index);
            }
            break;
            case R.id.Image2:{
                index = 1;
                LoadImage(index);
            }
            break;
            case R.id.Image3:{
                index = 2;
                LoadImage(index);
            }
            break;
            case R.id.Image4:{
                index = 3;
                LoadImage(index);
            }
            break;
            default:
                break;
        }
    }

    private void getWriteGranted(){
        Context mContext = ImageSpin.this;
        // 1.检查是否已经授予了 REQUEST_PERMISSION 权限
        if(ContextCompat.checkSelfPermission(
                mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            // 1.1可以直接使用系统外设，访问系统存储文件

        }else if(shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) { // 1.2 之前没有授权，现在检查是否需要弹出指导窗口

            // 1.2.1 弹出指导窗口 这里指导窗口使用一个 Dialog 实现
            showInContextUI();
        }else{
            // 1.2.2 无需弹出指导窗口，进行请求授权
            //requestPermissionLauncher.launch(Manifest.permission.<REQUESTED_PERMISSION>);
            ActivityCompat.requestPermissions(ImageSpin.this,
                    new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE },
                    GrantedWriteOrNot);
        }
    }

    // 请求权限是否成功
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case GrantedWriteOrNot:{
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 获取授权成功
                    Toast.makeText(ImageSpin.this, "Write Permission Granted", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(ImageSpin.this, "Write Permission Denied", Toast.LENGTH_SHORT).show();
                }
            }
                break;
            default:
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    // 处理从指导窗口的返回值
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case Accept_Or_Cancel:{
                if(resultCode == RESULT_OK){
                    // 进行请求授权
                    //requestPermissionLauncher.launch(Manifest.permission.<REQUESTED_PERMISSION>);
                    ActivityCompat.requestPermissions(ImageSpin.this,
                            new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE },
                            GrantedWriteOrNot);
                }else{
                    // 退出Activity
                    finish();
                }
            }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    // 弹出指导窗口
    private void showInContextUI(){
        startActivityForResult(new Intent(ImageSpin.this,InfoDialog.class),Accept_Or_Cancel);
    }

    // 在系统相册区存储图片文件
    private void saveImageFromImageViewIntoExterStorage(){
        imageView.setDrawingCacheEnabled(true);
        Bitmap bm = imageView.getDrawingCache();

        String root = Environment.getExternalStorageDirectory().toString();
        // 存储在应用程序临时 cache 区域，无需用户授权
        File myDir = new File(root+"/DCIM/ImagesFromMyApp");
        if (!myDir.exists()) {
            Log.d(TAG, "Make new Dir");
            myDir.mkdirs();
        }
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Image-"+ n +".jpg";
        File file = new File (myDir, fname);
        if (file.exists ())
            file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            Log.d(TAG, "save Image Success");
        } catch (Exception e) {
            Log.d(TAG, "save Image Failed");
            e.printStackTrace();
        }finally {
            imageView.setDrawingCacheEnabled(false);
        }

    }

    // 在应用程序缓存去区存储图片文件，无需授权
    private void saveImageFromImageView(){
        imageView.setDrawingCacheEnabled(true);
        Bitmap bm = imageView.getDrawingCache();

        String root = Environment.getExternalStorageDirectory().toString();
        // 存储在应用程序临时 cache 区域，无需用户授权
        File myDir = new File(getExternalCacheDir().toString());
        if (!myDir.exists()) {
            Log.d(TAG, "Make new Dir");
            myDir.mkdirs();
        }
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Image-"+ n +".jpg";
        File file = new File (myDir, fname);
        if (file.exists ())
            file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            Log.d(TAG, "save Image Success");
        } catch (Exception e) {
            Log.d(TAG, "save Image Failed");
            e.printStackTrace();
        }finally {
            imageView.setDrawingCacheEnabled(false);
        }

    }

    private static final String[] urls = {
            "https://gitee.com/zhang-jianhua1/blogimage/raw/master/img/nature-g4d058222c_640.jpg",
            "https://gitee.com/zhang-jianhua1/blogimage/raw/master/img/mountains-g6c54eb020_640.jpg",
            "https://gitee.com/zhang-jianhua1/blogimage/raw/master/img/owl-gdd735f0a1_640.jpg",
            "https://gitee.com/zhang-jianhua1/blogimage/raw/master/img/owl-gb4dce14fa_640.jpg"};
    private static int index = 0;
    private static final String TAG = "ImageSpin";
    ImageView imageView = null;
    TextView angle_cur;
    TextView size_cur;
    private Context mContext;
    ViewGroup.LayoutParams params = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_spin);
        imageView = (ImageView) findViewById(R.id.ImageList2);
        LoadImage(index);
        angle_cur = (TextView) findViewById(R.id.angle_cur);
        size_cur = (TextView) findViewById(R.id.size_cur);
        mContext = ImageSpin.this;

        params = new ViewGroup.LayoutParams(imageView.getLayoutParams());
        Log.d(TAG, "params_width:"+params.width);

        Button downImage = (Button) findViewById(R.id.DownloadImage);
        Button downImageIntoExternal = (Button) findViewById(R.id.DownloadImageIntoExternal);
        // 图片单选按钮
        Button image1 = (Button) findViewById(R.id.Image1);
        Button image2 = (Button) findViewById(R.id.Image2);
        Button image3 = (Button) findViewById(R.id.Image3);
        Button image4 = (Button) findViewById(R.id.Image4);
        downImage.setOnClickListener(this);
        downImageIntoExternal.setOnClickListener(this);
        image1.setOnClickListener(this);
        image2.setOnClickListener(this);
        image3.setOnClickListener(this);
        image4.setOnClickListener(this);

        // 调整角度的进度条
        SeekBar angleBar = (SeekBar)findViewById(R.id.angleBar);
        angleBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                angle_cur.setText(" " + (int)(progress*3.6f) + "  / 360 ");
                imageView.setRotation(progress*3.6f);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //Toast.makeText(mContext, "触碰SeekBar", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //Toast.makeText(mContext, "放开SeekBar", Toast.LENGTH_SHORT).show();
            }
        });

        // 调整图片大小的进度条
        SeekBar sizeBar = (SeekBar)findViewById(R.id.sizeBar);
        sizeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                size_cur.setText(" " + progress + "  / 100 ");
                ViewGroup.LayoutParams params_cur = (ViewGroup.LayoutParams) imageView.getLayoutParams();
                Log.d(TAG, "params.width: "+params_cur.width);
                params_cur.width = params.width*(100-progress)/100;
                imageView.setLayoutParams(params_cur);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //Toast.makeText(mContext, "触碰SeekBar", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //Toast.makeText(mContext, "放开SeekBar", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void LoadImage(int index){
        Picasso.with(this)
                .load(urls[index])
                .placeholder(R.drawable.ic_bilibili)
                .error(R.drawable.ic_camera)
                .into(imageView);
    }
}
