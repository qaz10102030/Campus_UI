package net.kaijie.campus_ui;

import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by User on 2017/10/5.
 */
public class AddCourseActivity extends AppCompatActivity{

    private SharedPreferences settings;
    private static final String marker_data = "DATA";
    private static final String isCourseDataReady = "isCourseDataReady";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settings = getSharedPreferences(marker_data,0);
        boolean courseReady = settings.getBoolean(isCourseDataReady,false);
        if(!courseReady){

        }
        initCourse();
    }

    private void initCourse() {
    }

}
