package net.kaijie.campus_ui;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by pc on 2017/11/5.
 */

public class AddNoteActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_cancel, btn_save;
    private TextView tv_title, tv_text, tv_date;
    private EditText et_title, et_content;
    private NoteDateBaseHelper DBHelper;
    public int enter_state = 0;//用来区分是新建一个note还是更改原来的note
    public String last_content,last_title;//用来獲取edittext内容

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_note_activity);
        InitView();

    }

    private void InitView() {
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_save = (Button) findViewById(R.id.btn_save);
        tv_text = (TextView) findViewById(R.id.tv_text);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_date = (TextView) findViewById(R.id.tv_date);
        et_content = (EditText) findViewById(R.id.et_content);
        et_title = (EditText) findViewById(R.id.et_title);
        DBHelper = new NoteDateBaseHelper(this);

        //讀取現在時間
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String dateString = sdf.format(date);
        tv_date.setText(dateString);

        //接收id和筆記內容
        Bundle myBundle = this.getIntent().getExtras();
        last_title = myBundle.getString("title");
        last_content = myBundle.getString("info");
        enter_state = myBundle.getInt("enter_state");
        et_content.setText(last_content);
        et_title.setText(last_title);
        btn_cancel.setOnClickListener(this);
        btn_save.setOnClickListener(this);
        et_title.addTextChangedListener(new MagicTextLengthWatcher(20));
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_save:

                SQLiteDatabase db = DBHelper.getReadableDatabase();
                // 获取edittext内容

                String content = et_content.getText().toString();
                String title = et_title.getText().toString();
                if (enter_state == 0) {
                    if (!content.equals("")) {
                        Date date = new Date();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        String dateString = sdf.format(date);
                        // 向資料庫新增資料
                        ContentValues values = new ContentValues();
                        values.put("title", title);
                        values.put("content", content);
                        values.put("date", dateString);
                        db.insert("note", null, values);
                        finish();
                    }
                    else {
                        Toast.makeText(AddNoteActivity.this, "請輸入筆記內容", Toast.LENGTH_SHORT).show();
                    }
                }
                    else {
                    ContentValues values = new ContentValues();
                    values.put("content", content);
                    values.put("title", title);
                    db.update("note", values, "content =? and title = ?", new String[]{last_content,last_title});
                    finish();
                }
                break;
            case R.id.btn_cancel:
                finish();
                break;
        }
    }
}