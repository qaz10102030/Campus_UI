package net.kaijie.campus_ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity
        //rabbit
        implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnMapClickListener
        ////////////
{
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private ArrayList<PageView> pageList;
    private CourseTableView courseTableView;

    //rabbit
    private GoogleMap mMap;
    private Marker singleMarker;
    private LatLng latlng;
    private BitmapDescriptor icon;
    private HashMap<String, HashMap<String, ArrayList<LatLng>>> mapArea=null;
    private static final String yuntech_json  = "[{\"college\":\"工程學院\",\"department\":[{\"name\":\"工程一館\",\"code\":\"EM\"},{\"name\":\"工程二館\",\"code\":\"EL\"},{\"name\":\"工程三館\",\"code\":\"ES\"},{\"name\":\"工程四館\",\"code\":\"EC\"},{\"name\":\"工程五館\",\"code\":\"EB\",\"floor\":[{\"floor_num\":\"1F\",\"classroom\":[{\"type\":\"電腦教室\",\"number\":\"EB102\"},{\"type\":\"一般教室\",\"number\":\"EB109\"},{\"type\":\"一般教室\",\"number\":\"EB110\"}]},{\"floor_num\":\"2F\",\"classroom\":[{\"type\":\"實驗室\",\"number\":\"EB201\"},{\"type\":\"一般教室\",\"number\":\"EB202\"}]}]},{\"name\":\"工程六館\",\"code\":\"EN\"}]}]";
    private HashMap<String, HashMap<String, List<LatLng>>> buildkind=null;
    private ArrayList<Marker> mMarkers = new ArrayList<>();
    private ArrayAdapter adFloor;
    public ImageButton bt_navigation;
    private SharedPreferences settings;
    private static final String marker_data = "DATA";
    private static final String bicycle_state = "bicycle_state";
    private static final String montor_state = "montor_state";
    private static final String trash_state = "trash_state";
    private static final String toilet_state = "toilet_state";
    private static final String college_state = "college_state";
    private static final int REQUEST_PERMISSION = 99; //設定權限是否設定成功的檢查碼
    private static Boolean isExit = false;
    private static Boolean hasTask = false;
    //////////////////////

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

        //rabbit
        check_permission();
        checkGPS();
        rabbit();
        /////////////////////////////////
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
            courseTableView = (CourseTableView) view.findViewById(R.id.ctv);
            initclass();

            //rabbit

            FloatingActionButton fab = (FloatingActionButton)view.findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final Snackbar snackbar = Snackbar.make(view, "诶嘿嘿 你想幹嘛", Snackbar
                            .LENGTH_SHORT);
                    snackbar.show();
                }
            });
            ///////////////////////////////////////

        }
    }
    public class PageThreeView extends PageView{
        public PageThreeView(Context context) {
            super(context);
            View view = LayoutInflater.from(context).inflate(R.layout.tab_3, null);

            //rabbit
            bt_navigation = (ImageButton) view.findViewById(R.id.bt_navigation);
            bt_navigation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(singleMarker != null)ClickInfoWondows(singleMarker.getPosition());
                }
            });
            Animation fadeoutfirst = AnimationUtils.loadAnimation(MainActivity.this,R.anim.fade_out_first);
            bt_navigation.startAnimation(fadeoutfirst);
            //////////////////////////////////

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

    //rabbit
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng yuntech = new LatLng(23.6951701,120.5337975);
        addArea();
        init_marker();
        singleMarker = null;
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setOnMarkerClickListener(this);
        mMap.setMyLocationEnabled(true); // 右上角的定位功能；這行會出現紅色底線，不過仍可正常編譯執行
        mMap.getUiSettings().setZoomControlsEnabled(true);  // 右下角的放大縮小功能
        mMap.getUiSettings().setCompassEnabled(true);       // 左上角的指南針，要兩指旋轉才會出現
        mMap.getUiSettings().setMapToolbarEnabled(false);    // 關閉右下角的導覽及開啟 Google Map功能
        mMap.moveCamera(CameraUpdateFactory.newLatLng(yuntech));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        mMap.setInfoWindowAdapter(new MyInfoWindowAdapter());
        mMap.setOnMapClickListener(this);
        mMap.setOnInfoWindowLongClickListener(new GoogleMap.OnInfoWindowLongClickListener() {
            @Override
            public void onInfoWindowLongClick(Marker marker) {
                Toast.makeText(MainActivity.this,marker.getTitle() + " 長按",Toast.LENGTH_SHORT).show();
            }
        });
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Toast.makeText(MainActivity.this,marker.getTitle() + " 點擊",Toast.LENGTH_SHORT).show();
                ClickInfoWondows(marker.getPosition());
            }
        });
    }
    @Override
    public boolean onMarkerClick(final Marker marker) {
        if(singleMarker == null) {
            Animation fadein = AnimationUtils.loadAnimation(this,R.anim.fade_in);
            bt_navigation.startAnimation(fadein);
        }
        singleMarker = marker;
        String tag = marker.getTag().toString();
        Toast.makeText(MainActivity.this, tag , Toast.LENGTH_SHORT).show();
        return false;
    }
    @Override
    public void onMapClick(LatLng latLng) {
        if(null != singleMarker) {
            Toast.makeText(MainActivity.this,singleMarker.getTitle(),Toast.LENGTH_SHORT).show();
            Animation fadeout = AnimationUtils.loadAnimation(this,R.anim.fade_out);
            bt_navigation.startAnimation(fadeout);
        }
        singleMarker = null;
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 判斷是否按下Back
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            /*
            if (!searchView.isIconified() && searchView.getQuery() == null) {
                searchView.setIconified(true);

            }else {
            */
            // 是否要退出
            if (!isExit) {
                isExit = true; //記錄下一次要退出
                Toast.makeText(this, "你真的要離開了嗎OAQ"
                        , Toast.LENGTH_SHORT).show();
                // 如果超過兩秒則恢復預設值
                if (!hasTask) {
                    timerExit.schedule(task, 2000);
                }
            } else {
                finish(); // 離開程式
                System.exit(0);
            }
        }
        //}
        return false;
    }
    public void rabbit() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        settings = getSharedPreferences(marker_data,0);
        settings.edit()
                .putBoolean(bicycle_state, true)
                .putBoolean(montor_state, true)
                .putBoolean(trash_state, true)
                .putBoolean(toilet_state, true)
                .putBoolean(college_state, true)
                .apply();

        buildkind = CommonMethod.Buildingkind();
        mapArea = CommonMethod.BuildingArea();
    }
    private void addArea() {
        PolygonOptions polygonOptions;
        for (String key : mapArea.keySet()) {
            int color = 0;
            switch (key) {
                case "行政區域":
                    color = Color.argb(255, 140, 178, 131);
                    break;
                case "一般區域":
                    color = Color.argb(255, 199, 211, 147);
                    break;
                case "學生宿舍":
                    color = Color.argb(255, 221, 152, 155);
                    break;
                case "管理學院":
                    color = Color.argb(255, 114, 176, 187);
                    break;
                case "工程學院":
                    color = Color.argb(255, 189, 154, 186);
                    break;
                case "人科學院":
                    color = Color.argb(255, 210, 185, 93);
                    break;
                case "設計學院":
                    color = Color.argb(255, 200, 131, 98);
                    break;
            }
            for (String key1 : mapArea.get(key).keySet()) {
                polygonOptions = new PolygonOptions();
                polygonOptions
                        .addAll(mapArea.get(key).get(key1))
                        .strokeWidth(0)
                        .fillColor(color);
                mMap.addPolygon(polygonOptions).setZIndex(1);
            }
        }
        for (String key : buildkind.keySet()) {
            int color = 0;
            if (!key.equals("推薦景點") && !key.equals("休閒區域")) {
                color = Color.argb(255, 237, 229, 210);
            } else if (key.equals("推薦景點")) {
                color = Color.argb(255, 188, 218, 220);
            } else if (key.equals("休閒區域")) {
                color = Color.argb(255, 173, 199, 128);
            }
            for (String key1 : buildkind.get(key).keySet()) {
                polygonOptions = new PolygonOptions();
                polygonOptions
                        .addAll(buildkind.get(key).get(key1))
                        .strokeWidth(1)
                        .strokeColor(Color.BLACK)
                        .fillColor(color);
                mMap.addPolygon(polygonOptions).setZIndex(2);
            }
        }
    }
    private void init_marker(){
        latlng = new LatLng(23.695210, 120.536921);
        icon = BitmapDescriptorFactory.fromResource(R.mipmap.marker_college);
        singleMarker = mMap.addMarker(new MarkerOptions()
                .position(latlng)
                .title("工程五館" + "----------")
                .snippet(yuntech_json)
                .icon(icon));
        singleMarker.setTag("學院");
        startDropMarkerAnimation(singleMarker);

        mMarkers.add(singleMarker);

        latlng = new LatLng(23.696210, 120.536921);
        icon = BitmapDescriptorFactory.fromResource(R.mipmap.marker_biycycle);
        singleMarker = mMap.addMarker(new MarkerOptions()
                .position(latlng)
                .title("腳踏車停車場" + "----------")
                .icon(icon));
        singleMarker.setTag("腳踏車車位");
        startDropMarkerAnimation(singleMarker);

        mMarkers.add(singleMarker);

        latlng = new LatLng(23.697210, 120.536921);
        icon = BitmapDescriptorFactory.fromResource(R.mipmap.marker_scooter);
        singleMarker = mMap.addMarker(new MarkerOptions()
                .position(latlng)
                .title("機車停車場" + "----------")
                .icon(icon));
        singleMarker.setTag("機車車位");
        startDropMarkerAnimation(singleMarker);

        mMarkers.add(singleMarker);

        latlng = new LatLng(23.698210, 120.536921);
        icon = BitmapDescriptorFactory.fromResource(R.mipmap.marker_toilet);
        singleMarker = mMap.addMarker(new MarkerOptions()
                .position(latlng)
                .title("廁所" + "----------")
                .icon(icon));
        singleMarker.setTag("廁所");
        startDropMarkerAnimation(singleMarker);

        mMarkers.add(singleMarker);

        latlng = new LatLng(23.699210, 120.536921);
        icon = BitmapDescriptorFactory.fromResource(R.mipmap.marker_trash);
        singleMarker = mMap.addMarker(new MarkerOptions()
                .position(latlng)
                .title("垃圾桶" + "----------")
                .icon(icon));
        singleMarker.setTag("垃圾桶");
        startDropMarkerAnimation(singleMarker);

        mMarkers.add(singleMarker);

    }
    public class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }
        @Override
        public View getInfoContents(Marker marker) {
            // 依指定layout檔，建立地標訊息視窗View物件
            LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
            View infoWindow = inflater.inflate(R.layout.my_infowindow, null);
            // 顯示地標title
            TextView title = ((TextView) infoWindow.findViewById(R.id.txtTitle));
            title.setText(marker.getTitle());
            // 顯示地標snippet
            String floorinfo = marker.getSnippet();
            if( floorinfo != null) {
                ListView lvSnippet = ((ListView) infoWindow.findViewById(R.id.lvFloor));
                adFloor = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1);
                lvSnippet.setAdapter(adFloor);
                parsefloor(floorinfo);
            }
            return infoWindow;
        }

        private void parsefloor(String floorinfo) {
            String test = "";
            JSONArray jArray = null;
            JSONObject jObject = null;
            try {
                jArray = new JSONArray(floorinfo);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < jArray.length(); i++) {
                try {
                    jObject = jArray.getJSONObject(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    test = jObject.getJSONArray("department").getJSONObject(4).getJSONArray("floor").toString();
                    JSONArray parsefloor = new JSONArray(test);
                    for(int j = 0;j<parsefloor.length();j++)
                    {
                        String floor_lv_string = "";
                        String floor_num = parsefloor.getJSONObject(j).getString("floor_num");
                        floor_lv_string += floor_num;
                        JSONArray floor_class = new JSONArray(parsefloor.getJSONObject(j).getJSONArray("classroom").toString());
                        for(int k = 0;k<floor_class.length();k++)
                        {
                            String get_class_number = floor_class.getJSONObject(k).getString("number");
                            String get_class_type = floor_class.getJSONObject(k).getString("type");
                            floor_lv_string += "\n" + get_class_number + " " + get_class_type;
                        }
                        adFloor.add(floor_lv_string);
                        adFloor.notifyDataSetChanged();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private void ClickInfoWondows(final LatLng latlng){
        LayoutInflater inflater = LayoutInflater.from(MainActivity.this); //LayoutInflater的目的是將自己設計xml的Layout轉成View
        final View view = inflater.inflate(R.layout.long_click_infowindows_dialog, null); //指定要給View表述的Layout
        ArrayList<String> function = new ArrayList<>();
        function.add("前往此處");

        new AlertDialog.Builder(MainActivity.this) //宣告對話框物件，並顯示
                .setTitle("功能選單")
                .setItems(function.toArray(new String[function.size()]), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + latlng.latitude + ", " + latlng.longitude + "&mode=w");
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        startActivity(mapIntent);
                    }
                })
                .show();
    }
    private void startDropMarkerAnimation(final Marker marker) {
        final LatLng target = marker.getPosition();
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = mMap.getProjection();
        Point targetPoint = proj.toScreenLocation(target);
        final long duration = (long) (25 + (targetPoint.y * 0.6));
        Point startPoint = proj.toScreenLocation(marker.getPosition());
        startPoint.y = 0;
        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
        final Interpolator interpolator = new LinearOutSlowInInterpolator();
        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed / duration);
                double lng = t * target.longitude + (1 - t) * startLatLng.longitude;
                double lat = t * target.latitude + (1 - t) * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));
                if (t < 1.0) {
                    // Post again 16ms later == 60 frames per second
                    handler.postDelayed(this, 16);
                }
            }
        });
    }
    public void check_permission(){
        int location = ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION);
        if (location != PackageManager.PERMISSION_GRANTED) { //檢查是否有權限
            ActivityCompat.requestPermissions( //如果沒有就跟使用者要求
                    MainActivity.this,
                    new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE, ACCESS_FINE_LOCATION}, REQUEST_PERMISSION
            );
        }
    }
    public void checkGPS(){
        if(!isGPSEnabled(this)) {
            Toast.makeText(MainActivity.this, "請開始定位功能避免定位失效", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }
    }
    public static boolean isGPSEnabled(Context context){
        LocationManager locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }
    Timer timerExit = new Timer();
    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            isExit = false;
            hasTask = true;
        }
    };
    //////////////////////
}