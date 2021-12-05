package com.example.weatherapp.camera;

import android.graphics.Bitmap;

public class Photo {

    private int id;
    private String fileName;
    private Bitmap bitmap;
    private String info;
    private String filePath;//大图的保存路径

    public Photo() {
        this.id=-1;
        this.fileName=null;
        this.bitmap=null;
        this.info=null;
        this.filePath=null;
    }

    public Photo(int id, String fileName, Bitmap bitmap, String info, String filePath) {
        this.id = id;
        this.fileName = fileName;
        this.bitmap = bitmap;
        this.info = info;
        this.filePath = filePath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
