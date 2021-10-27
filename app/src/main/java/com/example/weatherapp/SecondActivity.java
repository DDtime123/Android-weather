package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SecondActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "SecondActivity";

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

            }
                break;
            case R.id.music:{

            }
                break;
            case R.id.position:{

            }
                break;
            case R.id.student:{

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
    }



}















