package net.kaijie.campus_ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by pc on 2017/9/23.
 */

public class CourseTableView extends RelativeLayout {
    // 課程格子有課程的背景圖
    private static final int[] COURSE_BG = {R.drawable.course_info_light_blue, R.drawable.course_info_green,
            R.drawable.course_info_red, R.drawable.course_info_blue, R.drawable.course_info_yellow,
            R.drawable.course_info_orange, R.drawable.course_info_purple};
    private List<? extends Course> coursesData;
    //用来保存課程資訊息
    public void updateCourseViews(List<? extends Course> coursesData) {
        this.coursesData = coursesData;
        updateCourseViews();
    }

    private OnCourseItemClickListener onCourseItemClickListener;
    //點擊課程的監聽事件 設定TextView內容格式
    public interface OnCourseItemClickListener {
        void onCourseItemClick(TextView tv, int Classtimes, int Day, String Classroom, int Num, String Name,  String des);
    }


    private void updateCourseViews( ) {
        //在每次做更新操作时，先清除一下当前的已经添加上去的View
        clearViewsIfNeeded();
        FrameLayout fl;
        FrameLayout.LayoutParams flp;
        TextView tv;
        for (final Course c : coursesData) {
            //取得節次（等於行）
            final int Classtimes = c.getClasstimes();
            //取得星期（等於列）
            final int Day = c.getDay();

            final int Num = c.getNum();

            //外层包裹一个FrameLayout 方便为TextView设置padding，保证课程信息与边框有一定距离(2dp)
            fl = new FrameLayout(getContext());
            //设置课程信息的宽高，宽度就是列宽，高度是行高 * 跨度
            flp = new FrameLayout.LayoutParams(notFirstEveryColumnsWidth,
                    notFirstEveryRowHeight * c.getSpanNum());
            //设置横向和纵向的偏移量，和上面介绍的一致，但day和jieci都是从1开始的，需减1.
            flp.setMargins((Day - 1) * notFirstEveryColumnsWidth, (Classtimes - 1) * notFirstEveryRowHeight, 0, 0);
            fl.setLayoutParams(flp);
            fl.setPadding(twoW, twoW, twoW, twoW);

            tv = new TextView(getContext());
            flp = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);


            tv.setText( c.getClassroom()+ "\n"+ Num+ "  "+ c.getName()+ "\n" + c.getDes());//顯示內容


            tv.setTextColor(Color.WHITE);
            tv.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
            tv.setPadding(twoW, twoW, twoW, twoW);  //
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);

