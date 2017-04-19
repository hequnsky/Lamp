package com.rmc.lamp.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.rmc.lamp.bean.BalloonModel;
import com.rmc.lamp.mvp.main.ui.R;
import com.rmc.lamp.rx.RxBus;

import java.util.ArrayList;
import java.util.Random;

/**
 * 作者：hequnsky on 2016/7/22 10:09
 * 邮箱：hequnsky@gamil.com
 */

public class BalloonsView extends View {
    private ArrayList<BalloonModel> balloonModels;
    private Handler handler;
    private int screenWidth, screenHeight;
    private final int DEFAULT_RADII = 80;
    int[] bg_color = new int[]{getResources().getColor(R.color.silver),getResources().getColor(R.color.orange), getResources().getColor(R.color.yellow),
            getResources().getColor(R.color.green),getResources().getColor(R.color.lime),getResources().getColor(R.color.blue)
            ,getResources().getColor(R.color.pink),getResources().getColor(R.color.purple),getResources().getColor(R.color.silver)
            ,getResources().getColor(R.color.blue)
            ,getResources().getColor(R.color.purple)};

    public BalloonsView(Context context) {
        super(context);
        init();
    }

    public BalloonsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BalloonsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    //初始化画笔
    private void init() {
        balloonModels = new ArrayList<BalloonModel>();
        handler = new Handler();
        WindowManager wm = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        screenWidth = wm.getDefaultDisplay().getWidth();
        screenHeight = wm.getDefaultDisplay().getHeight();
        //设置颜色
        handler.post(createBalloonRunnable);
        handler.post(animationRunnable);
    }


    private void createBalloon() {
        synchronized (balloonModels) {


            Paint paint = new Paint();
            Random x = new Random();
            paint.setColor(bg_color[x.nextInt(9)]);
            paint.setStyle(Paint.Style.FILL);
            paint.setAntiAlias(true);
            Random rand = new Random();
            int startX = rand.nextInt(screenWidth - DEFAULT_RADII * 2);
            BalloonModel balloonModel = new BalloonModel();
            balloonModel.x = startX + DEFAULT_RADII;
            balloonModel.y = screenHeight;
            balloonModel.radii = DEFAULT_RADII;
            balloonModel.mPaint = paint;
            balloonModels.add(balloonModel);
        }
    }

    private Runnable animationRunnable = new Runnable() {
        @Override
        public void run() {
            synchronized (balloonModels) {
                boolean is = false;
                for (BalloonModel balloonModel : balloonModels) {
                    if (balloonModel.y < screenHeight / 3) {
                        balloonModel.y = balloonModel.y - 5;
                    } else {
                        balloonModel.y = balloonModel.y - 3;
                    }

                }
            }
            invalidate();
            handler.postDelayed(this, 1);
        }
    };

    private Runnable createBalloonRunnable = new Runnable() {
        @Override
        public void run() {
            createBalloon();
            //每500ms创建一个小球
            handler.postDelayed(this, 900);
        }
    };

    @Override
    protected void onDraw(Canvas canvas) {
        synchronized (balloonModels) {
            ArrayList<BalloonModel> temp = new ArrayList<BalloonModel>();
            for (BalloonModel balloonModel : balloonModels) {
                if (balloonModel.y > 0 - DEFAULT_RADII) {
                    if (balloonModel.isClick) {

//                        RectF rectf = new RectF((float) (balloonModel.x - DEFAULT_RADII * (balloonModel.life / 5)), (float) (balloonModel.y - DEFAULT_RADII - DEFAULT_RADII * (balloonModel.life / 5)),
//                                (float) (balloonModel.x + DEFAULT_RADII * 2 + DEFAULT_RADII * (balloonModel.life / 5)), (float) (balloonModel.y + DEFAULT_RADII + DEFAULT_RADII * (balloonModel.life / 5)));
//                        RectF rectf = new RectF((float)(balloonModel.x+DEFAULT_RADII),(float)(balloonModel.y+DEFAULT_RADII),(float)(balloonModel.x+DEFAULT_RADII),(float)(balloonModel.y+DEFAULT_RADII));
                        RectF rectf = new RectF(0,0,0,0);
                        canvas.drawOval(rectf, balloonModel.mPaint);

                        balloonModel.life = balloonModel.life + 1;
                        if (balloonModel.life >= 8) {
                            temp.add(balloonModel);
                        }
                        int color = balloonModel.mPaint.getColor();
                        String getcolor = ("#" + Integer.toHexString(color));
                        RxBus.getInstance().post("setColor", getcolor);
                    } else {
                        RectF rectf = new RectF(balloonModel.x, balloonModel.y - DEFAULT_RADII, balloonModel.x + DEFAULT_RADII * 2, balloonModel.y + DEFAULT_RADII);
                        canvas.drawOval(rectf, balloonModel.mPaint);
                    }

                } else {
                    temp.add(balloonModel);
                }
            }
            balloonModels.removeAll(temp);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                synchronized (balloonModels) {
                    float x = event.getX();
                    float y = event.getY();
                    for (BalloonModel balloonModel : balloonModels) {
                        if (x > balloonModel.x && x < balloonModel.x + DEFAULT_RADII *2.5 && y > balloonModel.y && y < balloonModel.y + DEFAULT_RADII *2.5) {
                            balloonModel.isClick = true;
                        }
                    }
                }
                break;
        }
        return super.onTouchEvent(event);
    }


}
