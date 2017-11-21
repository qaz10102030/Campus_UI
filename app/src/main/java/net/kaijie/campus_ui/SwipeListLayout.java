package net.kaijie.campus_ui;


import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by pc on 2017/11/15.
 */

public class SwipeListLayout extends FrameLayout {
    private View hiddenView;
    private View itemView;
    private int hiddenViewWidth;
    private ViewDragHelper mDragHelper;
    private int hiddenViewHeight;
    private int itemWidth;
    private int itemHeight;
    private OnSwipeStatusListener listener;
    private Status status = Status.Close;
    private boolean smooth = true;

    public static final String TAG = "SlipListLayout";


    public enum Status {
        Open, Close
    }

    public void setStatus(Status status, boolean smooth) {
        this.status = status;
        if (status == Status.Open) {
            open(smooth);
        } else {
            close(smooth);
        }
    }

    public void setOnSwipeStatusListener(OnSwipeStatusListener listener) {
        this.listener = listener;
    }

    public void setSmooth(boolean smooth) {
        this.smooth = smooth;
    }

    public interface OnSwipeStatusListener {

        void onStatusChanged(Status status);

        /**
         * 執行open動畫
         */
        void onStartCloseAnimation();

        /**
         * 執行close動畫
         */
        void onStartOpenAnimation();
    }

    public SwipeListLayout(Context context) {
        super(context, null);
    }

    public SwipeListLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mDragHelper = ViewDragHelper.create(this, callback);
    }

    ViewDragHelper.Callback callback = new ViewDragHelper.Callback() {
        @Override
        public boolean tryCaptureView(View view, int arg1) {
            return view == itemView;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            if (child == itemView) {
                if (left > 0) {
                    return 0;
                } else {
                    left = Math.max(left, -hiddenViewWidth);
                    return left;
                }
            }
            return 0;
        }
        @Override
        public int getViewHorizontalDragRange(View child) {
            return hiddenViewWidth;
        }
        @Override
        public void onViewPositionChanged(View changedView, int left, int top,
                                          int dx, int dy) {
            if (itemView == changedView) {
                hiddenView.offsetLeftAndRight(dx);
            }
            // 防止滑太快沒有繪製，為了確定畫好 而用 invalidate
            invalidate();
        }
        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            // 向右滑xvel為正 向左滑xvel為負
            if (releasedChild == itemView) {
                if (xvel == 0
                        && Math.abs(itemView.getLeft()) > hiddenViewWidth / 2.0f) {
                    open(smooth);
                } else if (xvel < 0) {
                    open(smooth);
                } else {
                    close(smooth);
                }
            }
        }
    };
    private Status preStatus = Status.Close;
    private void close(boolean smooth) {
        preStatus = status;
        status = Status.Close;
        if (smooth) {
            if (mDragHelper.smoothSlideViewTo(itemView, 0, 0)) {
                if (listener != null) {
                    Log.i(TAG, "start close animation");
                    listener.onStartCloseAnimation();
                }
                ViewCompat.postInvalidateOnAnimation(this);
            }
        } else {
            layout(status);
        }
        if (listener != null && preStatus == Status.Open) {
            Log.i(TAG, "close");
            listener.onStatusChanged(status);
        }
    }
    private void layout(Status status) {
        if (status == Status.Close) {
            hiddenView.layout(itemWidth, 0, itemWidth + hiddenViewWidth,
                    itemHeight);
            itemView.layout(0, 0, itemWidth, itemHeight);
        } else {
            hiddenView.layout(itemWidth - hiddenViewWidth, 0, itemWidth,
                    itemHeight);
            itemView.layout(-hiddenViewWidth, 0, itemWidth - hiddenViewWidth,
                    itemHeight);
        }
    }

    private void open(boolean smooth) {
        preStatus = status;
        status = Status.Open;
        if (smooth) {
            if (mDragHelper.smoothSlideViewTo(itemView, -hiddenViewWidth, 0)) {
                if (listener != null) {
                    Log.i(TAG, "start open animation");
                    listener.onStartOpenAnimation();
                }
                ViewCompat.postInvalidateOnAnimation(this);
            }
        } else {
            layout(status);
        }
        if (listener != null && preStatus == Status.Close) {
            Log.i(TAG, "open");
            listener.onStatusChanged(status);
        }
    }
    @Override
    public void computeScroll() {
        super.computeScroll();
        // 開始執行動畫
        if (mDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }
    // touch事件
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();
        if (action == MotionEvent.ACTION_CANCEL) {
            mDragHelper.cancel();
            return false;
        }
        return mDragHelper.shouldInterceptTouchEvent(ev);
    }

    //touch事件
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDragHelper.processTouchEvent(event);
        return true;
    };

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        hiddenView = getChildAt(0); // 得到隱藏按鈕的linearlayout
        itemView = getChildAt(1); // 得到最上層的linearlayout
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // 測量item長寬大小
        itemWidth = itemView.getMeasuredWidth();
        itemHeight = itemView.getMeasuredHeight();
        hiddenViewWidth = hiddenView.getMeasuredWidth();
        hiddenViewHeight = hiddenView.getMeasuredHeight();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
        layout(Status.Close);
    }


}