package com.example.weatherapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class WeatherActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "WeatherActivity";
    TextView weatherContent;
    TextView dateContent;
    Drawable weather_img;

    // 用于存储从相机拍摄回来的照片，用 ImageView 来存储
    private ImageView picture;
    // 用于指定照片存储位置的 Uri，这个 Uri 注册为 contentProvider
    private Uri imageUri;
    // 请求码，用于辨别返回的 Intent 应该对应的是哪一个结果，结果由请求方写
    public static final int TAKE_PHOTO = 1;

//    public static final int UPDATE_TEXT = 1;

    // 处理从 Service 中获取的数据
    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(messageReceiver, new IntentFilter("my-message"));
    }

    // Handling the received Intents for the "my-integer" event
    private BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Extract data included in the Intent
            Message msg =(Message) intent.getParcelableExtra("my-msg");

            // 首先设置天气显示
            String city = msg.getData().getString("city");
            String wea = msg.getData().getString("wea");
            String update_time = msg.getData().getString("update_time");
            String wea_img = msg.getData().getString("wea_img");

            int wea_img_id = getResources().getIdentifier(wea_img , "mipmap" , getPackageName()) ;
            weather_img = ContextCompat.getDrawable(context,wea_img_id);

            String tem = msg.getData().getString("tem");
            Log.d(TAG, "温度："+tem+" C\n"+city+"\n更新时间："+update_time+"\n");

            weatherContent.setText("温度："+tem+" C\n"+city+"\n更新时间："+update_time+"\n");
            weatherContent.setCompoundDrawablesWithIntrinsicBounds(null,weather_img,null,null);

            // 接下来设置日期显示
            String date = msg.getData().getString("date");
            String week = msg.getData().getString("week");
            String air = msg.getData().getString("air");
            dateContent.setText("日期："+date+"\n"+week+"\n空气质量："+air+"\n");
        }
    };

    // 获取从相机程序的请求码和返回码
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case TAKE_PHOTO:
                if(resultCode == RESULT_OK){
                    try{
                        // 将拍摄的照片显示出来
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        picture.setImageBitmap(bitmap);

                    }catch (FileNotFoundException e){
                        e.printStackTrace();
                    }
                }
                break;
             default:
                break;
        }
    }

    @Override
    protected void onPause() {
        // Unregister since the activity is not visible
        LocalBroadcastManager.getInstance(this).unregisterReceiver(messageReceiver);
        super.onPause();
    }

    // 消息回调函数
//    private Handler handler = new Handler(){
//        public void handleMessage(Message msg){
//            switch (msg.what){
//                case UPDATE_TEXT:
//                    // 可以在此处进行 UI 操作
//                    weatherContent.setText("msg.city: "+msg.getData().getString("city"));
//                    break;
//                default:
//                    break;
//            }
//        }
//    };

    @Override
    public void onClick(View v) {
        Log.d(TAG, "onClick: ");
        switch (v.getId()){
            case R.id.Contact:
                break;
            case R.id.Camera:{
                // 创建 File 对象，用于存储拍摄的图片
                File outputImage = new File(getExternalCacheDir() ,"output_image.jpg");
                try{
                    if(outputImage.exists()){
                        outputImage.delete();
                    }
                    outputImage.createNewFile();
                }catch(IOException e){
                    e.printStackTrace();
                }
                if(Build.VERSION.SDK_INT >= 24){
                    imageUri = FileProvider.getUriForFile(WeatherActivity.this,
                            "com.example.cameraalbumtest.fileprovider",outputImage);
                }else{
                    imageUri = Uri.fromFile(outputImage);
                }
                // 启动相机程序
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                startActivityForResult(intent, TAKE_PHOTO);
            }
                break;
            case R.id.Weixin: break;
            case R.id.Call: break;
            case R.id.Youku: break;
            case R.id.Toutiao: break;
            default:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        weatherContent = (TextView) findViewById(R.id.WebContent);
        dateContent = (TextView) findViewById(R.id.DateContent);
        Intent intent = new Intent(WeatherActivity.this,LongRunningService.class);
        this.startService(intent);

        ImageButton btn_contact = (ImageButton) findViewById(R.id.Contact);
        ImageButton btn_camera = (ImageButton) findViewById(R.id.Camera);
        ImageButton btn_weixin = (ImageButton) findViewById(R.id.Weixin);
        ImageButton btn_call = (ImageButton) findViewById(R.id.Call);
        ImageButton btn_youku = (ImageButton) findViewById(R.id.Youku);
        ImageButton btn_toutiao = (ImageButton) findViewById(R.id.Toutiao);

        btn_camera.setOnClickListener(this);
    }



}
