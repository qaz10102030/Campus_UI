package net.kaijie.campus_ui;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import net.kaijie.campus_ui.NetworkResource.HttpRequest;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

/**
 * Created by pc on 2017/11/13.
 */

public class WelcomeActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSION = 99; //設定權限是否設定成功的檢查碼

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_layout);

        ProgressBar progBar = (ProgressBar) findViewById(R.id.progressBar);

        int permission = ActivityCompat.checkSelfPermission(WelcomeActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE); //設定權限常數
        int location = ActivityCompat.checkSelfPermission(WelcomeActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission != PackageManager.PERMISSION_GRANTED || location != PackageManager.PERMISSION_GRANTED) { //檢查是否有權限
            ActivityCompat.requestPermissions( //如果沒有就跟使用者要求
                    WelcomeActivity.this,
                    new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE, ACCESS_FINE_LOCATION}, REQUEST_PERMISSION
            );
        }
        else
        {
            mHandler.sendEmptyMessageDelayed(GOTO_MAIN_ACTIVITY,1000);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch(requestCode) {
            case REQUEST_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mHandler.sendEmptyMessageDelayed(GOTO_MAIN_ACTIVITY,1000);
                } else {
                    Toast.makeText(WelcomeActivity.this, "請允許所有權限避免功能不正常", Toast.LENGTH_SHORT).show();
                    mHandler.sendEmptyMessageDelayed(GOTO_MAIN_ACTIVITY,1000);
                }
                return;
        }

    }

    private static final int GOTO_MAIN_ACTIVITY = 0;
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case GOTO_MAIN_ACTIVITY:
                    Intent intent = new Intent();
                    intent.setClass(WelcomeActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                default:
                    break;
            }
        }
    };
}
