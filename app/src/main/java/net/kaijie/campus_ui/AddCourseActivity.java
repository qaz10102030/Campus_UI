package net.kaijie.campus_ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rabbit on 2017/10/5.
 */
public class AddCourseActivity extends AppCompatActivity implements
        SearchView.OnQueryTextListener,
        ListView.OnItemClickListener{

    private SharedPreferences settings;
    private static final String marker_data = "DATA";
    private static final String isCourseDataReady = "isCourseDataReady";
    private ListView lvCourse;
    private SearchView searchView;
    private CourseAdapter courseAdapter;
    private List<Course> addCourse;
    private Parcelable parcelable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.add_course_activity);
        settings = getSharedPreferences(marker_data,0);
        boolean courseReady = settings.getBoolean(isCourseDataReady,false);
        if(!courseReady){

        }
        initView();
        initCourse();
    }

    private void initView() {
        lvCourse = (ListView)findViewById(R.id.lvCourse);
        courseAdapter = new CourseAdapter(AddCourseActivity.this,R.layout.add_course_item);
        lvCourse.setAdapter(courseAdapter);
        lvCourse.setOnItemClickListener(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        View actionbarLayout = LayoutInflater.from(this).inflate(
                R.layout.actionbar_layout, null);
        getSupportActionBar().setCustomView(actionbarLayout);
        searchView = (SearchView) actionbarLayout.findViewById(R.id.searchView);
        searchView.setIconifiedByDefault(true);
        searchView.setOnQueryTextListener(this);
        searchView.setFocusable(false);
        searchView.requestFocusFromTouch();
        searchView.setSubmitButtonEnabled(false);
        searchView.setQueryHint("找甚麼呢?!");
    }

    private void initCourse() {
        addCourse = new ArrayList<>();
        SQLiteDatabase db = SQLiteDatabase.openDatabase(getFilesDir() + "class_database.db", null,SQLiteDatabase.OPEN_READONLY);
        String selectCourse = "select * from tb_course";
        try {
            Cursor cursor = db.rawQuery(selectCourse,null);
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                Course tempCourse = new Course();
                tempCourse.setday(cursor.getInt(12))
                        .setname(cursor.getString(1))
                        .setrequire(cursor.getString(9))
                        .setschedule_display(cursor.getString(13))
                        .setserial(cursor.getString(3))
                        .setteacher(cursor.getString(8))
                        .setclassfor(cursor.getString(5));
                courseAdapter.add(tempCourse);
                cursor.moveToNext();
            }
            courseAdapter.notifyDataSetChanged();
            cursor.close();
            Log.d("SQLite","course data is ready");
        }
        catch (SQLiteException e)
        {
            Log.d("SQLite",e.getMessage());
        }
        db.close();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if(newText.trim().length() > 0)
        courseAdapter.getFilter().filter(newText);
        return true;
    }

    @Override
    protected void onDestroy() {
        if(addCourse.size() > 0)
        {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("list", (ArrayList<? extends Parcelable>) addCourse);
            intent.putExtra("addCourse",bundle);
            setResult(RESULT_OK, intent);
        }
        super.onDestroy();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            ArrayList<String> result_dialog = new ArrayList<>(); //宣告動態陣列，用來存課程項目跟資料組合後的字串
            LayoutInflater inflater = LayoutInflater.from(AddCourseActivity.this); //LayoutInflater的目的是將自己設計xml的Layout轉成View
            View class_view = inflater.inflate(R.layout.class_msg, null); //指定要給View表述的Layout
            ListView into_class = (ListView) class_view.findViewById(R.id.into_class); //定義顯示課程資訊的清單物件
            ArrayAdapter ClassInfo = new ArrayAdapter(AddCourseActivity.this, android.R.layout.simple_list_item_1);//設定課程資訊的清單物件要顯示資料的陣列
            into_class.setAdapter(ClassInfo); //定義顯示課程資訊的清單物件
            final Course displayCourse = (Course) parent.getItemAtPosition(position);
            ClassInfo.clear(); //先把清單物件的資料陣列清空
            result_dialog.clear(); //再把要存組合字串的陣列內容清空
            result_dialog.add("上課教室：" + displayCourse.getroom());
            result_dialog.add("課號：" + displayCourse.getserial());
            result_dialog.add("課程名稱：" + displayCourse.getname() +"\n( "+ displayCourse.getname_eng()+ " )");
            result_dialog.add("上課節數：" + displayCourse.getSpanNum());
            result_dialog.add("開課班級：" + displayCourse.getclass_for());
            result_dialog.add("修別：" + displayCourse.getrequire() +"( "+ displayCourse.getrequire_eng()+ " )");
            result_dialog.add("學分組合：" + displayCourse.getcredits()+"\n( 講授時數-實習時數-學分數 )");
            result_dialog.add("授課老師：" + displayCourse.getteacher()+"老師");
            for(int a = 0;a<result_dialog.size();a++) //把陣列內的資料丟給清單顯示
            {
                ClassInfo.add(result_dialog.get(a)); //將資料加到陣列裡
                ClassInfo.notifyDataSetChanged(); //通知陣列資料有被更改
                into_class.smoothScrollToPosition(ClassInfo.getCount() - 1); //滑動到最後一項(如果超出畫面)
            }

            new AlertDialog.Builder(AddCourseActivity.this) //宣告對話框物件，並顯示課程資料
                    .setTitle("詳細資料")
                    .setView(class_view)
                    .setNegativeButton("新增", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(AddCourseActivity.this, "已新增", Toast.LENGTH_SHORT).show();
                            addCourse.add(displayCourse);
                        }
                    })
                    .setPositiveButton("離開", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).show();
        }
        catch (Exception e){
            Log.e("dialog",e.toString());
        }
    }
}
