package net.kaijie.campus_ui;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
        c1.setDay(1);
        c1.setClassroom("EB109");
        c1.setNum(2163);
        c1.setName("作業系統");
        c1.setDes("TRUE");
        c1.setClasstimes(2);
        c1.setSpanNum(2);
        list.add(c1);

        Course c2 = new Course();
        c2.setDay(5);
        c2.setClassroom("EB110");
        c2.setNum(2154);
        c2.setName("嵌入式作業系統實作");
        c2.setDes("TRUE");
        c2.setClasstimes(7);
        c2.setSpanNum(2);
        list.add(c2);

        Course c3 = new Course();
        c3.setDay(4);
        c3.setClassroom("EB110");
        c3.setNum(2154);
        c3.setName("嵌入式作業系統實作");
        c3.setDes("TRUE");
        c3.setClasstimes(3);
        c3.setSpanNum(2);
        list.add(c3);
        courseTableView.setOnCourseItemClickListener(new CourseTableView.OnCourseItemClickListener() {
            @Override
            public void onCourseItemClick(TextView tv, int Classtimes, int Day, String Classroom, int Num, String Name, String des) {
                Log.d("test",tv.getText().toString() );
            }
        });
        courseTableView.updateCourseViews(list);
    }


    private void initData() {
        mTabLayout = (android.support.design.widget.TabLayout)findViewById(R.id.tabs);
        mTabLayout.addTab(mTabLayout.newTab().setText("個人"));
        mTabLayout.addTab(mTabLayout.newTab().setText("課程"));
        mTabLayout.addTab(mTabLayout.newTab().setText("地圖"));
        mTabLayout.addTab(mTabLayout.newTab().setText("運動"));
        mTabLayout.addTab(mTabLayout.newTab().setText("聊天"));
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
