package com.example.weatherapp.model;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class Stu implements Serializable {
    /*****************************************************************/
    private String name;
    private String id;
    private String email;
    private String createDate;
    private String updateDate;

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getCreateDate() {
        return createDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    // 从 API 提供的 URL 中获取中等大小的书籍封面
    public String getCoverUrl(){
        return "https://user.helpforwriters.me/cart/image/cache/catalog/medium%20book-500x500.jpg";
    }

    // 从 API 提供的 URL 中获取大型的书籍封面
    public String getLargeCoverUrl(){
        return "https://tse4-mm.cn.bing.net/th/id/OIP-C.AcklgBTQlnx1GZGTaiJvhAHaFX?pid=ImgDet&rs=1";
    }

    /***********************************************************************/
    private String openLibraryId;
    private String author;
    private String title;

    public String getOpenLibraryId() {
        return openLibraryId;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }



    // 将 JSON 格式的书籍信息转换为书籍对象
    public static Stu fromJson(JSONObject jsonObject){
        Stu stu = new Stu();
        try {
            // Deserialize json into object fields
            // Check if a cover edition is available
            stu.id = jsonObject.has("id") ? jsonObject.getString("id") : "";
            stu.name = jsonObject.has("name") ? jsonObject.getString("name") : "";
            stu.email = jsonObject.has("email") ? jsonObject.getString("email") : "";
            stu.createDate = jsonObject.has("createDate") ? jsonObject.getString("createDate") : "";
            stu.updateDate = jsonObject.has("updateDate") ? jsonObject.getString("updateDate") : "";
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        // 返回 stu 对象
        return stu;
    }


    // Decodes array of book json results into business model objects
    public static ArrayList<Stu> fromJson(JSONArray jsonArray) {
        ArrayList<Stu> stus = new ArrayList<Stu>(jsonArray.length());
        // Process each result in json array, decode and convert to business
        // object
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject stuJson = null;
            try {
                stuJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            Stu stu = Stu.fromJson(stuJson);
            if (stu != null) {
                stus.add(stu);
            }
        }
        return stus;
    }
}
