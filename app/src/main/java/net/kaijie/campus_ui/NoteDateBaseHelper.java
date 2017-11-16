package net.kaijie.campus_ui;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by pc on 2017/11/6.
 */

public class NoteDateBaseHelper extends SQLiteOpenHelper {

    private final static int DB_VERSION = 1; // 資料庫版本
    private final static String DB_NAME = "NoteSQLite.db"; //資料庫名稱，附檔名為db

    public NoteDateBaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CreateNote = "create table note ("
                + "id integer primary key autoincrement, "
                + "title text,"
                + "content text , "
                + "date text)";
        db.execSQL(CreateNote);
    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
        // TODO Auto-generated method stub
    }
}
