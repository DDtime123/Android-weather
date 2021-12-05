package com.example.weatherapp.Student;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weatherapp.R;
import com.example.weatherapp.fragment.EditFormDIalogFragment;
import com.example.weatherapp.model.Stu;
import com.example.weatherapp.net.StuClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class StuDetailActivity extends AppCompatActivity implements View.OnClickListener , EditFormDIalogFragment.EditNameDialogListener {
    private ImageView ivStuCover;
    private static final String TAG = "StuDetailActivity";
    /*****************************获取FragmentDialog传递回的信息时的操作****************************/
    // 实现接口 EditNameDialogListener 的方法
    @Override
    public void onFinishEditDialog(Bundle message) {
        /**
         * @Bundle message
         * message 有 name email 等从DialogFrame传回的信息
         * */
        Toast.makeText(this,"Hi"+message.getString("name")+message.getString("email"),Toast.LENGTH_SHORT).show();
    }
    /***********************************************************************************************/
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.stu_update:{
                // 更新 DialogFragment

                FragmentManager fm = getSupportFragmentManager();
                EditFormDIalogFragment editFormDIalogFragment = EditFormDIalogFragment.newInstance("Some Title");
                editFormDIalogFragment.show(fm, "fragment_edit_name");

                // 创建警告对话框
                /**
                FragmentManager fm = getSupportFragmentManager();
                MyAlertDialogFragment myAlertDialogFragment = MyAlertDialogFragment.newInstance("Some Titie");
                myAlertDialogFragment.show(fm, "my_alert_fragment");
                 */
            }
                break;
            default:
                break;
        }
    }



    private TextView tvStuName;
    private TextView tvStuEmail;
    private TextView tvStuPublisher;
    private TextView tvStuPageCount;
    private StuClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stu_detail);

        // 获取视图
        ivStuCover = (ImageView) findViewById(R.id.ivStuCover);
        tvStuName = (TextView) findViewById(R.id.tvStuName);
        tvStuEmail = (TextView) findViewById(R.id.tvStuEmail);
        tvStuPublisher = (TextView) findViewById(R.id.tvStuPublisher);
        tvStuPageCount = (TextView) findViewById(R.id.tvStuPageCount);
        // Use the book to populate the data into our views
        Stu stu = (Stu) getIntent().getSerializableExtra(StuListActivity.STU_DETAIL_KEY);
        loadStu(stu);

        Button stu_update = (Button)findViewById(R.id.stu_update);
        stu_update.setOnClickListener(this);
    }

    // 为学生填充数据
    private void loadStu(Stu stu) {
        //更改活动标题
        this.setTitle(stu.getName());
        // 填充数据
        Picasso.with(this).load(Uri.parse(stu.getLargeCoverUrl())).error(R.drawable.ic_nocover).into(ivStuCover);
        tvStuName.setText(stu.getName());
        tvStuEmail.setText(stu.getEmail());
        // 从书籍 API 中获取额外的书籍数据
        client = new StuClient();
        client.getExtraStuDetails(stu.getId(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    Log.d(TAG, "on Detail search Id "+ response.getString("id"));
                    if (response.has("updateDate")) {
                        // 显示以逗号分隔的出版商列表
                        String updateDate = response.getString("updateDate");
//                        final int numPublishers = updateDate.length();
//                        final String[] publishers = new String[numPublishers];
//                        for (int i = 0; i < numPublishers; ++i) {
//                            publishers[i] = updateDate.getString(i);
//                        }
                        tvStuPublisher.setText("Last Update: "+updateDate);
                    }
                    if (response.has("number_of_pages")) {
                        tvStuPageCount.setText(Integer.toString(response.getInt("number_of_pages")) + " pages");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 给菜单充气； 如果它存在，这会将项目添加到操作栏。
        getMenuInflater().inflate(R.menu.menu_stu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // 在此处处理操作栏项目的点击。 操作栏将
        // 自动处理点击 Home/Up 按钮，这么久
        // 当您在 AndroidManifest.xml 中指定父活动时。
        int id = item.getItemId();

        if (id == R.id.stu_action_share) {
            setShareIntent();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setShareIntent() {
        ImageView ivImage = (ImageView) findViewById(R.id.ivStuCover);
        final TextView tvName = (TextView)findViewById(R.id.tvStuName);
        // 访问位图的 URI
        Uri bmpUri = getLocalBitmapUri(ivImage);
        // 构造一个带有图像链接的 ShareIntent
        Intent shareIntent = new Intent();
        // 构造一个带有图像链接的 ShareIntent
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("*/*");
        shareIntent.putExtra(Intent.EXTRA_TEXT, (String)tvName.getText());
        shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
        // 启动共享菜单
        startActivity(Intent.createChooser(shareIntent, "Share Image"));

    }

    // 返回显示在封面图像视图中的位图的 URI 路径
    public Uri getLocalBitmapUri(ImageView imageView) {
        // 从 ImageView drawable 中提取位图
        Drawable drawable = imageView.getDrawable();
        Bitmap bmp = null;
        if (drawable instanceof BitmapDrawable){
            bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        } else {
            return null;
        }
        // 将图像存储到默认的外部存储目录
        Uri bmpUri = null;
        try {
            File file =  new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS), "share_image_" + System.currentTimeMillis() + ".png");
            file.getParentFile().mkdirs();
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }
}
