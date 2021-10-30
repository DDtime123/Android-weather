package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
    // 用于指定照片存储位置的 Uri，这个 Uri 注册为 contentProvider
    private Uri imageUri;

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.DownloadImage:{
                // 请求存储权限

                // 下载图片 from imageView cache
                saveImageFromImageView();
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

    private void saveImageFromImageView(){
        imageView.setDrawingCacheEnabled(true);
        Bitmap bm = imageView.getDrawingCache();

        String root = Environment.getExternalStorageDirectory().toString();
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
        // 图片单选按钮
        Button image1 = (Button) findViewById(R.id.Image1);
        Button image2 = (Button) findViewById(R.id.Image2);
        Button image3 = (Button) findViewById(R.id.Image3);
        Button image4 = (Button) findViewById(R.id.Image4);
        downImage.setOnClickListener(this);
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
