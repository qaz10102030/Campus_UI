package net.kaijie.campus_ui;

import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private ArrayList<PageView> pageList;

    private CourseTableView courseTableView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        initData();
        initView();


        mTabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));


        TabLayout.Tab tab = mTabLayout.getTabAt(2); //選定初步顯示的Tab
        tab.select();


    }

    public void initclass() {

        List<Course> list = new ArrayList<>();
        Course c1 = new Course();
        c1.setday(1);
        c1.setroom("EB109");
        c1.setserial(2163);
        c1.setname("作業系統");
        c1.setteacher("陳士煜老師");
        c1.setschedule(2);
        c1.setSpanNum(2);
        list.add(c1);

        Course c2 = new Course();
        c2.setday(2);
        c2.setroom("EB109");
        c2.setserial(2163);
        c2.setname("作業系統");
        c2.setteacher("陳士煜老師");
        c2.setschedule(2);
        c2.setSpanNum(2);
        list.add(c2);

        Course c3 = new Course();
        c3.setday(3);
        c3.setroom("EB109");
        c3.setserial(2163);
        c3.setname("作業系統");
        c3.setteacher("陳士煜老師");
        c3.setschedule(2);
        c3.setSpanNum(2);
        list.add(c3);


        Course c4 = new Course();
        c4.setday(4);
        c4.setroom("EB109");
        c4.setserial(2163);
        c4.setname("作業系統");
        c4.setteacher("陳士煜老師");
        c4.setschedule(2);
        c4.setSpanNum(2);
        list.add(c4);

        Course c5 = new Course();
        c5.setday(5);
        c5.setroom("EB109");
        c5.setserial(2163);
        c5.setname("作業系統");
        c5.setteacher("陳士煜老師");
        c5.setschedule(2);
        c5.setSpanNum(2);
        list.add(c5);
        courseTableView.setOnCourseItemClickListener(new CourseTableView.OnCourseItemClickListener() {
            @Override
            public void onCourseItemClick(TextView tv, int schedule, int day, String room,int serial, String name, String teacher) {
            try {
                ArrayList<String> result_dialog = new ArrayList<>(); //宣告動態陣列，用來存課程項目跟資料組合後的字串
                LayoutInflater inflater = LayoutInflater.from(MainActivity.this); //LayoutInflater的目的是將自己設計xml的Layout轉成View
                View class_view = inflater.inflate(R.layout.class_msg, null); //指定要給View表述的Layout
                ListView into_class = (ListView) class_view.findViewById(R.id.into_class); //定義顯示課程資訊的清單物件
                ArrayAdapter ClassInfo = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1);//設定課程資訊的清單物件要顯示資料的陣列
                into_class.setAdapter(ClassInfo); //定義顯示課程資訊的清單物件

                String course_plan="True";

             //  final String[] classes_Item = {"上課教室：","課號：","課程名稱：","授課老師：","教學大綱網站：\n"}; //設定存有課程項目的陣列
                ClassInfo.clear(); //先把清單物件的資料陣列清空
                result_dialog.clear(); //再把要存組合字串的陣列內容清空
                result_dialog.add("上課教室：" + room);
                result_dialog.add("課號：" + serial);
                result_dialog.add("課程名稱：" + name);
                result_dialog.add("授課老師：" + teacher);
                result_dialog.add("教學大綱網站：\n" + course_plan);
                for(int a = 0;a<result_dialog.size();a++) //把陣列內的資料丟給清單顯示
                {
                    ClassInfo.add(result_dialog.get(a)); //將資料加到陣列裡
                    ClassInfo.notifyDataSetChanged(); //通知陣列資料有被更改
                    into_class.smoothScrollToPosition(ClassInfo.getCount() - 1); //滑動到最後一項(如果超出畫面)
                }

                new AlertDialog.Builder(MainActivity.this) //宣告對話框物件，並顯示課程資料
                        .setTitle("詳細資料")
                        .setView(class_view)
                        .setPositiveButton("離開", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).show();
            }
            catch (Exception e){Log.e("dialog",e.toString());}
            }
        });
        courseTableView.updateCourseViews(list);
    }


    private void initData() {
        mTabLayout = (android.support.design.widget.TabLayout)findViewById(R.id.tabs);
        mTabLayout.addTab(mTabLayout.newTab().setText("個人").setIcon(R.mipmap.ic_yuntech));
        mTabLayout.addTab(mTabLayout.newTab().setText("課程").setIcon(R.mipmap.ic_yuntech));
        mTabLayout.addTab(mTabLayout.newTab().setText("地圖").setIcon(R.mipmap.ic_yuntech));
        mTabLayout.addTab(mTabLayout.newTab().setText("運動").setIcon(R.mipmap.ic_yuntech));
        mTabLayout.addTab(mTabLayout.newTab().setText("聊天").setIcon(R.mipmap.ic_yuntech));
        //取得TabLayout
        LinearLayout linearLayout=(LinearLayout)mTabLayout.getChildAt(0);
        linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        linearLayout.setDividerDrawable(ContextCompat.getDrawable(this,
                R.drawable.layout_divider_vertical));
        linearLayout.setDividerPadding(35);

        pageList = new ArrayList<>();
        pageList.add(new PageOneView(MainActivity.this));
        pageList.add(new PageTwoView(MainActivity.this));
        pageList.add(new PageThreeView(MainActivity.this));
        pageList.add(new PageFourView(MainActivity.this));
        pageList.add(new PageFiveView(MainActivity.this));
    }
    private void initView() {
        mViewPager = (ViewPager)findViewById(R.id.viewpager);
        mViewPager.setAdapter(new SamplePagerAdapter());

    }






    class SamplePagerAdapter extends PagerAdapter{
        @Override
        public int getCount(){
        return 5;
        }
        @Override
        public boolean isViewFromObject(View view, Object o){
        return o == view ;
        }

        @Override
        public CharSequence getPageTitle(int position){
            return  "Item" + (position+1);
        }
        @Override
        public Object instantiateItem(ViewGroup container, int position){
            container.addView(pageList.get(position));
            return pageList.get(position);
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object){
            container.removeView((View) object);
        }
    }


    public class PageView extends RelativeLayout {
        public PageView(Context context) {
            super(context);
        }
    }
    public class PageOneView extends PageView {
        public PageOneView(Context context) {
            super(context);
            View view = LayoutInflater.from(context).inflate(R.layout.tab_1, null);
            TextView textView = (TextView) view.findViewById(R.id.item_test1);
            textView.setText("Page one");
            addView(view);
        }
    }
    public class PageTwoView extends PageView{
        public PageTwoView(Context context) {
            super(context);
            View view = LayoutInflater.from(context).inflate(R.layout.tab_2, null);

            addView(view);
            courseTableView = (CourseTableView) findViewById(R.id.ctv);
            initclass();

        }
    }
    public class PageThreeView extends PageView{
        public PageThreeView(Context context) {
            super(context);
            View view = LayoutInflater.from(context).inflate(R.layout.tab_3, null);
            TextView textView = (TextView) view.findViewById(R.id.item_test3);
            textView.setText("Page three");
            addView(view);




        }
    }
    public class PageFourView extends PageView{
        public PageFourView(Context context) {
            super(context);
            View view = LayoutInflater.from(context).inflate(R.layout.tab_4, null);
            TextView textView = (TextView) view.findViewById(R.id.item_test4);
            textView.setText("Page four");
            addView(view);
        }
    }
    public class PageFiveView extends PageView{
        public PageFiveView(Context context) {
            super(context);
            View view = LayoutInflater.from(context).inflate(R.layout.tab_5, null);
            TextView textView = (TextView) view.findViewById(R.id.item_test5);
            textView.setText("Page five");
            addView(view);
        }
    }



}
