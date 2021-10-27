package com.example.weatherapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import org.json.JSONObject;

import java.util.Date;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LongRunningService extends Service {
    private static final String TAG = "LongRunningService";

    public static final int UPDATE_TEXT = 1;
    private Handler handler = null;

    public void sendMessage(Message msg){
        Intent intent = new Intent("my-message");
        intent.putExtra("my-msg",msg);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    public void getHandler(Handler handler){
        this.handler = handler;
    }

    
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: ");
        // 开启线程来发起网络请求
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    // 要想发起请求，必须创建请求对象
                    Request request = new Request.Builder()
                            .url("https://tianqiapi.com/api?unescape=1&version=v6&appid=31225177&appsecret=eqIA7pd6")
                            .build();
                    // 调用 OkHttpClient 的 newCall() 方法来创建一个 Call 对象，
                    // 并调用它的 execute 方法来发送请求并获取服务器返回的数据
                    Response response = client.newCall(request).execute();
                    // 获取 response 中的内容，其中 body 为响应正文
                    String responseData = response.body().string();
                    Log.d(TAG, responseData);

                    // 接下来将响应正文解析成 JSON 格式数据
                    Bundle bundle = parseJSONWithJSONObject(responseData);
                    // 进行消息回调，将 JSON 文本传递会主线程
                    Message msg = new Message();
                    msg.what = UPDATE_TEXT;
                    msg.setData(bundle);
                    //handler.sendMessage(msg);
                    sendMessage(msg);

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
        // 获取一个 AlarmManager 实例
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        // 设置一个定时任务
        int temSec = 20 * 1000; // 20秒钟的毫秒数
        long triggerAtTime = SystemClock.elapsedRealtime() + temSec; // 20秒钟
        Intent i = new Intent(this, LongRunningService.class);
        PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime, pi);
        return super.onStartCommand(intent, flags, startId);
    }

    // 解析 JSON
    private Bundle parseJSONWithJSONObject(String jsonData) {
        Bundle bundle = new Bundle();
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            final String city = jsonObject.getString("city");
            final String wea = jsonObject.getString("wea");
            final String wea_img = jsonObject.getString("wea_img");
            final String tem = jsonObject.getString("tem");
            final String date = jsonObject.getString("date");
            final String week = jsonObject.getString("week");
            final String air = jsonObject.getString("air");
            final String air_level = jsonObject.getString("air_level");

            final String update_time = jsonObject.getString("update_time");

            bundle.putString("city",city);
            bundle.putString("wea",wea);
            bundle.putString("wea_img",wea_img);
            bundle.putString("tem",tem);
            bundle.putString("update_time",update_time);
            bundle.putString("date",date);
            bundle.putString("week",week);
            bundle.putString("air",air);
            bundle.putString("air_level",air_level);

            Log.d(TAG, "城市: "+city);
            Log.d(TAG, "天气: "+wea);
            Log.d(TAG, "天气图片: "+wea_img);
            Log.d(TAG, "温度: "+tem);
            Log.d(TAG, "小时: "+update_time);

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            return bundle;
        }
    }
}
