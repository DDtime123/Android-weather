package com.example.weatherapp.camera;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class PhotoDAO {
    SQLiteDatabase sqLiteDatabase=null;
    Context context;
    PhotoDAO(Context context){
        this.context=context;
    }

    //构造方法
    public void  connect(){
        DataBaseAccess dba=new DataBaseAccess(context,"photoDb",1);
        sqLiteDatabase=dba.getReadableDatabase();
    }

    //图片插入
    public long insert(Photo photo){
        connect();
        ContentValues values=new ContentValues();
        values.put("name",photo.getFileName());
        values.put("info",photo.getInfo());
        values.put("filePath",photo.getFilePath());
        //values.put("bitmap",photo.getBitmap());
        long n=sqLiteDatabase.insert("photo",null,values);
        Toast.makeText(context, "插入成功"+n, Toast.LENGTH_SHORT).show();
        sqLiteDatabase.close();
        return n;
    }
//图片删除

    public void delete(int id){

        connect();
        String sql="delete from photo where id="+id; //注意没有delete * from
        sqLiteDatabase.execSQL(sql);
        Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
    }

//图片查询

    public List<Photo> findPhoto(){

        connect();
        List<Photo> photoList=new ArrayList<>();
        String sql="select * from photo";
        Cursor cursor=sqLiteDatabase.rawQuery(sql,null);
        while(cursor.moveToNext()){
            int id=cursor.getInt(cursor.getColumnIndex("id"));
            String name=cursor.getString(cursor.getColumnIndex("name"));
            String info=cursor.getString(cursor.getColumnIndex("info"));
//            String filePath=cursor.getString(cursor.getColumnIndex("filePath"));
//            Log.d("PhotoDap", "filePath: "+filePath);
            Log.d("PhotoDap", "id"+id+"name"+name+"info"+info);
            Photo p=new Photo();
            p.setId(id);
            p.setFileName(name);
            p.setInfo(info);
//            p.setFilePath(filePath);
            photoList.add(p);
        }
        sqLiteDatabase.close();
        return photoList;
    }
}
