package net.kaijie.campus_ui;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import net.kaijie.campus_ui.NetworkResource.HttpRequest;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pc on 2017/11/3.
 */

public class SharedNotelist extends AppCompatActivity {
    private String serial;
    private HttpRequest httpRequest;
    private ListView note_list;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shared_notelist);
        Bundle bundle = getIntent().getExtras();
        serial = bundle.getString("serial");
        Toast.makeText(SharedNotelist.this,serial,Toast.LENGTH_SHORT).show();
        note_list = (ListView) findViewById(R.id.note_list);
        httpRequest = new HttpRequest(SharedNotelist.this);
        httpRequest.postShareNote(callback,serial);
    }

    private AdapterView.OnItemClickListener onItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ArrayList temp = (ArrayList) parent.getItemAtPosition(position);
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString("title",temp.get(0).toString());
            bundle.putString("content",temp.get(1).toString());
            bundle.putString("userName",temp.get(2).toString());
            intent.putExtras(bundle);
            intent.setClass(SharedNotelist.this,ViewSharedNote.class);
            startActivity(intent);
        }
    };

    private HttpRequest.VolleyCallback callback = new HttpRequest.VolleyCallback() {
        @Override
        public void onSuccess(String label, String result) {
            try{
                JSONArray jsonArray = new JSONArray(result);
                List<List<String>> list_data = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    List<String> temp = new ArrayList<>();
                    String title = jsonArray.getJSONObject(i).getString("title");
                    String content = jsonArray.getJSONObject(i).getString("content");
                    String userName = jsonArray.getJSONObject(i).getString("userName");
                    temp.add(title);
                    temp.add(content);
                    temp.add(userName);
                    list_data.add(temp);
                }
                ListAdapter listAdapter = new ListAdapter(list_data);
                note_list.setOnItemClickListener(onItemClick);
                note_list.setAdapter(listAdapter);
            }catch (JSONException ignored){}
        }

        @Override
        public void onError(String error) {
            Toast.makeText(SharedNotelist.this,"伺服器出錯啦><",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onImageSuccess(String label, Bitmap result) {

        }
    };

    class ListAdapter extends BaseAdapter{
        List<List<String>> list_adapter;

        public ListAdapter(List<List<String>> list_adapter) {
            this.list_adapter = list_adapter;
        }

        @Override
        public int getCount() {
            return list_adapter.size();
        }

        @Override
        public Object getItem(int arg0) {
            return list_adapter.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        @Override
        public View getView(final int arg0, View view, ViewGroup arg2) {
            if (view == null) {
                view = LayoutInflater.from(SharedNotelist.this).inflate(
                        R.layout.shared_notelist_item, null);
            }
            TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
            TextView tv_content = (TextView) view.findViewById(R.id.tv_content);
            TextView tv_user = (TextView) view.findViewById(R.id.tv_user);
            tv_title.setText(list_adapter.get(arg0).get(0));
            tv_content.setText(list_adapter.get(arg0).get(1));
            tv_user.setText(list_adapter.get(arg0).get(2));
            return view;
        }
    }
}
