package net.kaijie.campus_ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Rabbit on 2017/11/24.
 */
public class ViewSharedNote extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_sharednote);
        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        TextView tv_content = (TextView) findViewById(R.id.tv_content);
        TextView tv_user = (TextView) findViewById(R.id.tv_user);
        Bundle bundle = getIntent().getExtras();
        tv_title.setText(bundle.getString("title"));
        tv_content.setText(bundle.getString("content"));
        String user =  "上傳者：" + bundle.getString("userName");
        tv_user.setText(user);
        Button bt_return = (Button) findViewById(R.id.bt_return);
        bt_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
