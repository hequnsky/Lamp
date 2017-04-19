package com.rmc.lamp.mvp.control.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rmc.lamp.mvp.main.ui.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DengActivity extends Activity implements View.OnTouchListener {
    private static final String TAG = "DengActivity";
    @Bind(R.id.ball_ll)
    ImageView ball_ll;
    Display display;
    @Bind(R.id.time)
    TextView time;
    @Bind(R.id.don)
    TextView don;
    @Bind(R.id.li_ll)
    FrameLayout li_ll;
    private MyCountDownTimer mc;
    private int topTitleHeight;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deng);
        ButterKnife.bind(this);
        time.setText("30s");
        display = getWindowManager().getDefaultDisplay();
        li_ll.setOnTouchListener(this);


//
//        ball_ll.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mc = new MyCountDownTimer(30000, 1000);
//                mc.start();
//                int height = display.getHeight();
//                Log.i(TAG, "onClick: " + height);
//                // 动画效果：上升效果
//                ObjectAnimator animUp = ObjectAnimator.ofFloat(ball_ll, "y", 1000, -height).setDuration(4000);
//                animUp.setInterpolator(new DecelerateInterpolator());
//                animUp.start();
//                animUp.addListener(new Animator.AnimatorListener() {
//                    @Override
//                    public void onAnimationStart(Animator animator) {
//
//                    }
//
//                    @Override
//                    public void onAnimationEnd(Animator animator) {
//
//                        ball_ll.setImageDrawable(getResources().getDrawable(R.mipmap.deng_h1));
//                    }
//
//                    @Override
//                    public void onAnimationCancel(Animator animator) {
//
//                    }
//
//                    @Override
//                    public void onAnimationRepeat(Animator animator) {
//
//
//                    }
//                });
//
//            }
//        });
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                moveViewWithFinger(ball_ll, event.getRawX(), event.getRawY());
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }

    class MyCountDownTimer extends CountDownTimer {
        /**
         * @param millisInFuture    表示以毫秒为单位 倒计时的总数
         *                          <p>
         *                          例如 millisInFuture=1000 表示1秒
         * @param countDownInterval 表示 间隔 多少微秒 调用一次 onTick 方法
         *                          <p>
         *                          例如: countDownInterval =1000 ; 表示每1000毫秒调用一次onTick()
         */
        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            time.setText("30s");
        }

        @Override
        public void onTick(long millisUntilFinished) {
            Log.i("MainActivity", millisUntilFinished + "");
            time.setText(millisUntilFinished / 1000 + "s");
        }
    }


    /**
     * 设置View的布局属性，使得view随着手指移动 注意：view所在的布局必须使用RelativeLayout 而且不得设置居中等样式
     *
     * @param view
     * @param rawX
     * @param rawY
     */
    private void moveViewWithFinger(View view, float rawX, float rawY) {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view
                .getLayoutParams();
        params.leftMargin = (int) rawX - ball_ll.getWidth() / 2;
        params.topMargin = (int) rawY - topTitleHeight - ball_ll.getHeight() / 2;
        view.setLayoutParams(params);
    }

    /**
     * 通过layout方法，移动view
     *
     * @param view
     * @param rawX
     * @param rawY
     */
    private void moveViewByLayout(View view, int rawX, int rawY, int scale) {
        int left = rawX - ball_ll.getWidth() / 2;
        int top = rawY - topTitleHeight - ball_ll.getHeight() / 2;
        int width = left + (int) (view.getWidth() + scale);
        int height = top + (int) (view.getHeight() + scale);
        view.layout(left, top, width, height);
    }

    private void moveViewByLayout(View view, int rawX, int rawY) {
        int left = rawX - ball_ll.getWidth() / 2;
        int top = rawY - topTitleHeight - ball_ll.getHeight() / 2;
        int width = left + view.getWidth();
        int height = top + view.getHeight();
        view.layout(left, top, width, height);
    }


    public void AutoView() {
        RelativeLayout mRelativeLayout = new RelativeLayout(this);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
    }

}
