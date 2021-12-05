package com.example.weatherapp.camera;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ImageFileAccess {
    Context context;
    public ImageFileAccess(Context context){
        this.context=context;
    }
    //保存图片
    public void saveImageFile(String fileName,Bitmap bitmap){
        try {
            FileOutputStream fos=context.openFileOutput(fileName,MODE_PRIVATE);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,fos);
            Toast.makeText(context,"保存成功",Toast.LENGTH_SHORT).show();
            fos.flush();
            fos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //读取图片，返回Bitmap
    public Bitmap readImageFile(String fileName){
        try {
            FileInputStream fis=context.openFileInput(fileName);
            Bitmap bitmap= BitmapFactory.decodeStream(fis);
            return bitmap;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
    //删除文件
    public void delFile(String fileName){
        String path=context.getFilesDir().getPath();
        String filePath=path+"/"+fileName;
        File file=new File(filePath);
        file.delete();
    }
    //照相时生成一张照片的路径
    public String getImageFile(){
        String path=context.getFilesDir().getPath();
        DateFormat sdf = new SimpleDateFormat("yyyyMMdd_hhmmss");
        String myDate = sdf.format(new Date());
        String mFilePath = path + "/" + myDate+".jpg";
        //Uri uri = Uri.fromFile(new File(mFilePath));
        return mFilePath;
    }
}

