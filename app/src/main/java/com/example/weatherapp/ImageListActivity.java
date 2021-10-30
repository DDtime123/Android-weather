package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class ImageListActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String[] urls = {
            "https://gitee.com/zhang-jianhua1/blogimage/raw/master/img/nature-g4d058222c_640.jpg",
            "https://gitee.com/zhang-jianhua1/blogimage/raw/master/img/mountains-g6c54eb020_640.jpg",
            "https://gitee.com/zhang-jianhua1/blogimage/raw/master/img/owl-gdd735f0a1_640.jpg",
            "https://gitee.com/zhang-jianhua1/blogimage/raw/master/img/owl-gb4dce14fa_640.jpg"};
    private static int cur_index = 0;
    private static final String TAG = "ImageListActivity";
    ImageView imageView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_list);

        imageView = (ImageView) findViewById(R.id.ImageList);

        LoadNextImage();
        Button lastImage = (Button)findViewById(R.id.LastImage);
        lastImage.setOnClickListener(this);
        Button nextImage = (Button)findViewById(R.id.NextImage);
        nextImage.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.LastImage:{
                // 获取并加载图像
                // 使用 OkHttp
                LoadNextImage();
                Log.d(TAG, "onClick: ");
            }
                break;
            case R.id.NextImage:{
                // 获取并加载图像
                // 使用 OkHttp
                LoadLastImage();
                Log.d(TAG, "onClick: ");
            }
            break;
            default:
                break;
        }
    }

    public void LoadNextImage(){
//        OkHttpClient client = new OkHttpClient.Builder()
//                .addInterceptor(new Interceptor() {
//                    @Override
//                    public Response intercept(Chain chain) throws IOException {
//                        Request newRequest = chain.request().newBuilder()
//                                .addHeader("Authorization","563492ad6f917000010000012cedbf28021f4925a611b30a94dec7c8")
//                                .build();
//                        return chain.proceed(newRequest);
//                    }
//                })
//                .build();
//
//        Picasso picasso = new Picasso.Builder(this)
//                .downloader(new OkHttp3Downloader(client))
//                .build();

        Animation aniFade = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fadeout);
        imageView.startAnimation(aniFade);

        Picasso.with(this)
                .load(urls[cur_index])
                .placeholder(R.drawable.ic_bilibili)
                .error(R.drawable.ic_camera)
                .into(imageView);
        Animation aniFade2 = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fadein);
        imageView.startAnimation(aniFade2);
        cur_index = (cur_index+1)%urls.length;
    }

    public void LoadLastImage(){
        Animation aniFade = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fadeout);
        imageView.startAnimation(aniFade);

        Picasso.with(this)
                .load(urls[cur_index])
                .placeholder(R.drawable.ic_bilibili)
                .error(R.drawable.ic_camera)
                .into(imageView);
        Animation aniFade2 = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fadein);
        imageView.startAnimation(aniFade2);
        cur_index = (cur_index+urls.length-1)%urls.length;
    }
}
