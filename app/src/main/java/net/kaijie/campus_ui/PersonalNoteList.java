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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by pc on 2017/11/3.
 */

public class PersonalNoteList extends AppCompatActivity implements
        AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener {

    private TextView tv_listname;
    public  ListView note_list;
    private ListAdapter listAdapter;
    private List<List<String>> dataList;
    private NoteDateBaseHelper DbHelper;
    private SQLiteDatabase DB;
    private Set<SwipeListLayout> sets = new HashSet();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_notelist);

        InitView();


    }

    class MyOnSlipStatusListener implements SwipeListLayout.OnSwipeStatusListener {

        private SwipeListLayout slipListLayout;

        public MyOnSlipStatusListener(SwipeListLayout slipListLayout) {
            this.slipListLayout = slipListLayout;
        }

        @Override
        public void onStatusChanged(SwipeListLayout.Status status) {
            if (status == SwipeListLayout.Status.Open) {
                //若有其他的item的状态为Open，则Close，然后移除
                if (sets.size() > 0) {
                    for (SwipeListLayout s : sets) {
                        s.setStatus(SwipeListLayout.Status.Close, true);
                        sets.remove(s);
                    }
                }
                sets.add(slipListLayout);
            } else {
                if (sets.contains(slipListLayout))
                    sets.remove(slipListLayout);
            }
        }

        @Override
        public void onStartCloseAnimation() {

        }

        @Override
        public void onStartOpenAnimation() {

        }

    }

    class ListAdapter extends BaseAdapter {
        List<List<String>> list_adapter;
        public ListAdapter(List<List<String>> list_adapter){
            this.list_adapter=list_adapter;
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
                view = LayoutInflater.from(PersonalNoteList.this).inflate(
                        R.layout.notelist_item, null);
            }
            final SwipeListLayout sll_main = (SwipeListLayout) view
                    .findViewById(R.id.sll_main);
            TextView tv_shared = (TextView) view.findViewById(R.id.tv_shared);
            TextView tv_delete = (TextView) view.findViewById(R.id.tv_delete);
            TextView tv_edit = (TextView) view.findViewById(R.id.tv_edit);
            TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
            TextView tv_content = (TextView) view.findViewById(R.id.tv_content);
            TextView tv_date = (TextView) view.findViewById(R.id.tv_date);
            tv_title.setText(list_adapter.get(arg0).get(0));
            tv_content.setText(list_adapter.get(arg0).get(1));
            tv_date.setText(list_adapter.get(arg0).get(2));
            sll_main.setOnSwipeStatusListener(new MyOnSlipStatusListener(
                    sll_main));
            tv_shared.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View view) {
                    sll_main.setStatus(SwipeListLayout.Status.Close, true);
                    Toast.makeText(PersonalNoteList.this,"共用",Toast.LENGTH_SHORT).show();

                    notifyDataSetChanged();
                }
            });
            tv_delete.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View view) {
                    sll_main.setStatus(SwipeListLayout.Status.Close, true);
                    Toast.makeText(PersonalNoteList.this,"刪除",Toast.LENGTH_SHORT).show();

                    notifyDataSetChanged();
                }
            });
            tv_edit.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View view) {
                    sll_main.setStatus(SwipeListLayout.Status.Close, true);
                    Toast.makeText(PersonalNoteList.this,"修改",Toast.LENGTH_SHORT).show();
                    String content = list_adapter.get(arg0).get(1);

                    String title1 = list_adapter.get(arg0).get(0);


                    Intent myIntent = new Intent(PersonalNoteList.this, AddNoteActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("title",title1 );
                    bundle.putString("info", content);
                    bundle.putInt("enter_state", 1);
                    myIntent.putExtras(bundle);
                    startActivityForResult(myIntent,1);
                    notifyDataSetChanged();
                }
            });
            return view;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        RefreshNotesList();
    }

    protected void onStart() {
        super.onStart();
        RefreshNotesList();

    }

    private void InitView() {
        note_list = (ListView) findViewById(R.id.note_list);
        tv_listname = (TextView) findViewById(R.id.tv_listname);
        dataList = new ArrayList<>();
        DbHelper = new NoteDateBaseHelper(this);
        DB = DbHelper.getReadableDatabase();
       /* note_list.setOnItemClickListener(this);
        note_list.setOnItemLongClickListener(this);
*/
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, Menu.FIRST, Menu.NONE, "新增");
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
            dataList.clear();
            listAdapter.notifyDataSetChanged();
        }
        //從資料庫讀取信息
        Cursor cursor = DB.query("note", null, null, null, null, null, null);
        //startManagingCursor(cursor);
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex("content"));
            String title = cursor.getString(cursor.getColumnIndex("title"));
            String date = cursor.getString(cursor.getColumnIndex("date"));
            List<String> str= new ArrayList<>();
            str.add(name);
            str.add(title);
            str.add(date);
            dataList.add(str);

        }

     /*   simple_adapter = new SimpleAdapter(this, dataList, R.layout.notelist_item,
                new String[]{"tv_title","tv_content", "tv_date"}, new int[]{
                R.id.tv_title,R.id.tv_content, R.id.tv_date});
*/
        listAdapter= new ListAdapter(dataList);
        note_list.setAdapter(listAdapter);
        note_list.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    //当listview开始滑动时，若有item的状态为Open，则Close，然后移除
                    case SCROLL_STATE_TOUCH_SCROLL:
                        if (sets.size() > 0) {
                            for (SwipeListLayout s : sets) {
                                s.setStatus(SwipeListLayout.Status.Close, true);
                                sets.remove(s);
                            }
                        }
                        break;

                }
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

            }
        });
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