            //顯示不下的話，尾部以"..."顯示
            tv.setEllipsize(TextUtils.TruncateAt.END);
            //設定最大顯示7行
            tv.setLines(8);  //調格子內最大顯示行數 預設是7
            tv.setBackgroundResource(COURSE_BG[Day - 1]);
            tv.setLayoutParams(flp);
            tv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    //課程資訊設置點擊事件監聽
                    if (onCourseItemClickListener != null)
                        onCourseItemClickListener.onCourseItemClick((TextView) v, Classtimes, Day, c.getClassroom(), Num,c.getName(), c.getDes());//int Classtimes, int Day, String Classroom, int Num, String Name, String Teacher, String des
                }
            });
            fl.addView(tv);
            //对每个添加到布局中的课程信息View做一个保存，方便下次清除
            myCacheViews.add(fl);
            flCourseContent.addView(fl);
        }
    }
    private void clearViewsIfNeeded() {
        if (myCacheViews == null || myCacheViews.isEmpty())
            return;

        for (int i = myCacheViews.size() - 1; i >= 0; i--) {
            flCourseContent.removeView(myCacheViews.get(i));
            myCacheViews.remove(i);
        }
    }




    /**
     * 保存View 方便Remove
     */
    private List<View> myCacheViews = new ArrayList<View>();

    //init  布局  日期部分
    //今天是星期幾(星期日=0 星期一=1.....星期六=6)
    private int todayNum;
    //和星期數對應
    private int[] US_DAYS_NUMS = { 7, 1, 2, 3, 4, 5, 6};
    //星期數對應日期
    private String[] datesOfMonth;


    //CourseTableView 布局

    private int totalDay = 7;

    private int totalClasstime = 16;

    //getOneWeekDatesOfMonth 布局
    //左上角的TextView 顯示當周的周一所屬的月份
    private String preMonth;
    /**
         * 讀取以今天為基準,周一到周日為這個月的幾號
         * @return
        */

    //drawFrame 布局 畫格子大小
    //第一行的高度
    private int firstRowHeight;
    //除了第一行 每一行的高度
    private int notFirstEveryRowHeight;
    //第一列的寬度
    private int firstColumnWidth;
    //除了第一列 每一列的宽度
    private int notFirstEveryColumnsWidth;

    /**
        * 繪製初始化的第一個表格
        */
    private TextView firstTv;
    //2dp
    private int twoW = DensityUtils.dip2px(getContext(), 2);
    //20dp
    private int twenty = DensityUtils.dip2px(getContext(), 20);
    //1dp
    private int oneW = DensityUtils.dip2px(getContext(), 1);

    private static final int FIRST_TV = 555;
    /**
     * 繪製初始化的第一個格子右邊的部分
     */
    private static final int FIRST_ROW_TV_QZ = 3;
    private String[] DAYS = {"週一", "週二", "週三", "週四", "週五", "週六", "週日"};

    //ScrollView 課程格子部分的父布局
    private FrameLayout flCourseContent;
    /**
        *
        *
        */

    public CourseTableView(Context context,AttributeSet attrs){
        this(context, attrs, 0);
    }
    public CourseTableView(Context context) {
        this(context, null);
    }
    public CourseTableView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        final TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CourseTable, defStyleAttr,
                0);
        totalDay = ta.getInt(R.styleable.CourseTable_totalDays, 7);
        totalClasstime = ta.getInt(R.styleable.CourseTable_totalClasstime, 16);
        ta.recycle();
        init(context);
        //繪製整個課程布局框架
        drawFrame();
    }
    public void setTotalClasstime(int totalClasstime) {
        this.totalClasstime= totalClasstime;
        refreshCurrentLayout();
    }
    public void setTotalDay(int totalDay) {
        this.totalDay = totalDay;
        refreshCurrentLayout();
    }

    /**
     *   更新 操作後的畫面   加入自己課程部分
     */





    /**
        *
        *
        */
    private void init(Context context) {
        Calendar toDayCal = Calendar.getInstance();
        //讀取今天的日期
        toDayCal.setTimeInMillis(System.currentTimeMillis());
        //讀取今天星期幾    (此處星期為美曆)
        toDayCal.get(Calendar.DAY_OF_WEEK);
        //轉換 台灣日曆 周日=1 , 周一=2 , .....周六=7
        todayNum = toDayCal.get(Calendar.DAY_OF_WEEK)-1;
        //得到當周的日期
        datesOfMonth = getOneWeekDatesOfMonth();

    }
    /**
        *
        *
        */
    public String[] getOneWeekDatesOfMonth() {
        Calendar tempCal= Calendar.getInstance();
        //存儲日期
        String[] temp = new String[totalDay];
        //得知台灣的周幾
        int TW_Days_Nums =  US_DAYS_NUMS[todayNum];
        //如果今天不是周日 ，代表美曆的下周尚未開始 ，美曆的周一=台灣的周一
        if (TW_Days_Nums != 7) {
            tempCal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        } else {
            //如果是周日，代表美曆已經是下周的周一了 於是跳回上週
            tempCal.add(Calendar.WEEK_OF_MONTH, -1);
            //到上周後 設置周一
            tempCal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        }

        int ds = 0;//變數記錄周一是幾號
        for (int i = 1; i < totalDay; i++) {
            if (i == 1) {//如果今天為周一
                ds = tempCal.get(Calendar.DAY_OF_MONTH);
                //設定周一是幾號
                temp[i - 1] = tempCal.get(Calendar.DAY_OF_MONTH) + "";
                //紀錄周一對應到的月份
                preMonth = (tempCal.get(Calendar.MONTH) + 1) + "月";
            }
            //往後加一天
            tempCal.add(Calendar.DATE, 1);
            //如果筆記錄到的日期小 代表已經進入下個月了
            if (tempCal.get(Calendar.DAY_OF_MONTH) < ds) {
                //則不顯示日期，顯示這天的月份
                temp[i] = (tempCal.get(Calendar.MONTH) + 1) + "月";
                //重新給ds值 ，
                ds = tempCal.get(Calendar.DAY_OF_MONTH);
            } else {
                //其他情況顯示對應的日期
                temp[i] = tempCal.get(Calendar.DAY_OF_MONTH) + "";
            }
        }
        //將結果數返回，可能的格式：{"30","31","9月","2","3","4","5"}
        return temp;
    }
    /**
         *
         *
         */

    private void drawFrame() {
        //初始化格子寬高大小
        initSize();
        // 繪製第一行
        drawFirstRow();
        // 繪製下面的畫面,整个下面是一ScrollView包裹一个個LinearLayout
        addBottomRestView();
    }

    /**
        *
        *
         */
    private void initSize() {
        int screenWidth = getScreenWidth();
        int screenHeight = getScreenHeight();
        //第一行高度为40dp，这个dp->px工具方法在上一篇有用到
        firstRowHeight = DensityUtils.dip2px(getContext(), 40);
        //此处解一个方程，设第一行非第一列格子宽度为x，最左边的格子为x/2，则totalDay*x+x/2 = screenHeight ;
        //x=notFirstEveryColumnsWidth ;
        notFirstEveryColumnsWidth = screenWidth * 2 / (2 * totalDay + 1);
        //第一列的宽度为x的一半
        firstColumnWidth = notFirstEveryColumnsWidth / 2;
        //非第一行，每一行的高度为屏幕的高度除以总节次+5dp
        notFirstEveryRowHeight = (screenHeight - firstRowHeight) / totalClasstime + DensityUtils.dip2px(getContext(), 20);// 預設是5   數字調每隔高度
    }
    public int getScreenWidth() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    public int getScreenHeight() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    /**
        * 繪製第一行
         */
    private void drawFirstRow() {
        //绘制左上角的TextView
        initFirstTv();
        //繪製剩下的東西，是LinearLayout包裹的
        initRestTv();
    }
    private void initFirstTv() {
        firstTv = new TextView(getContext());
        //設定一個Id，和布局文件的Id相同
        firstTv.setId(FIRST_TV);
        //設置布局參數 ，initSize() 已經算好
        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(firstColumnWidth, firstRowHeight);
        firstTv.setBackgroundResource(R.drawable.course_table_background);
        firstTv.setText(preMonth);
        firstTv.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
        firstTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
        firstTv.setPadding(oneW, twoW, oneW, twoW);
        firstTv.setLayoutParams(rlp);
        addView(firstTv);
    }
    private void initRestTv() {
        LinearLayout linearLayout;
        RelativeLayout.LayoutParams rlp;
        TextView textView;
        for (int i = 0; i < totalDay; i++) {
            //這裡使用LinearLayout(垂直)包裹兩個TextView
            linearLayout = new LinearLayout(getContext());
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            //設置一個Id，加上前綴以防止重複（不加也行）
            linearLayout.setId(FIRST_ROW_TV_QZ + i);
            //設置寬跟高
            rlp = new RelativeLayout.LayoutParams(notFirstEveryColumnsWidth,
                    firstRowHeight);
            //如果是第一個，则繪製在左上角第一個的TextView右測
            if (i == 0)
                rlp.addRule(RelativeLayout.RIGHT_OF, firstTv.getId());
                //剩下的都繪製在前一個的右側
            else
                rlp.addRule(RelativeLayout.RIGHT_OF, FIRST_ROW_TV_QZ + i - 1);
            linearLayout.setBackgroundResource(R.drawable.course_table_background);
            linearLayout.setLayoutParams(rlp);
            LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT);

            //上方顯示日期的TextView
            textView = new TextView(getContext());
            textView.setText(datesOfMonth[i]);
            textView.setLayoutParams(llp);
            textView.setGravity(Gravity.CENTER);
            textView.setPadding(twoW, twoW, twoW, twoW);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
            linearLayout.addView(textView);
            llp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            //下方顯示星期的TextView
            textView = new TextView(getContext());
            textView.setLayoutParams(llp);
            textView.setText(DAYS[i]);
            textView.setGravity(Gravity.CENTER | Gravity.BOTTOM);
            //在當天的這個格子做高亮的處理
            if (US_DAYS_NUMS[todayNum] - 1 == i) {
                linearLayout.setBackgroundColor(0x77069ee9);
            }
            textView.setPadding(twoW, 0, twoW, twoW * 2);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            linearLayout.addView(textView);
            addView(linearLayout);
        }
    }

    /**
        *
        *
        */
    private void addBottomRestView() {
        ScrollView sv = new ScrollView(getContext());
        LayoutParams rlp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        //其位置在左上角第一個的TextView下面
        rlp.addRule(RelativeLayout.BELOW, firstTv.getId());
        sv.setLayoutParams(rlp);
        //滾動條隱藏
        sv.setVerticalScrollBarEnabled(false);

        //包裹的LinearLayout（默認水平）
        LinearLayout llBottom = new LinearLayout(getContext());
        ViewGroup.LayoutParams vlp = new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        llBottom.setLayoutParams(vlp);

        //左侧使用LinearLayout（垂直），包裹節次的TextView
        LinearLayout llLeftCol = new LinearLayout(getContext());
        LinearLayout.LayoutParams llp1 = new LinearLayout.LayoutParams(firstColumnWidth, LayoutParams.WRAP_CONTENT);
        llLeftCol.setLayoutParams(llp1);
        llLeftCol.setOrientation(LinearLayout.VERTICAL);

        //初始化左側顯示節次的TextView
        initLeftTextViews(llLeftCol);
        llBottom.addView(llLeftCol);

        flCourseContent = new FrameLayout(getContext());
        LinearLayout.LayoutParams llp2 = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        flCourseContent.setLayoutParams(llp2);
        //先添加課程格子的邊框
        drawCourseFrame();
        llBottom.addView(flCourseContent);

        sv.addView(llBottom);

        addView(sv);
    }
    private void initLeftTextViews(LinearLayout llLeftCol) {

        LinearLayout.LayoutParams rlp = new LinearLayout.LayoutParams(firstColumnWidth, notFirstEveryRowHeight);
        TextView textView;


        int ch=65,other=0,clock=0;
        for (int i = 0; i < totalClasstime; i++) {

            textView = new TextView(getContext());
            textView.setLayoutParams(rlp);
            textView.setBackgroundResource(R.drawable.course_table_background);
            //顯示節次
            if(i==0){
                ch=87;
                other++;}
            if(i==1){ch=88;
                other++;}
            if(i==6){ch=89;
                other++;}
            if(i==11){ch=90;
                other++;}
            if(i!=0 && i!=1 && i!=6 && i!=11 ){
                ch=65+i-other;
            }
            char ch2 = (char) ch;

            textView.setText(""+ch2);
           // textView.setText(""+(i+1));
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(Color.GRAY);
            llLeftCol.addView(textView);
        }


    }
    private void drawCourseFrame() {
        FrameLayout fl;
        FrameLayout.LayoutParams flp;
        for (int i = 0; i < totalDay * totalClasstime; i++) {
            int row = i / totalDay;
            int col = i % totalDay;
            fl = new FrameLayout(getContext());
            //设置格子的大小
            flp = new FrameLayout.LayoutParams(notFirstEveryColumnsWidth,
                    notFirstEveryRowHeight);
            fl.setBackgroundResource(R.drawable.course_table_background);
            //这裡使用设置Margin值来確定每個格子的背景的位置
            //col(列數) * 列宽為格子左侧偏移量
            //row(行數) * 行高為格子上方偏移量
            //這樣就可以確定格子的位置（後面添加课程資訊 也用的這種方式）
            flp.setMargins(col * notFirstEveryColumnsWidth, row * notFirstEveryRowHeight, 0, 0);
            fl.setLayoutParams(flp);
            flCourseContent.addView(fl);
        }
    }


    /**
     *
     *
     */
    public void setOnCourseItemClickListener(OnCourseItemClickListener onCourseItemClickListener) {
        this.onCourseItemClickListener = onCourseItemClickListener;
    }
    //点击课程信息的监听事件

     /**
     * 保存View 方便Remove
     */
     private void refreshCurrentLayout() {
         removeAllViews();
         init(getContext());
         drawFrame();
         updateCourseViews();
     }

}
