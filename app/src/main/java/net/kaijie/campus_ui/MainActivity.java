package net.kaijie.campus_ui;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private ArrayList<PageView> pageList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        mTabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        TabLayout.Tab tab = mTabLayout.getTabAt(2); //選定初步顯示的Tab
        tab.select();

    }
    private void initData() {
        mTabLayout = (android.support.design.widget.TabLayout)findViewById(R.id.tabs);
        mTabLayout.addTab(mTabLayout.newTab().setText("個人"));
        mTabLayout.addTab(mTabLayout.newTab().setText("課程"));
        mTabLayout.addTab(mTabLayout.newTab().setText("地圖"));
        mTabLayout.addTab(mTabLayout.newTab().setText("運動"));
        mTabLayout.addTab(mTabLayout.newTab().setText("防災"));
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

    private class PageOneView extends PageView {
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
            TextView textView = (TextView) view.findViewById(R.id.item_test2);
            textView.setText("Page two");
            addView(view);
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
