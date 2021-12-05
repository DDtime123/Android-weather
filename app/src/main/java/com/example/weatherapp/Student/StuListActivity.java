package com.example.weatherapp.Student;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;

import com.example.weatherapp.R;
import com.example.weatherapp.adapters.StuAdapter;
import com.example.weatherapp.model.Stu;
import com.example.weatherapp.net.StuClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;


public class StuListActivity extends AppCompatActivity {
    public static final String STU_DETAIL_KEY = "stu";
    private ListView lvStus;
    private StuAdapter stuAdapter;
    private StuClient client;
    private ProgressBar progress;
    private static final String TAG = "StuListActivity";

    /*********************************存储方式——文件*******************************************/
    public void save(){
        BufferedWriter writer = null;
        FileOutputStream out = null;
        try {
            out = openFileOutput("file.txt", Context.MODE_PRIVATE);
            writer = new BufferedWriter(new OutputStreamWriter(out));
            writer.write(StuListActivity.this.getTitle().toString());
            out.close();
        }catch (IOException ex){
            ex.printStackTrace();
        }finally {
            try{
                if(writer!=null){
                    writer.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        save();
        super.onDestroy();
    }
    /********************************************************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stu_list);
        lvStus = (ListView) findViewById(R.id.lvStus);
        ArrayList<Stu> aStus = new ArrayList<Stu>();
        // 初始化适配器
        stuAdapter = new StuAdapter(this, aStus);
        // 将适配器绑定到listview
        lvStus.setAdapter(stuAdapter);
        progress = (ProgressBar) findViewById(R.id.stu_progress);
        setupStuSelectedListener();
    }

    public void setupStuSelectedListener() {
        lvStus.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemClick: ");
                // Launch the detail view passing book as an extra
                Intent intent = new Intent(StuListActivity.this, StuDetailActivity.class);
                intent.putExtra(STU_DETAIL_KEY, stuAdapter.getItem(position));
                startActivity(intent);
            }
        });

    }

    // 执行对OpenLibrary搜索端点的API调用，解析结果
    // 将它们转换为学生对象数组，并将它们添加到适配器
    private void fetchStus(String query) {
        // 在发出网络请求前显示进度条
        progress.setVisibility(ProgressBar.VISIBLE);
        client = new StuClient();
        client.getStus(query, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                    // 隐藏进度条
                    progress.setVisibility(ProgressBar.GONE);
                    JSONArray docs = null;
                    if(response != null) {
                        //获取docs json数组
                        // docs = response.getJSONArray(“文档”);
                        docs = response;
                        // 将json数组转换为学生数组
                        final ArrayList<Stu> stus =Stu.fromJson(docs);
                        // 清空适配器中的学生对象
                        stuAdapter.clear();
                        // 将模型导入适配器
                        for (Stu stu : stus) {
                            stuAdapter.add(stu); // 通过适配器添加stu
                            Log.d(TAG, stu.getName());
                        }
                        stuAdapter.notifyDataSetChanged();
                    }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                progress.setVisibility(ProgressBar.GONE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 膨胀的菜单;这将添加项目到操作栏，如果它存在。
        getMenuInflater().inflate(R.menu.menu_stu_list, menu);
        final MenuItem searchItem = menu.findItem(R.id.stu_action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // 远程获取数据
                fetchStus(query);
                // 重置 searchView
                searchView.clearFocus();
                searchView.setQuery("", false);
                searchView.setIconified(true);
                searchItem.collapseActionView();
                // 设置活动的查询的标题
                StuListActivity.this.setTitle(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //处理操作栏项目点击这里。操作栏会
        //自动处理Home/Up按钮的点击，so long
        //当你在AndroidManifest.xml中指定父activity时。
        int id = item.getItemId();

        //List列表中的查询item
        if (id == R.id.stu_action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
