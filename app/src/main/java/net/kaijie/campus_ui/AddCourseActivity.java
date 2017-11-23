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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rabbit on 2017/10/5.
 */
public class AddCourseActivity extends AppCompatActivity implements
        SearchView.OnQueryTextListener,
        ListView.OnItemClickListener{

    private static final String marker_data = "DATA";
    private static final String isCourseDataReady = "isCourseDataReady";
    private CourseAdapter courseAdapter;
    private List<Course> addCourse;
    private List<Course> userCourse = new ArrayList<>();
    private int[][] classtable = new int[7][16];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_course_activity);
        SharedPreferences settings = getSharedPreferences(marker_data, 0);
        boolean courseReady = settings.getBoolean(isCourseDataReady,false);
        if(!courseReady){
            Toast.makeText(AddCourseActivity.this,"無課程資料無法選課",Toast.LENGTH_SHORT).show();
            finish();
        }
       userCourse = getIntent().getBundleExtra("userCourse").getParcelableArrayList("userCourseList");
        for(int course_size = 0; course_size<userCourse.size(); course_size++) {


            int addspan = userCourse.get(course_size).getschedule()+ userCourse.get(course_size).getSpanNum()- 1;
            for (int spnum = userCourse.get(course_size).getschedule(); spnum <= addspan; spnum++) {
                classtable[userCourse.get(course_size).getday()-1][spnum-1] = 1;
            }
        }
        initView();
        initCourse();
    }

    private void initView() {
        ListView lvCourse = (ListView) findViewById(R.id.lvCourse);
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
        SearchView searchView = (SearchView) actionbarLayout.findViewById(R.id.searchView);
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
                tempCourse.setname(cursor.getString(1))
                        .setname_eng(cursor.getString(2))
                        .setserial(cursor.getString(3))
                        .setroom(cursor.getString(4))
                        .setclassfor(cursor.getString(5))
                        .setschedule(cursor.getInt(6))
                        .setSpanNum(cursor.getInt(7))
                        .setteacher(cursor.getString(8))
                        .setrequire(cursor.getString(9))
                        .setrequire_eng(cursor.getString(10))
                        .setcredits(cursor.getString(11))
                        .setday(cursor.getInt(12))
                        .setschedule_display(cursor.getString(13));

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

    private boolean checkCourseCollision(Course targetCourse){
        int targetDay = targetCourse.getday();
        int targetSchedule = targetCourse.getschedule();
        int targetspanNum = targetCourse.getSpanNum();
        int addtarget=targetSchedule+targetspanNum;
        for(int target = targetSchedule; target<addtarget;target++) {
            if (classtable[targetDay - 1][target - 1] == 1){
                return true;
            }

        }
        return false;
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
    public void onBackPressed() {
        if(addCourse.size() > 0)
        {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("userCourseList", (ArrayList<? extends Parcelable>) addCourse);
            intent.putExtra("addCourse",bundle);
            setResult(RESULT_OK, intent);
            finish();
        }
        super.onBackPressed();
    }

    public boolean checkUnAddCourse(Course targetCourse){
        int[][] localClassTable = new int[7][16];
        for(int course_size = 0; course_size<addCourse.size(); course_size++) {
            int addspan = addCourse.get(course_size).getschedule()+ addCourse.get(course_size).getSpanNum()- 1;
            for (int spnum = addCourse.get(course_size).getschedule(); spnum <= addspan; spnum++) {
                localClassTable[addCourse.get(course_size).getday()-1][spnum-1] = 1;
            }
        }
        int targetDay = targetCourse.getday();
        int targetSchedule = targetCourse.getschedule();
        int targetspanNum = targetCourse.getSpanNum();
        int addtarget=targetSchedule+targetspanNum;
        for(int target = targetSchedule; target<addtarget;target++) {
            if (localClassTable[targetDay - 1][target - 1] == 1){
                return true;
            }
        }
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final Course displayCourse = (Course) parent.getItemAtPosition(position);
        if(!displayCourse.getname().equals(": ( 沒有符合條件的結果")) {
            try {
                ArrayList<String> result_dialog = new ArrayList<>(); //宣告動態陣列，用來存課程項目跟資料組合後的字串
                LayoutInflater inflater = LayoutInflater.from(AddCourseActivity.this); //LayoutInflater的目的是將自己設計xml的Layout轉成View
                View class_view = inflater.inflate(R.layout.class_msg, null); //指定要給View表述的Layout
                ListView into_class = (ListView) class_view.findViewById(R.id.into_class); //定義顯示課程資訊的清單物件
                ArrayAdapter<String> ClassInfo = new ArrayAdapter<String>(AddCourseActivity.this, android.R.layout.simple_list_item_1);//設定課程資訊的清單物件要顯示資料的陣列
                into_class.setAdapter(ClassInfo); //定義顯示課程資訊的清單物件
                ClassInfo.clear(); //先把清單物件的資料陣列清空
                result_dialog.clear(); //再把要存組合字串的陣列內容清空
                result_dialog.add("上課教室：" + displayCourse.getroom());
                result_dialog.add("課號：" + displayCourse.getserial());
                result_dialog.add("課程名稱：" + displayCourse.getname() + "\n( " + displayCourse.getname_eng() + " )");
                result_dialog.add("上課節數：" + displayCourse.getSpanNum());
                result_dialog.add("開課班級：" + displayCourse.getclass_for());
                result_dialog.add("修別：" + displayCourse.getrequire() + "( " + displayCourse.getrequire_eng() + " )");
                result_dialog.add("學分組合：" + displayCourse.getcredits() + "\n( 講授時數-實習時數-學分數 )");
                result_dialog.add("授課老師：" + displayCourse.getteacher() + "老師");
                for (int a = 0; a < result_dialog.size(); a++) //把陣列內的資料丟給清單顯示
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
                                if(checkCourseCollision(displayCourse)) {
                                    Toast.makeText(AddCourseActivity.this, "該課堂已衝堂 請檢查課表", Toast.LENGTH_SHORT).show();
                                }
                                else if(checkUnAddCourse(displayCourse)){
                                    Toast.makeText(AddCourseActivity.this, "該課堂已衝堂 請檢查課表", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Toast.makeText(AddCourseActivity.this, "已新增", Toast.LENGTH_SHORT).show();
                                    addCourse.add(displayCourse);
                                }
                            }
                        })
                        .setPositiveButton("離開", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).show();
            } catch (Exception e) {
                Log.e("dialog", e.toString());
            }
        }
    }
}
