package net.kaijie.campus_ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by pc on 2017/11/3.
 */

public class SharedNotelist extends AppCompatActivity {
    private String serial;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shared_notelist);
        Bundle bundle = getIntent().getExtras();
        serial = bundle.getString("serial");
        Toast.makeText(SharedNotelist.this,serial,Toast.LENGTH_SHORT).show();
    }
}
