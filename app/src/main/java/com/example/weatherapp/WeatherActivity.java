package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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
                            .url("https://tianqiapi.com/api?unescape=1&version=v1&appid=31225177&appsecret=eqIA7pd6")
                            .build();
                    // 调用 OkHttpClient 的 newCall() 方法来创建一个 Call 对象，
                    // 并调用它的 execute 方法来发送请求并获取服务器返回的数据
                    Response response = client.newCall(request).execute();
                    // 获取 response 中的内容，其中 body 为响应正文
                    String responseData = response.body().string();
                    Log.d(TAG, responseData);
                    // 接下来将响应正文解析成 JSON 格式数据
                    parseJSONWithJSONObject(responseData);

                }catch (Exception e){

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

    private void parseJSONWithJSONObject(String jsonData) {
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String city = jsonObject.getString("cityid");
                Log.d(TAG, "city is " + city);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
