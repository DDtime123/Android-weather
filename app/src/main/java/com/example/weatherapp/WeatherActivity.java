package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;


import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {
    private static final String TAG = "WeatherActivity";
    TextView responseContent;

    public static final int UPDATE_TEXT = 1;

    // 消息回调函数
    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case UPDATE_TEXT:
                    // 可以在此处进行 UI 操作
                    responseContent.setText("msg.city: "+msg.getData().getString("city"));
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        responseContent = (TextView) findViewById(R.id.WebContent);
        sendRequestWithHttpURLConnection();
    }

    public void sendRequestWithHttpURLConnection(){
        // 开启线程来发起网络请求
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    // 要想发起请求，必须创建请求对象
                    Request request = new Request.Builder()
                            .url("https://www.tianqiapi.com/free/day?appid=31225177&appsecret=eqIA7pd6&unescape=1")
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
                    handler.sendMessage(msg);

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void showResponse(final String response){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // 进行 UI 操作，将结果显示在界面上
                responseContent.setText(response);
            }
        });
    }

    private Bundle parseJSONWithJSONObject(String jsonData) {
        Bundle bundle = new Bundle();
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            final String city = jsonObject.getString("city");
            final String wea = jsonObject.getString("wea");
            final String wea_img = jsonObject.getString("wea_img");
            final String tem = jsonObject.getString("tem");

            final String update_time = jsonObject.getString("update_time");

            bundle.putString("city",city);
            bundle.putString("wea",wea);
            bundle.putString("wea_img",wea_img);
            bundle.putString("tem",tem);
            bundle.putString("update_time",update_time);

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
