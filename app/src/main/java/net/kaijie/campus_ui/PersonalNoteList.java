package net.kaijie.campus_ui;

import android.content.DialogInterface;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pc on 2017/11/3.
 */

public class PersonalNoteList extends AppCompatActivity implements
        AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener {

    private TextView tv_listname;
    public ListView note_list;
    private SimpleAdapter simple_adapter;
    private List<Map<String, Object>> dataList;
    private TextView tv_title,tv_content;
    private NoteDateBaseHelper DbHelper;

    private SQLiteDatabase DB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_notelist);
        InitView();


    }

    protected void onStart() {
        super.onStart();
        RefreshNotesList();
    }

    private void InitView() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_content = (TextView) findViewById(R.id.tv_content);
        note_list = (ListView) findViewById(R.id.note_list);
        tv_listname = (TextView) findViewById(R.id.tv_listname);
        dataList = new ArrayList<Map<String, Object>>();
        DbHelper = new NoteDateBaseHelper(this);
        DB = DbHelper.getReadableDatabase();
        note_list.setOnItemClickListener(this);
        note_list.setOnItemLongClickListener(this);

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, Menu.FIRST, Menu.NONE, "新增");
        menu.add(Menu.NONE, Menu.FIRST + 1, Menu.NONE, "其他功能");
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case Menu.FIRST:

                Intent intent = new Intent(this, AddNoteActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("title","");
                bundle.putString("info","");
                bundle.putInt("enter_state", 0);
                intent.putExtras(bundle);
                startActivity(intent);

                break;
          //  case Menu.FIRST + 1:

              //  break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void RefreshNotesList() {
        //如果dataList已经有的内容，全部删掉
        //并且更新simp_adapter
        int size = dataList.size();
        if (size > 0) {
            dataList.removeAll(dataList);
            simple_adapter.notifyDataSetChanged();
        }
        //從資料庫讀取信息
        Cursor cursor = DB.query("note", null, null, null, null, null, null);
        //startManagingCursor(cursor);
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex("content"));
            String title = cursor.getString(cursor.getColumnIndex("title"));
            String date = cursor.getString(cursor.getColumnIndex("date"));
            Map<String, Object> map = new HashMap<String, Object>();

            map.put("tv_title", title);
            map.put("tv_content", name);
            map.put("tv_date", date);
            dataList.add(map);

        }

        simple_adapter = new SimpleAdapter(this, dataList, R.layout.notelist_item,
                new String[]{"tv_title","tv_content", "tv_date"}, new int[]{
                R.id.tv_title,R.id.tv_content, R.id.tv_date});

        note_list.setAdapter(simple_adapter);
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String content = parent.getItemAtPosition(position).toString();

        String content1 = content.split("[{},=]")[2];
        String title1 = content.split("[{},=]")[6];


        Intent myIntent = new Intent(PersonalNoteList.this, AddNoteActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("title",title1 );
        bundle.putString("info", content1);
        bundle.putInt("enter_state", 1);
        myIntent.putExtras(bundle);
        startActivity(myIntent);


    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        Builder builder = new Builder(this)
                .setTitle("刪除筆記")
                .setMessage("確定要刪除嗎？")
                .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String content = note_list.getItemAtPosition(position) + "";
                        String content1 = content.substring(content.indexOf("=") + 1, content.indexOf(","));

                        DB.delete("note", "content = ?", new String[]{content1});
                        RefreshNotesList();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        builder.create();
        builder.show();
        return true;
    }



}