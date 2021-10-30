package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class InfoDialog extends AppCompatActivity implements View.OnClickListener {

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.Accept:{
                Intent intent = new Intent().putExtra("AcceptOrCancel",true);
                setResult(RESULT_OK,intent);
                finish();
            }
                break;
            case R.id.Cancel:{
                Intent intent = new Intent().putExtra("AcceptOrCancel",false);
                setResult(RESULT_OK,intent);
                finish();
            }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_dialog);
        Button accept = (Button) findViewById(R.id.Accept);
        Button cancel = (Button) findViewById(R.id.Cancel);

        accept.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }
}
