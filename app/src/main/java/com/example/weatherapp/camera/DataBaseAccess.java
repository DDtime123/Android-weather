package com.example.weatherapp.camera;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseAccess extends SQLiteOpenHelper {
    public DataBaseAccess(Context context, String name,int version){
        super(context,name,null,version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqldel = "drop table photo";
        db.execSQL(sqldel);
        String sql = "create table photo(\n " +
                " id integer primary key autoincrement ,\n" +
                " name text,\n" +
                " info text,\n" +
                " filePath text\n" +
                ")";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
