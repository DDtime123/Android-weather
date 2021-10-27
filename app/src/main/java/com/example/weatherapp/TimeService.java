package com.example.weatherapp;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.text.format.DateFormat;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.Date;

public class TimeService extends Service {
    public TimeService() {
    }

    public void sendMessage(Message msg){
        Intent intent = new Intent("time-message");
        intent.putExtra("time-msg",msg);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 开启线程来更新时间
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while(true) {
                        Date dt = new Date();
                        int hours = dt.getHours();
                        int minutes = dt.getMinutes();
                        int seconds = dt.getSeconds();
                        String curTime = hours + ":" + minutes + ":" + seconds;
                        // 通过广播将时间传输回去
                        Message msg = new Message();
                        Bundle bundle = new Bundle();
                        bundle.putString("date", DateFormat.format("yyyy-MM-dd", dt).toString());
                        bundle.putString("curTime", curTime);
                        msg.setData(bundle);
                        sendMessage(msg);
                        Thread.sleep(1000);
                    }
                }catch (Exception e){

                }
            }
        }).start();
//        // 获取一个 AlarmManager 实例
//        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        // 设置一个定时任务
//        int temSec = 1 * 1000; // 1秒钟的毫秒数
//        long triggerAtTime = SystemClock.elapsedRealtime() + temSec; // 1秒钟
//        Intent i = new Intent(this, TimeService.class);
//        PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
//        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime, pi);
        return super.onStartCommand(intent, flags, startId);
    }
}
