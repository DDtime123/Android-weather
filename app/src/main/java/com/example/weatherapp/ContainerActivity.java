package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.weatherapp.model.UI1;
import com.example.weatherapp.model.UI2;
import com.example.weatherapp.model.UI3;
import com.example.weatherapp.model.UI4;

public class ContainerActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "ContainerActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);

        Button ToImageList = (Button)findViewById(R.id.ToImageList);
        Button ToImageSpin = (Button)findViewById(R.id.ToImageSpin);
        Button ToCalculator = (Button)findViewById(R.id.ToCalculator);
        Button ToWordAlign = (Button)findViewById(R.id.ToWordAlign);
        Button ToFrameLayout = (Button)findViewById(R.id.ToFrameLayout);
        Button ToUI2 = (Button)findViewById(R.id.ToUI2);
        Button ToUI4 = (Button)findViewById(R.id.ToUI4);
        Button ToBeautifulButton = (Button)findViewById(R.id.ToBeautifulButton);
        Button ToCheckBox = (Button)findViewById(R.id.ToCheckBox);

        ToImageList.setOnClickListener(this);
        ToImageSpin.setOnClickListener(this);
        ToCalculator.setOnClickListener(this);
        ToWordAlign.setOnClickListener(this);
        ToFrameLayout.setOnClickListener(this);
        ToUI2.setOnClickListener(this);
        ToUI4.setOnClickListener(this);
        ToBeautifulButton.setOnClickListener(this);
        ToCheckBox.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ToImageList:{
                startActivity(new Intent(ContainerActivity.this,ImageListActivity.class));
            }
            break;
            case R.id.ToImageSpin:{
                startActivity(new Intent(ContainerActivity.this,ImageSpin.class));
            }
            break;
            case R.id.ToCalculator:{
                startActivity(new Intent(ContainerActivity.this, UI1.class));
            }
            break;
            case R.id.ToWordAlign:{
                startActivity(new Intent(ContainerActivity.this, UI4.class));
            }
            break;
            case R.id.ToFrameLayout:{
                startActivity(new Intent(ContainerActivity.this, UI3.class));
            }
            break;
            case R.id.ToUI2:{
                startActivity(new Intent(ContainerActivity.this, UI2.class));
            }
            break;
            case R.id.ToUI4:{
                startActivity(new Intent(ContainerActivity.this, UI4.class));
            }
            break;
            case R.id.ToBeautifulButton:{
                startActivity(new Intent(ContainerActivity.this, ButtonActivity.class));
            }
            break;
            case R.id.ToCheckBox:{
                startActivity(new Intent(ContainerActivity.this, CheckboxActivity.class));
            }
            break;
            default:
                break;
        }
    }
}
