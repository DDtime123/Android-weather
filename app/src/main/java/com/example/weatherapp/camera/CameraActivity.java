package com.example.weatherapp.camera;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weatherapp.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class CameraActivity extends AppCompatActivity {

    private static final String TAG = "CameraActivity";
    ImageSwitcher imageSwitcher;
    ImageView btn_camera;
    TextView pic_name;
    TextView pic_info;
    ListView photoList;
    ImageFileAccess imageFileAccess;
    String filePath;
    public static final int REQUEST_CAMERA = 10;
    PhotoDAO photoDAO;
    List<Photo> photos;
    PhotoAdapter photoAdapter;
    float startX;
    float startY;
    float offsetX;
    float offsetY;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        imageSwitcher = (ImageSwitcher) findViewById(R.id.imageShow);
        btn_camera = (ImageView) findViewById(R.id.btn_camera);
        pic_name = (TextView) findViewById(R.id.pic_name);
        pic_info = (TextView) findViewById(R.id.pic_info);
        photoList = (ListView) findViewById(R.id.photoList);
        imageFileAccess = new ImageFileAccess(CameraActivity.this);
        photoDAO = new PhotoDAO(CameraActivity.this);
        photos = new ArrayList<Photo>();

        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                filePath=imageFileAccess.getImageFile();
                File file = new File(filePath);
                Uri uri = FileProvider.getUriForFile(CameraActivity.this.getApplicationContext(),
                        "com.example.cameraalbumtest.fileprovider",file);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.putExtra(MediaStore.EXTRA_OUTPUT,uri);
                startActivityForResult(intent,REQUEST_CAMERA);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CAMERA) {//请求码是获得照片
            if (resultCode == RESULT_OK) {//从URI中获得图片
                View infoInputView = View.inflate(this, R.layout.mydialog, null);
                final EditText name_edit = infoInputView.findViewById(R.id.pName);
                final EditText info_edit = infoInputView.findViewById(R.id.pInfo);
                File file = new File(filePath);
                Uri uri = FileProvider.getUriForFile(CameraActivity.this.getApplicationContext(),
                        "com.example.cameraalbumtest.fileprovider", file);
                try {
                    final Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                    info_edit.setHint("信息");
                    AlertDialog.Builder myDialog = new AlertDialog.Builder(CameraActivity.this);
                    myDialog.setTitle("描述一下");
                    myDialog.setIcon(R.drawable.ic_contact);
                    myDialog.setView(infoInputView);
                    myDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String name = name_edit.getText().toString();
                            String info = info_edit.getText().toString();
                            Photo photo = new Photo();
                            photo.setFileName(name);
                            photo.setBitmap(bitmap);
                            photo.setInfo(info);
                            photo.setFilePath(filePath);
                            Log.d(TAG, "filePath："+filePath);
                            long i = photoDAO.insert(photo);//保存图片信息到数据库
                            //利用数据库数据刷新
                            photos = photoDAO.findPhoto();
                            initPhoto(photos);
                        }
                    });
                    myDialog.setNegativeButton("取消", null);
                    myDialog.show();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void initPhoto(List<Photo> pList){
        List<Photo> photos = new ArrayList<>();

        for(Photo p : pList){
            String filePath = p.getFilePath();
            Log.d(TAG, filePath);
            Toast.makeText(this.getApplicationContext(),filePath, Toast.LENGTH_SHORT).show();
            File file = new File(filePath);
            Uri uri = FileProvider.getUriForFile(CameraActivity.this.getApplicationContext(),
                    "com.example.cameraalbumtest.fileprovider", file);

            try{
                Bitmap b = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                p.setBitmap(b);
                //Toast.makeText(this.getApplicationContext(),"p.setBitmap", Toast.LENGTH_SHORT).show();
                photos.add(p);
            }catch (FileNotFoundException e){
                e.printStackTrace();
            }
        }
        photoAdapter = new PhotoAdapter(CameraActivity.this,photos);
        photoList.setAdapter(photoAdapter);
    }


    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                startX=event.getX();
                startY=event.getY();
                break;
            case MotionEvent.ACTION_UP:
                offsetX=event.getX()-startX;
                offsetY=event.getY()-startY;
                if(Math.abs(offsetX)>Math.abs(offsetY)){
                    if(offsetX<-5){
                        swipeLeft();
                    }else if(offsetX>5){
                        swipeRight();
                    }
                }else{
                    if(offsetY<-5){
                        //swipeUp();
                        swipeLeft();
                    }else if(offsetY>5){
                        //swipeDown();
                        swipeRight();
                    }
                }

        }
        return true;
    }


    //@Override
    public View makeView() {
        ImageView imageView=new ImageView(this);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        //设置布局填充父亲
        imageView.setLayoutParams(new ImageSwitcher.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                , ViewGroup.LayoutParams.MATCH_PARENT));
        return imageView;
    }

    private void loadPic(Photo p){
        String filePath=p.getFilePath();
        String name=p.getFileName();
        String info=p.getInfo();
        pic_name.setText(name);
        pic_info.setText(info);
        imageSwitcher.setImageURI(Uri.parse(filePath));
    }
    private void swipeLeft() {//左边滑出，右边滑入
        System.out.println("left");
        position--;
        if (position < 0) {
            position = photos.size()-1;
        }
        /**
        imageSwitcher.setInAnimation(this, R.anim.right_in);
        imageSwitcher.setOutAnimation(this, R.anim.left_out);
         */
        Photo p=photos.get(position);
        loadPic(p);
    }
    private void swipeRight() {//右边滑出，左边滑入
        System.out.println("right");
        position++;
        if (position >photos.size() - 1) {
            position = 0;
        }
        /**
        imageSwitcher.setInAnimation(this, R.anim.left_in);
        imageSwitcher.setOutAnimation(this, R.anim.right_out);
         */
        Photo p=photos.get(position);
        loadPic(p);
    }

}