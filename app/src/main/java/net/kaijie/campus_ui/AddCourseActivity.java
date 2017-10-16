package net.kaijie.campus_ui;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

/**
 * Created by Rabbit on 2017/10/5.
 */
public class AddCourseActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{

    private SharedPreferences settings;
    private static final String marker_data = "DATA";
    private static final String isCourseDataReady = "isCourseDataReady";
    private ListView lvCourse;
    private SearchView searchView;
    private CourseAdapter courseAdapter;

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
}
