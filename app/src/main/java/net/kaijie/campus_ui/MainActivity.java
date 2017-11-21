package net.kaijie.campus_ui;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.multidex.MultiDex;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
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
import com.google.android.gms.vision.text.Text;

import net.kaijie.campus_ui.NetworkResource.ChatSocket;
import net.kaijie.campus_ui.NetworkResource.HttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
        GoogleMap.OnMapClickListener,
        View.OnClickListener,
        CourseTableView.OnCourseItemClickListener
        ////////////
{
    //kaijie
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private ArrayList<PageView> pageList;
    private CourseTableView courseTableView;
    private List<Course> userCourseList = new ArrayList<>();
    ///////////
    //rabbit
    private GoogleMap mMap;
    private Marker singleMarker;
    private LatLng latlng;
    private BitmapDescriptor icon;
    private HashMap<String, HashMap<String, ArrayList<LatLng>>> mapArea = null;
    private static final String yuntech_json = "[{\"college\":\"工程學院\",\"department\":[{\"name\":\"工程一館\",\"code\":\"EM\"},{\"name\":\"工程二館\",\"code\":\"EL\"},{\"name\":\"工程三館\",\"code\":\"ES\"},{\"name\":\"工程四館\",\"code\":\"EC\"},{\"name\":\"工程五館\",\"code\":\"EB\",\"floor\":[{\"floor_num\":\"1F\",\"classroom\":[{\"type\":\"電腦教室\",\"number\":\"EB102\"},{\"type\":\"一般教室\",\"number\":\"EB109\"},{\"type\":\"一般教室\",\"number\":\"EB110\"}]},{\"floor_num\":\"2F\",\"classroom\":[{\"type\":\"實驗室\",\"number\":\"EB201\"},{\"type\":\"一般教室\",\"number\":\"EB202\"}]}]},{\"name\":\"工程六館\",\"code\":\"EN\"}]}]";
    private HashMap<String, HashMap<String, List<LatLng>>> buildkind = null;
    private ArrayList<Marker> mMarkers = new ArrayList<>();
    private ArrayAdapter<String> adFloor;
    public ImageButton bt_navigation;
    public SharedPreferences settings;
    private static final String marker_data = "DATA";
    private static final String bicycle_state = "bicycle_state";
    private static final String montor_state = "montor_state";
    private static final String trash_state = "trash_state";
    private static final String toilet_state = "toilet_state";
    private static final String college_state = "college_state";
    private static final String isCourseDataReady = "isCourseDataReady";
    private static final String courseData = "CourseData";
    public  static final String fb_userName = "fb_userName";
    private static final String fb_userID = "fb_userID";
    private static final String user_depart = "user_depart";
    private static final int REQUEST_PERMISSION = 99; //設定權限是否設定成功的檢查碼
    private static Boolean isExit = false;
    private static Boolean hasTask = false;
    private HttpRequest httpRequest;
    public ChatSocket chatSocket;
    public static MainActivity mainActivity;
    private ProgressDialog proDialog;
    private FloatingActionMenu menu;
    private CallbackManager callbackManager;
    //////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        /* get package hash key for facebook
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "net.kaijie.campus_ui",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException ignored) {}
        */
        mainActivity = this;
        httpRequest = new HttpRequest(MainActivity.this);
        settings = getSharedPreferences(marker_data, 0);
        initData();
        initView();

        mTabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));

        TabLayout.Tab tab = mTabLayout.getTabAt(2); //選定初步顯示的Tab
        if (tab != null) {
            tab.select();
        }

        //rabbit
        check_permission();
        checkGPS();
        rabbit();
        /////////////////////////////////
    }

    //kaijie
    public void initclass() {
        String userCourse = settings.getString(courseData, "");
        if (userCourse.equals("")) {
            Toast.makeText(MainActivity.this, "無使用者選課資料", Toast.LENGTH_SHORT).show();
        } else {
            ArrayList<Course> tempCourse = selectCourse();
            String[] dataList = userCourse.split(",");
            if (tempCourse != null) {
                for (int i = 0; i < tempCourse.size(); i++) {
                    for (int j = 0; j < dataList.length; j += 2) {
                        Course initUserCourse = tempCourse.get(i);
                        if (initUserCourse.getserial().equals(dataList[j]) && initUserCourse.getclass_for().equals(dataList[j + 1])) {
                            userCourseList.add(initUserCourse);
                            break;
                        }
                    }
                }
            }
        }
    }

    private ArrayList<Course> selectCourse() {
        ArrayList<Course> temp = new ArrayList<>();
        SQLiteDatabase db = SQLiteDatabase.openDatabase(getFilesDir() + "class_database.db", null, SQLiteDatabase.OPEN_READONLY);
        String selectCourse = "select * from tb_course";
        try {
            Cursor cursor = db.rawQuery(selectCourse, null);
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                Course tempCourse = new Course();
                tempCourse.setname(cursor.getString(1))
                        .setname_eng(cursor.getString(2))
                        .setserial(cursor.getString(3))
                        .setroom(cursor.getString(4))
                        .setclassfor(cursor.getString(5))
                        .setschedule(cursor.getInt(6))
                        .setSpanNum(cursor.getInt(7))
                        .setteacher(cursor.getString(8))
                        .setrequire(cursor.getString(9))
                        .setrequire_eng(cursor.getString(10))
                        .setcredits(cursor.getString(11))
                        .setday(cursor.getInt(12))
                        .setschedule_display(cursor.getString(13));
                temp.add(tempCourse);
                cursor.moveToNext();
            }
            cursor.close();
            Log.d("SQLite", "course data is ready");
        } catch (SQLiteException e) {
            Log.d("SQLite", e.getMessage());
            return null;
        }
        db.close();
        return temp;
    }

    private void initData() {
        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mTabLayout.addTab(mTabLayout.newTab().setText("個人").setIcon(R.drawable.ic_person));
        mTabLayout.addTab(mTabLayout.newTab().setText("課程").setIcon(R.drawable.ic_class));
        mTabLayout.addTab(mTabLayout.newTab().setText("地圖").setIcon(R.drawable.ic_yuntech));
        mTabLayout.addTab(mTabLayout.newTab().setText("運動").setIcon(R.drawable.ic_sport));
        mTabLayout.addTab(mTabLayout.newTab().setText("聊天").setIcon(R.drawable.ic_chat));
        //取得TabLayout
        LinearLayout linearLayout = (LinearLayout) mTabLayout.getChildAt(0);
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
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(new SamplePagerAdapter());
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.menu_item:
                Intent intent = new Intent(this, AddCourseActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("userCourseList", (ArrayList<? extends Parcelable>) userCourseList);
                intent.putExtra("userCourse", bundle);
                startActivityForResult(intent, 1);
                menu.close(true);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Bundle resultBundle = data.getBundleExtra("addCourse");
                List<Course> temp = resultBundle.getParcelableArrayList("userCourseList");
                if (temp != null) {
                    for (int i = 0; i < temp.size(); i++) {
                        userCourseList.add(temp.get(i));
                    }
                    courseTableView.updateCourseViews(userCourseList);
                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(MainActivity.this, "新增失敗", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onCourseItemClick(TextView tv, int schedule, int day, String room, final String serial, String name, String name_eng, String class_for, String require, String require_eng, String credits, String teacher, int spanNum) {
        try {
            ArrayList<String> result_dialog = new ArrayList<>(); //宣告動態陣列，用來存課程項目跟資料組合後的字串
            LayoutInflater inflater = LayoutInflater.from(MainActivity.this); //LayoutInflater的目的是將自己設計xml的Layout轉成View
            View class_view = inflater.inflate(R.layout.class_msg, null); //指定要給View表述的Layout
            ListView into_class = (ListView) class_view.findViewById(R.id.into_class); //定義顯示課程資訊的清單物件
            ArrayAdapter<String> ClassInfo = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1);//設定課程資訊的清單物件要顯示資料的陣列
            into_class.setAdapter(ClassInfo); //定義顯示課程資訊的清單物件

            String course_plan = "True";

            //  final String[] classes_Item = {"上課教室：","課號：","課程名稱：","授課老師：","教學大綱網站：\n"}; //設定存有課程項目的陣列
            ClassInfo.clear(); //先把清單物件的資料陣列清空
            result_dialog.clear(); //再把要存組合字串的陣列內容清空
            result_dialog.add("上課教室：" + room);
            result_dialog.add("課號：" + serial);
            result_dialog.add("課程名稱：" + name + "\n( " + name_eng + " )");
            result_dialog.add("上課節數：" + spanNum);
            result_dialog.add("開課班級：" + class_for);
            result_dialog.add("修別：" + require + "( " + require_eng + " )");
            result_dialog.add("學分組合：" + credits + "\n( 講授時數-實習時數-學分數 )");
            result_dialog.add("授課老師：" + teacher + "老師");
            for (int a = 0; a < result_dialog.size(); a++) //把陣列內的資料丟給清單顯示
            {
                ClassInfo.add(result_dialog.get(a)); //將資料加到陣列裡
                ClassInfo.notifyDataSetChanged(); //通知陣列資料有被更改
                into_class.smoothScrollToPosition(ClassInfo.getCount() - 1); //滑動到最後一項(如果超出畫面)
            }

            new AlertDialog.Builder(MainActivity.this) //宣告對話框物件，並顯示課程資料
                    .setTitle("詳細資料")
                    .setView(class_view)
                    .setNegativeButton("刪除", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(MainActivity.this, "已刪除", Toast.LENGTH_SHORT).show();
                            courseTableView.clearViewsIfNeeded();
                            Course compare_c = new Course();
                            compare_c.setserial(serial);
                            int remove_c = 0;
                            for (int j = 0; j < userCourseList.size(); j++) {
                                if (userCourseList.get(j).getserial() == compare_c.getserial()) {
                                    remove_c = j;
                                }
                            }
                            userCourseList.remove(remove_c);
                            courseTableView.updateCourseViews(userCourseList);
                        }
                    })
                    .setPositiveButton("離開", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).show();
        } catch (Exception e) {
            Log.e("dialog", e.toString());
        }
    }

    class SamplePagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return o == view;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "Item" + (position + 1);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(pageList.get(position));
            return pageList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
            menu.close(true);
        }
    }

    class SamplePagerAdapter2 extends PagerAdapter {
        private ArrayList<PageView> personal;

        public SamplePagerAdapter2(ArrayList<PageView> personalPage) {
            personal = personalPage;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return o == view;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "Item " + (position + 1);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(personal.get(position));
            return personal.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
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
            TabLayout mTabs = (TabLayout) view.findViewById(R.id.tabs2);
            mTabs.addTab(mTabs.newTab().setText("我的筆記"));
            mTabs.addTab(mTabs.newTab().setText("共同筆記"));
            ViewPager mViewPager2 = (ViewPager) view.findViewById(R.id.viewpager2);
            ArrayList<PageView> personalPage = new ArrayList<>();
            initclass();
            personalPage.add(new PersonalOneView(MainActivity.this));
            personalPage.add(new PersonalTwoView(MainActivity.this));
            mViewPager2.setAdapter(new SamplePagerAdapter2(personalPage));
            mViewPager2.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabs));
            mTabs.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager2));

            final TextView tv_depart = (TextView) view.findViewById(R.id.tv_depart);
            final TextView tv_user = (TextView) view.findViewById(R.id.tv_user);
            final ImageView iv_userSticker = (ImageView) view.findViewById(R.id.iv_userSticker);
            iv_userSticker.setImageResource(R.mipmap.default_sticker);
            final Button bt_setDepart = (Button) view.findViewById(R.id.bt_setDepart);
            bt_setDepart.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            LoginButton loginButton = (LoginButton) view.findViewById(R.id.login_button);
            FB_Login(loginButton,tv_depart,bt_setDepart,tv_user,iv_userSticker);

            settings.getString(fb_userName,"");
            loadUserSetting(settings.getString(fb_userID,""),tv_depart,tv_user,iv_userSticker,bt_setDepart);

            addView(view);
        }

        private void FB_Login(final LoginButton loginButton ,final TextView tv_depart,final Button bt_setDepart,final TextView tv_user, final ImageView iv_userSticker) {
            //宣告callback Manager
            callbackManager = CallbackManager.Factory.create();
            //找到login button
            loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                //登入成功
                @Override
                public void onSuccess(LoginResult loginResult) {
                    //accessToken之後或許還會用到 先存起來
                    AccessToken accessToken = loginResult.getAccessToken();
                    Log.d("FB_test","access token got.");
                    //send request and call graph api
                    GraphRequest request = GraphRequest.newMeRequest(
                            accessToken,
                            new GraphRequest.GraphJSONObjectCallback() {
                                //當RESPONSE回來的時候
                                @Override
                                public void onCompleted(JSONObject object, GraphResponse response) {
                                    //讀出姓名 ID FB個人頁面連結
                                    Log.d("FB_test","complete");
                                    Log.d("FB_test",object.optString("name"));
                                    Log.d("FB_test",object.optString("link"));
                                    Log.d("FB_test",object.optString("id"));
                                    String userinfo = "User name: " + object.optString("name") +
                                            "\nUser ID: " + object.optString("id") +
                                            "\nUser Link:\n" + object.optString("link");
                                    tv_user.setText(object.optString("name"));
                                    settings.edit().putString(fb_userName,object.optString("name")).putString(fb_userID,object.optString("id")).apply();
                                    String url = "https://graph.facebook.com/"+object.optString("id")+"/picture?type=large&w\u200C\u200Bidth=400&height=400";
                                    httpRequest.getSticker(new HttpRequest.VolleyCallback() {
                                        @Override
                                        public void onSuccess(String label, String result) {

                                        }

                                        @Override
                                        public void onError(String error) {

                                        }

                                        @Override
                                        public void onImageSuccess(String label, Bitmap result) {
                                            iv_userSticker.setImageBitmap(getCircleBitmap(result));
                                            tv_user.setVisibility(VISIBLE);
                                            if(settings.getString(user_depart,"").equals("")) {
                                                bt_setDepart.setVisibility(VISIBLE);
                                                tv_depart.setVisibility(GONE);
                                            }
                                            else {
                                                tv_depart.setVisibility(VISIBLE);
                                                bt_setDepart.setVisibility(GONE);
                                                tv_depart.setText(settings.getString(user_depart,""));
                                            }
                                        }
                                    },url);
                                }
                            });
                    //包入你想要得到的資料 送出request
                    Bundle parameters = new Bundle();
                    parameters.putString("fields", "id,name,link");
                    request.setParameters(parameters);
                    request.executeAsync();
                }
                //登入取消
                @Override
                public void onCancel() {
                    // App code
                    Log.d("FB_test","CANCEL");
                }
                //登入失敗
                @Override
                public void onError(FacebookException exception) {
                    // App code
                    Log.d("FB_test",exception.toString());
                }
            });
        }

        private void loadUserSetting(String userID,final TextView tv_depart, final TextView tv_user, final ImageView iv_sticker,Button bt_setDepart){
            if(settings.getString(user_depart,"").equals("")) {
                bt_setDepart.setVisibility(VISIBLE);
                tv_depart.setVisibility(GONE);
            }
            else {
                tv_depart.setVisibility(VISIBLE);
                bt_setDepart.setVisibility(GONE);
                tv_depart.setText(settings.getString(user_depart,""));
            }
            if(!userID.equals("")) {
                String url = "https://graph.facebook.com/" + userID + "/picture?type=large&w\u200C\u200Bidth=400&height=400";
                httpRequest.getSticker(new HttpRequest.VolleyCallback() {
                    @Override
                    public void onSuccess(String label, String result) {

                    }

                    @Override
                    public void onError(String error) {

                    }

                    @Override
                    public void onImageSuccess(String label, Bitmap result) {
                        iv_sticker.setImageBitmap(getCircleBitmap(result));
                        tv_user.setVisibility(VISIBLE);
                    }
                }, url);
                tv_user.setText(settings.getString(fb_userName,""));
            }
            else{
                tv_user.setVisibility(GONE);
                bt_setDepart.setVisibility(GONE);
            }
        }

        public Bitmap getCircleBitmap(Bitmap bitmap) {
            Bitmap output = Bitmap.createBitmap( bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas( output);

            final int color = 0xff424242;
            final Paint paint = new Paint();
            final Rect rect = new Rect( 0, 0, bitmap.getWidth(), bitmap.getHeight());

            paint.setAntiAlias( true);
            paint.setFilterBitmap( true);
            paint.setDither( true);
            canvas.drawARGB( 0, 0, 0, 0);
            paint.setColor( color);
            //在画布上绘制一个圆
            canvas.drawCircle( bitmap.getWidth() / 2, bitmap.getHeight() / 2, bitmap.getWidth() / 2, paint);
            paint.setXfermode( new PorterDuffXfermode( PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap( bitmap, rect, rect, paint);
            return output;
        }
    }

    public class PageTwoView extends PageView {
        public PageTwoView(Context context) {
            super(context);
            View view = LayoutInflater.from(context).inflate(R.layout.tab_2, null);
            addView(view);
            courseTableView = (CourseTableView) view.findViewById(R.id.ctv);
            courseTableView.updateCourseViews(userCourseList);
            courseTableView.setOnCourseItemClickListener(MainActivity.this);


            menu = (FloatingActionMenu) view.findViewById(R.id.menu);
            FloatingActionButton floatingActionButton = (FloatingActionButton) view.findViewById(R.id.menu_item);
            floatingActionButton.setOnClickListener(MainActivity.this);


        }
    }

    public class PageThreeView extends PageView {
        public PageThreeView(Context context) {
            super(context);
            View view = LayoutInflater.from(context).inflate(R.layout.tab_3, null);

            //rabbit
            bt_navigation = (ImageButton) view.findViewById(R.id.bt_navigation);
            bt_navigation.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (singleMarker != null) ClickInfoWondows(singleMarker.getPosition());
                }
            });
            Animation fadeoutfirst = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fade_out_first);
            bt_navigation.startAnimation(fadeoutfirst);
            //////////////////////////////////

            addView(view);

        }
    }

    public class PageFourView extends PageView {
        public PageFourView(Context context) {
            super(context);
            View view = LayoutInflater.from(context).inflate(R.layout.tab_4, null);
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT);
            view.setLayoutParams(param);

            final String[][] courtInfo = new String[][]
                    {
                            {"田徑場", "排球場", "籃球場", "司令台後方廣場", "棒壘球場", "網球場", "溜冰場"},
                            {"綜合球場", "羽球場", "選手村", "桌球教室", "韻律教室", "柔道教室", "B2重訓室", "游泳館", "體適能中心", "視聽教室", "B1射箭場", "2F撞球桌"}
                    };
            final Spinner spinner1 = (Spinner) view.findViewById(R.id.spinner);
            ArrayAdapter<String> sp1 = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, new String[]{"室外場地", "體育場"});
            spinner1.setAdapter(sp1);
            final Spinner spinner2 = (Spinner) view.findViewById(R.id.spinner2);
            ArrayAdapter<String> sp2 = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, courtInfo[0]);
            spinner2.setAdapter(sp2);
            ListView lvCourt = (ListView) view.findViewById(R.id.lvCourt);
            View headerView = getLayoutInflater().inflate(R.layout.listview_header2, lvCourt, false);
            TextView tv_court_header = (TextView) headerView.findViewById(R.id.tv_court_header);
            Date date = new Date();
            int year = Integer.parseInt(new SimpleDateFormat("yyyy").format(date)) - 1911;
            final String month = new SimpleDateFormat("MM").format(date);
            String checkMonFir;
            String checkMonLast;
            if (Integer.parseInt(courseTableView.getOneWeekDatesOfMonth()[0]) < 10)
                checkMonFir = "0" + courseTableView.getOneWeekDatesOfMonth()[0];
            else
                checkMonFir = courseTableView.getOneWeekDatesOfMonth()[0];
            if (Integer.parseInt(courseTableView.getOneWeekDatesOfMonth()[6]) < 10)
                checkMonLast = "0" + courseTableView.getOneWeekDatesOfMonth()[6];
            else
                checkMonLast = courseTableView.getOneWeekDatesOfMonth()[6];

            final String courtDate = year + month + checkMonFir + "-" + year + month + checkMonLast;
            String headerInfo = "場地資料日期：" + courtDate;
            tv_court_header.setText(headerInfo);
            lvCourt.addHeaderView(headerView);
            final ArrayList<String> court_data = new ArrayList<>();
            final ArrayAdapter<String> court = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, court_data);
            lvCourt.setAdapter(court);
            spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    ArrayAdapter<String> temp = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, courtInfo[position]);
                    spinner2.setAdapter(temp);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            final Spinner spinner3 = (Spinner) view.findViewById(R.id.spinner3);
            final TextView tv_choseCourt = (TextView) view.findViewById(R.id.tv_choseCourt);

            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(final AdapterView<?> parent, View view, int position, long id) {
                    String courtType = parent.getSelectedItem().toString();
                    try {
                        court_data.clear();
                        if (courtType.matches("排球場|籃球場|網球場|綜合球場|羽球場|視聽教室")) {
                            spinner3.setVisibility(VISIBLE);
                            tv_choseCourt.setVisibility(VISIBLE);
                            httpRequest.postCourt(new HttpRequest.VolleyCallback() {
                                @Override
                                public void onSuccess(String label, String result) {
                                    try {
                                        JSONArray jsonArray = new JSONArray(result);
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject temp = jsonArray.getJSONObject(i);
                                            if (temp.getString("name").equals(parent.getSelectedItem().toString())) {
                                                final JSONArray state = temp.getJSONArray("state");
                                                final List<String> multiCourt = new ArrayList<>();
                                                for (int j = 0; j < state.length(); j++) {
                                                    String innerCourt = state.getJSONObject(j).getString("court");
                                                    multiCourt.add(innerCourt);
                                                }
                                                ArrayAdapter<String> sp3 = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, multiCourt);
                                                spinner3.setAdapter(sp3);
                                                spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                    @Override
                                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                        court_data.clear();
                                                        String seleCourt = parent.getSelectedItem().toString();
                                                        try {
                                                            for (int x = 0; x < state.length(); x++) {
                                                                if (state.getJSONObject(x).getString("court").equals(seleCourt)) {
                                                                    JSONArray innerState = state.getJSONObject(x).getJSONArray("state");
                                                                    for (int j = 0; j < 7; j++) {
                                                                        String info = month + "/" + courseTableView.getOneWeekDatesOfMonth()[j] + "\n\t";
                                                                        if (innerState.getString(j).equals(""))
                                                                            court_data.add(info + "////////無借用資訊////////");
                                                                        else
                                                                            court_data.add(info + innerState.getString(j));
                                                                    }
                                                                    court.notifyDataSetChanged();
                                                                }
                                                            }
                                                        } catch (JSONException e) {
                                                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    }

                                                    @Override
                                                    public void onNothingSelected(AdapterView<?> parent) {
                                                        court_data.clear();
                                                        String seleCourt = parent.getSelectedItem().toString();
                                                        try {
                                                            for (int x = 0; x < state.length(); x++) {
                                                                if (state.getJSONObject(x).getString("court").equals(seleCourt)) {
                                                                    JSONArray innerState = state.getJSONObject(x).getJSONArray("state");
                                                                    for (int j = 0; j < 7; j++) {
                                                                        String info = month + "/" + courseTableView.getOneWeekDatesOfMonth()[j] + "\n\t";
                                                                        if (innerState.getString(j).equals(""))
                                                                            court_data.add(info + "////////無借用資訊////////");
                                                                        else
                                                                            court_data.add(info + innerState.getString(j));
                                                                    }
                                                                    court.notifyDataSetChanged();
                                                                }
                                                            }
                                                        } catch (JSONException e) {
                                                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                            }
                                        }
                                    } catch (JSONException e) {
                                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                    court.notifyDataSetChanged();
                                }

                                @Override
                                public void onError(String error) {
                                    Toast.makeText(MainActivity.this, "伺服器出錯啦><\n" + error, Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onImageSuccess(String label, Bitmap result) {

                                }
                            }, courtDate);
                        } else {
                            spinner3.setVisibility(GONE);
                            tv_choseCourt.setVisibility(GONE);
                            httpRequest.postCourt(new HttpRequest.VolleyCallback() {
                                @Override
                                public void onSuccess(String label, String result) {
                                    try {
                                        JSONArray jsonArray = new JSONArray(result);
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject temp = jsonArray.getJSONObject(i);
                                            if (temp.getString("name").equals(parent.getSelectedItem().toString())) {
                                                if (!parent.getSelectedItem().toString().equals("桌球教室")) {
                                                    JSONArray state = temp.getJSONArray("state");
                                                    for (int j = 0; j < 7; j++) {
                                                        String info = month + "/" + courseTableView.getOneWeekDatesOfMonth()[j] + "\n\t";
                                                        if (state.getString(j).equals(""))
                                                            court_data.add(info + "////////無借用資訊////////");
                                                        else
                                                            court_data.add(info + state.getString(j));
                                                    }
                                                } else {
                                                    JSONArray state = temp.getJSONArray("state").getJSONObject(0).getJSONArray("state");
                                                    for (int j = 0; j < 7; j++) {
                                                        String info = month + "/" + courseTableView.getOneWeekDatesOfMonth()[j] + "\n\t";
                                                        if (state.getString(j).equals(""))
                                                            court_data.add(info + "////////無借用資訊////////");
                                                        else
                                                            court_data.add(info + state.getString(j));
                                                    }
                                                }
                                            }
                                        }
                                    } catch (JSONException e) {
                                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                    court.notifyDataSetChanged();
                                }

                                @Override
                                public void onError(String error) {
                                    Toast.makeText(MainActivity.this, "伺服器出錯啦><\n" + error, Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onImageSuccess(String label, Bitmap result) {

                                }
                            }, courtDate);
                        }
                        Toast.makeText(MainActivity.this, "選了" + courtType, Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onNothingSelected(final AdapterView<?> parent) {
                    String courtType = parent.getSelectedItem().toString();
                    try {
                        court_data.clear();
                        if (courtType.matches("排球場|籃球場|網球場|綜合球場|羽球場|視聽教室")) {
                            spinner3.setVisibility(VISIBLE);
                            tv_choseCourt.setVisibility(VISIBLE);
                            httpRequest.postCourt(new HttpRequest.VolleyCallback() {
                                @Override
                                public void onSuccess(String label, String result) {
                                    try {
                                        JSONArray jsonArray = new JSONArray(result);
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject temp = jsonArray.getJSONObject(i);
                                            if (temp.getString("name").equals(parent.getSelectedItem().toString())) {
                                                final JSONArray state = temp.getJSONArray("state");
                                                final List<String> multiCourt = new ArrayList<>();
                                                for (int j = 0; j < state.length(); j++) {
                                                    String innerCourt = state.getJSONObject(j).getString("court");
                                                    multiCourt.add(innerCourt);
                                                }
                                                ArrayAdapter<String> sp3 = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, multiCourt);
                                                spinner3.setAdapter(sp3);
                                                spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                    @Override
                                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                        court_data.clear();
                                                        String seleCourt = parent.getSelectedItem().toString();
                                                        try {
                                                            for (int x = 0; x < state.length(); x++) {
                                                                if (state.getJSONObject(x).getString("court").equals(seleCourt)) {
                                                                    JSONArray innerState = state.getJSONObject(x).getJSONArray("state");
                                                                    for (int j = 0; j < 7; j++) {
                                                                        String info = month + "/" + courseTableView.getOneWeekDatesOfMonth()[j] + "\n\t";
                                                                        if (innerState.getString(j).equals(""))
                                                                            court_data.add(info + "////////無借用資訊////////");
                                                                        else
                                                                            court_data.add(info + innerState.getString(j));
                                                                    }
                                                                    court.notifyDataSetChanged();
                                                                }
                                                            }
                                                        } catch (JSONException e) {
                                                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    }

                                                    @Override
                                                    public void onNothingSelected(AdapterView<?> parent) {
                                                        court_data.clear();
                                                        String seleCourt = parent.getSelectedItem().toString();
                                                        try {
                                                            for (int x = 0; x < state.length(); x++) {
                                                                if (state.getJSONObject(x).getString("court").equals(seleCourt)) {
                                                                    JSONArray innerState = state.getJSONObject(x).getJSONArray("state");
                                                                    for (int j = 0; j < 7; j++) {
                                                                        String info = month + "/" + courseTableView.getOneWeekDatesOfMonth()[j] + "\n\t";
                                                                        if (innerState.getString(j).equals(""))
                                                                            court_data.add(info + "////////無借用資訊////////");
                                                                        else
                                                                            court_data.add(info + innerState.getString(j));
                                                                    }
                                                                    court.notifyDataSetChanged();
                                                                }
                                                            }
                                                        } catch (JSONException e) {
                                                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                            }
                                        }
                                    } catch (JSONException e) {
                                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                    court.notifyDataSetChanged();
                                }

                                @Override
                                public void onError(String error) {
                                    Toast.makeText(MainActivity.this, "伺服器出錯啦><\n" + error, Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onImageSuccess(String label, Bitmap result) {

                                }
                            }, courtDate);
                        } else {
                            spinner3.setVisibility(GONE);
                            tv_choseCourt.setVisibility(GONE);
                            httpRequest.postCourt(new HttpRequest.VolleyCallback() {
                                @Override
                                public void onSuccess(String label, String result) {
                                    try {
                                        JSONArray jsonArray = new JSONArray(result);
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject temp = jsonArray.getJSONObject(i);
                                            if (temp.getString("name").equals(parent.getSelectedItem().toString())) {
                                                if (!parent.getSelectedItem().toString().equals("桌球教室")) {
                                                    JSONArray state = temp.getJSONArray("state");
                                                    for (int j = 0; j < 7; j++) {
                                                        String info = month + "/" + courseTableView.getOneWeekDatesOfMonth()[j] + "\n\t";
                                                        if (state.getString(j).equals(""))
                                                            court_data.add(info + "////////無借用資訊////////");
                                                        else
                                                            court_data.add(info + state.getString(j));
                                                    }
                                                } else {
                                                    JSONArray state = temp.getJSONArray("state").getJSONObject(0).getJSONArray("state");
                                                    for (int j = 0; j < 7; j++) {
                                                        String info = month + "/" + courseTableView.getOneWeekDatesOfMonth()[j] + "\n\t";
                                                        if (state.getString(j).equals(""))
                                                            court_data.add(info + "////////無借用資訊////////");
                                                        else
                                                            court_data.add(info + state.getString(j));
                                                    }
                                                }
                                            }
                                        }
                                    } catch (JSONException e) {
                                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                    court.notifyDataSetChanged();
                                }

                                @Override
                                public void onError(String error) {
                                    Toast.makeText(MainActivity.this, "伺服器出錯啦><\n" + error, Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onImageSuccess(String label, Bitmap result) {

                                }
                            }, courtDate);
                        }
                        Toast.makeText(MainActivity.this, "預設" + courtType, Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
            addView(view);
        }
    }

    public class PageFiveView extends PageView {
        public PageFiveView(Context context) {
            super(context);
            View view = LayoutInflater.from(context).inflate(R.layout.tab_5, null);
            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.reyview);
            LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
            recyclerView.setLayoutManager(layoutManager);
            List<String> list = new ArrayList<>();
            list.add("校園版");
            list.add("有趣版");
            list.add("運動版");
            list.add("課程版");
            list.add("寵物版");
            list.add("男孩版");
            list.add("女孩版");
            list.add("廢文版");
            list.add("升學版");
            list.add("3C版");
            list.add("社團版");
            TopicChatAdapter topicChatAdapter = new TopicChatAdapter(list, MainActivity.this);
            recyclerView.setAdapter(topicChatAdapter);
            List<String> courseList = new ArrayList<>();
            for (int i = 0; i < userCourseList.size(); i++) {
                courseList.add(userCourseList.get(i).getname());
            }
            ListView lv_course_chat = (ListView) view.findViewById(R.id.lv_course_chat);
            View headerView = getLayoutInflater().inflate(R.layout.listview_header, lv_course_chat, false);
            lv_course_chat.addHeaderView(headerView);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, courseList);
            lv_course_chat.setAdapter(adapter);
            lv_course_chat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String name = parent.getItemAtPosition(position).toString();
                    int index = 0;
                    for (int i = 0; i < userCourseList.size(); i++) {
                        String temp = userCourseList.get(i).getname();
                        if (name.equals(temp))
                            index = i;
                    }
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    if(settings.getString(fb_userName,"").equals("")) {
                        chatSocket.connect("yuntech_" + userCourseList.get(index).getserial(), "匿名_" + Build.SERIAL);
                        bundle.putString("username", "匿名_" + Build.SERIAL);
                    }else {
                        chatSocket.connect("yuntech_" + userCourseList.get(index).getserial(), settings.getString(fb_userName, ""));
                        bundle.putString("username", settings.getString(fb_userName, ""));
                    }
                    bundle.putString("roomID", userCourseList.get(index).getserial() + " " + userCourseList.get(index).getname());

                    intent.putExtras(bundle);
                    intent.setClass(MainActivity.this, ChatActivity.class);
                    startActivity(intent);
                }
            });
            addView(view);
        }
    }

    public class PersonalOneView extends PageView {
        public PersonalOneView(Context context) {
            super(context);
            View view = LayoutInflater.from(context).inflate(R.layout.personal1, null);
            List<String> p1List = new ArrayList<>();

            for (int i = 0; i < userCourseList.size(); i++) {
                String serial = userCourseList.get(i).getserial();
                String name = userCourseList.get(i).getname();
                String teather = userCourseList.get(i).getteacher();
                p1List.add(serial + " " + name);
            }

            ListView person1 = (ListView) view.findViewById(R.id.lvPerson1);

            ArrayAdapter p1 = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, p1List);

            person1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // Toast.makeText(MainActivity.this,"點擊第"+(position+1)+"個item\n"+ userCourseList.get(position).getserial()+ userCourseList.get(position).getname(),Toast.LENGTH_SHORT).show();
                    String serial = userCourseList.get(position).getserial();
                    String name = userCourseList.get(position).getname();
                    Bundle bundle = new Bundle();
                    bundle.putString("serial", serial);
                    bundle.putString("name", name);
                    Intent intent = new Intent(MainActivity.this, PersonalNoteList.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
            person1.setAdapter(p1);
            addView(view);
        }
    }

    public class PersonalTwoView extends PageView {
        public PersonalTwoView(Context context) {
            super(context);
            View view = LayoutInflater.from(context).inflate(R.layout.personal2, null);
            List<String> p2List = new ArrayList<>();
            for (int i = 0; i < userCourseList.size(); i++) {
                String serial = userCourseList.get(i).getserial();
                String name = userCourseList.get(i).getname();
                String teather = userCourseList.get(i).getteacher();
                p2List.add(serial + " " + name);
            }

            ListView person2 = (ListView) view.findViewById(R.id.lvPerson2);
            ArrayAdapter p2 = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, p2List);

            person2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(MainActivity.this, "點擊第" + (position + 1) + "個item\n" + userCourseList.get(position).getserial() + userCourseList.get(position).getname(), Toast.LENGTH_SHORT).show();
                    String serial = userCourseList.get(position).getserial();
                    Bundle bundle = new Bundle();
                    bundle.putString("serial", serial);
                    Intent intent = new Intent(MainActivity.this, SharedNotelist.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });

            person2.setAdapter(p2);
            addView(view);
        }
    }

    //////////
    //rabbit
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng yuntech = new LatLng(23.6951701, 120.5337975);
        addArea();
        //init_marker();
        singleMarker = null;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
                Toast.makeText(MainActivity.this, marker.getTitle() + " 長按", Toast.LENGTH_SHORT).show();
            }
        });
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Toast.makeText(MainActivity.this, marker.getTitle() + " 點擊", Toast.LENGTH_SHORT).show();
                ClickInfoWondows(marker.getPosition());
            }
        });
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        if (singleMarker == null) {
            Animation fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in);
            bt_navigation.startAnimation(fadein);
        }
        singleMarker = marker;
        String tag = marker.getTag().toString();
        Toast.makeText(MainActivity.this, tag, Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if (null != singleMarker) {
            Toast.makeText(MainActivity.this, singleMarker.getTitle(), Toast.LENGTH_SHORT).show();
            Animation fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out);
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
                storeCourse();
                finish(); // 離開程式
                System.exit(0);
            }
        }
        //}
        return false;
    }

    @Override
    protected void onStop() {
        storeCourse();
        super.onStop();
    }

    public void storeCourse() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < userCourseList.size(); i++) {
            String serial = userCourseList.get(i).getserial();
            String classfor = userCourseList.get(i).getclass_for();
            if (serial != null || classfor != null)
                sb.append(serial).append(",").append(classfor).append(",");
        }
        settings.edit().putString(courseData, sb.toString()).apply();
    }

    public void rabbit() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        settings.edit()
                .putBoolean(bicycle_state, true)
                .putBoolean(montor_state, true)
                .putBoolean(trash_state, true)
                .putBoolean(toilet_state, true)
                .putBoolean(college_state, true)
                .apply();

        buildkind = CommonMethod.Buildingkind();
        mapArea = CommonMethod.BuildingArea();

        //建立Request物件
        chatSocket = new ChatSocket(socketCallback);
        boolean courseReady = settings.getBoolean(isCourseDataReady, false);
        if (!checkCourseData() || !courseReady) {
            downloadCourse();
        }
    }

    private void downloadCourse() {
        final boolean[] check = {false};
        new AlertDialog.Builder(MainActivity.this) //宣告對話框物件，並顯示課程資料
                .setTitle("初次使用")
                .setMessage("是否下載課程資料?")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setPositiveButton("下載", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        check[0] = true;
                        //使用方法取課表
                        httpRequest.getCourse(callback);
                        proDialog = ProgressDialog.show(MainActivity.this, "請稍候", "正在為您下載課程資料...", true);
                    }
                })
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        if (!check[0]) {
                            Toast.makeText(MainActivity.this, "無課程資料將使部分功能無法使用", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).show();
    }

    private boolean checkCourseData() {
        SQLiteDatabase checkDB;
        try {
            checkDB = SQLiteDatabase.openDatabase(getFilesDir() + "class_database.db", null,
                    SQLiteDatabase.OPEN_READONLY);
            checkDB.close();
            Log.e("database", "db exsist");
        } catch (SQLiteException e) {
            Log.e("database", e.getMessage());
            return false;
        }
        return true;
    }

    private void analysis_course(String course_data) {
        ArrayList<Course> courseArray = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(course_data);
            String index[] = {"empty", "W", "X", "A", "B", "C", "D", "Y", "E", "F", "G", "H", "Z", "I"};
            for (int i = 0; i < array.length(); i++) {
                String serial = array.getJSONObject(i).optString("serial");
                String name = array.getJSONObject(i).optString("name");
                String schedule = array.getJSONObject(i).optString("schedule");
                int schedule_index = 0;
                if (!schedule.equals("")) {
                    schedule_index = Arrays.asList(index).indexOf(schedule.substring(0, 1));
                }
                String room = array.getJSONObject(i).optString("room");
                String teacher = array.getJSONObject(i).optString("teacher");
                String day = array.getJSONObject(i).optString("day");
                int day_index = 0;
                if (!day.equals("")) {
                    day_index = Integer.parseInt(day);
                }
                int spanNum = array.getJSONObject(i).optInt("class_span");
                String require = array.getJSONObject(i).optString("require");
                String classfor = array.getJSONObject(i).optString("class");
                String name_eng = array.getJSONObject(i).optString("name_eng");
                String credits = array.getJSONObject(i).optString("credits");
                String require_eng = array.getJSONObject(i).optString("require_eng");
                Course course = new Course();
                course.setserial(serial)
                        .setname(name)
                        .setschedule(schedule_index)
                        .setroom(room)
                        .setteacher(teacher)
                        .setday(day_index)
                        .setrequire(require)
                        .setSpanNum(spanNum)
                        .setschedule_display(schedule)
                        .setclassfor(classfor)
                        .setname_eng(name_eng)
                        .setcredits(credits)
                        .setrequire_eng(require_eng);
                courseArray.add(course);
            }
            Log.d("Course", "OK");
            createCourseDatabase(courseArray);
        } catch (JSONException e) {
            proDialog.dismiss();
            Toast.makeText(MainActivity.this, "課程資料分析出錯QQ", Toast.LENGTH_SHORT).show();
            Log.e("analysis_course", e.getMessage());
        }
    }

    public void createCourseDatabase(ArrayList<Course> data) {
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(getFilesDir() + "class_database.db", null);
        try {
            String createSql = "create table tb_course (_id integer primary key autoincrement" +
                    ",name varchar(50)" +
                    ",name_eng varchar(50)" +
                    ",serial varchar(10)" +
                    ",room varchar(10)" +
                    ",classfor varchar(10)" +
                    ",schedule varchar(5)" +
                    ",SpanNum varchar(5)" +
                    ",teacher varchar(50)" +
                    ",require varchar(50)" +
                    ",require_eng varchar(10)" +
                    ",credits varchar(10)" +
                    ",day varchar(2)" +
                    ",schedule_display varchar(10)" +
                    ")";
            db.execSQL(createSql);

            String insertSql = "insert into tb_course values (null,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            for (int i = 0; i < data.size(); i++) {
                Course obj = data.get(i);
                db.execSQL(insertSql,
                        new String[]{obj.getname(),
                                obj.getname_eng(),
                                obj.getserial(),
                                obj.getroom(),
                                obj.getclass_for(),
                                obj.getschedule() + "",
                                obj.getSpanNum() + "",
                                obj.getteacher(),
                                obj.getrequire(),
                                obj.getrequire_eng(),
                                obj.getcredits(),
                                obj.getday() + "",
                                obj.getschedule_display()});
            }
            proDialog.dismiss();
            settings.edit().putBoolean(isCourseDataReady, true).apply();
            Log.d("course", "data create suceess");
        } catch (SQLiteException e) {
            proDialog.dismiss();
            Log.d("SQLite", "資料庫建立出錯....");
        }

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

    private void init_marker() {
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
            if (floorinfo != null) {
                ListView lvSnippet = ((ListView) infoWindow.findViewById(R.id.lvFloor));
                adFloor = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1);
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
                    for (int j = 0; j < parsefloor.length(); j++) {
                        String floor_lv_string = "";
                        String floor_num = parsefloor.getJSONObject(j).getString("floor_num");
                        floor_lv_string += floor_num;
                        JSONArray floor_class = new JSONArray(parsefloor.getJSONObject(j).getJSONArray("classroom").toString());
                        for (int k = 0; k < floor_class.length(); k++) {
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

    private void ClickInfoWondows(final LatLng latlng) {
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

    public void check_permission() {
        int location = ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (location != PackageManager.PERMISSION_GRANTED) { //檢查是否有權限
            ActivityCompat.requestPermissions( //如果沒有就跟使用者要求
                    MainActivity.this,
                    new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE, ACCESS_FINE_LOCATION}, REQUEST_PERMISSION
            );
        }
    }

    public void checkGPS() {
        if (!isGPSEnabled(this)) {
            Toast.makeText(MainActivity.this, "請開始定位功能避免定位失效", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }
    }

    public static boolean isGPSEnabled(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
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
    public HttpRequest.VolleyCallback callback = new HttpRequest.VolleyCallback() {
        @Override
        public void onSuccess(String label, String result) {
            switch (label) {
                case "course":
                    analysis_course(result);
                    break;
            }
        }

        @Override
        public void onError(String error) {
            proDialog.dismiss();
            Toast.makeText(MainActivity.this, "伺服器出錯啦><\n" + error, Toast.LENGTH_SHORT).show();
            Log.d("Volley", error);
        }

        @Override
        public void onImageSuccess(String label, Bitmap result) {

        }
    };
    public ChatSocket.SocketCallback socketCallback = new ChatSocket.SocketCallback() {
        @Override
        public void onReceived(String result) {
            Log.d("Socket_Activity", result);
        }

        @Override
        public void onError(Exception err) {
            Log.d("Socket_Err_Activity", err.getMessage());
        }
    };

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
    //////////////////////
}