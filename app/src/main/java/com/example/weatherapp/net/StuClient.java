package com.example.weatherapp.net;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class StuClient {
    private static final String TAG = "StuClient";
    private static final String API_BASE_URL = "http://192.168.43.231:8088/";
    private AsyncHttpClient client;

    public StuClient() {
        this.client = new AsyncHttpClient();
    }

    private String getApiUrl(String relativeUrl) {
        return API_BASE_URL + relativeUrl;
    }

    // stu 搜索 API
    public void getStus(final String query, JsonHttpResponseHandler handler) {
        try {
            //String url = getApiUrl("search.json?q=");
            String url = getApiUrl("public/queryStus");
            client.get(url + "?name=" + URLEncoder.encode(query, "utf-8"), handler);
            Log.d(TAG, url + "?name=" + URLEncoder.encode(query, "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    // Method for accessing books API to get publisher and no. of pages in a book.
    public void getExtraStuDetails(String stuId, JsonHttpResponseHandler handler) {
        String url = getApiUrl("public/findStus");
        client.get(url +  "?id=" + stuId , handler);
    }
}