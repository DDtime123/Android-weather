package com.example.weatherapp.application;


import android.app.Application;
import android.content.Context;

// 静态单例获取Context
public class MyApplication extends Application {
    private static Context myContext;

    @Override
    public void onCreate() {
        super.onCreate();
        MyApplication.myContext=getApplicationContext();
    }

    public static Context getContext(){
        return MyApplication.myContext;
    }
}
