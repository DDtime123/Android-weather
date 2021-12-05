package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;

import com.example.ui.GalleryActivity;
import com.example.weatherapp.Student.StuListActivity;
import com.example.weatherapp.camera.CameraActivity;

public class SecondActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "SecondActivity";
    private static int REQUEST_CODE_CAMERA = 1;
    private Context mContext;

    /***************************************相机*********************************************/
    private void openCamera(){
        // 打开相机
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivity(intent);
    }
    private void Grand_Outer_Storage(){
        // 1.检查是否已经授予了 REQUEST_PERMISSION 权限
        if(ContextCompat.checkSelfPermission(
                mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            // 1.1可以直接使用系统外设，访问系统存储文件
            // ...
        }else if(shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) { // 1.2 之前没有授权，现在检查是否需要弹出指导窗口

            // 1.2.1 弹出指导窗口
            // showInContextUI(...);
        }else{
            // 1.2.2 无需弹出指导窗口，进行请求授权
            ActivityCompat.requestPermissions(SecondActivity.this,
                    new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_CODE_CAMERA );
        }
    }
    /****************************************************************************************/
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.toolbox:{
                startActivity(new Intent(SecondActivity.this,WeatherActivity.class));
            }
                break;
            case R.id.alipay:{
                // 打开支付宝 包名：com.eg.android.AlipayGphone
                Intent intent = getPackageManager().getLaunchIntentForPackage("com.eg.android.AlipayGphone");
                startActivity(intent);
            }
            case R.id.camera:{
                Intent intent = new Intent(SecondActivity.this, GalleryActivity.class);
                startActivity(intent);
                /**
                Intent intent = new Intent(SecondActivity.this,CameraActivity.class);
                startActivity(intent);
                 */
            }
                break;
            case R.id.home:{
                // 打开书籍app
                //startActivity(new Intent(SecondActivity.this,BookListActivity.class));
                // 打开图像app
                startActivity(new Intent(SecondActivity.this,ContainerActivity.class));
            }
            break;
            case R.id.music:{

            }
                break;
            case R.id.position:{

            }
                break;
            case R.id.student:{
                // 打开学生app
                startActivity(new Intent(SecondActivity.this, StuListActivity.class));
            }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Button position = (Button)findViewById(R.id.position);

        Button home = (Button)findViewById(R.id.home);
        Button student = (Button)findViewById(R.id.student);
        Button camera = (Button)findViewById(R.id.camera);
        Button alipay = (Button)findViewById(R.id.alipay);
        Button music = (Button)findViewById(R.id.music);
        Button toolbox = (Button)findViewById(R.id.toolbox);

        student.setOnClickListener(this);
        camera.setOnClickListener(this);
        alipay.setOnClickListener(this);
        music.setOnClickListener(this);
        toolbox.setOnClickListener(this);
        home.setOnClickListener(this);

        mContext=this;
    }



}















