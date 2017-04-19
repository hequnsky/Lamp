package com.rmc.lamp.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.rmc.lamp.mvp.main.ui.R;


/**
 * Created by Administrator on 15-5-22.
 */
public class ThermometeProgressBar extends View {
    private static final int WIDTH = 400;
    private static final int HEIGHT = 45;
    private static final int DIAMETER= 90;
    private float progress =  0;
    private float start;
    private Paint paint;
    private Paint textPaint;
    private float smallRoundScale;
    private float rectScale;
    private float bigRoundScale;
    private float overallWidht;
    private float textSize;
    public ThermometeProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint=new Paint();
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(R.color.progresbae_color);
        paint.setStrokeWidth(6.0f);
        textSize = DIAMETER/5;
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(R.color.progresbae_text_color);
        textPaint.setTextSize(textSize);
        start=6.0f+(DIAMETER-HEIGHT)/2;
        overallWidht = WIDTH+HEIGHT/2+DIAMETER*2/3;
        smallRoundScale = HEIGHT/2/overallWidht*100;
        bigRoundScale = DIAMETER*2/3/overallWidht*100;
        rectScale = WIDTH/overallWidht*100;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(WIDTH+DIAMETER+20,DIAMETER+20);
    }
    public void setProgress(float progress){
        this.progress = progress;
        invalidate();
    }
    public void startProgressBar(int time){
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0,100);
        valueAnimator.setDuration(time);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                progress = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimator.start();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        startProgressBar(10000);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        paint.setStyle(Paint.Style.STROKE);
        RectF oval = new RectF();
        oval.left = 6.0f;
        oval.right =HEIGHT;
        oval.top = start;
        oval.bottom =HEIGHT+start;
        canvas.drawArc(oval, 90, 180, false, paint);
        canvas.drawLine((HEIGHT+6)/2,start,WIDTH,start,paint);
        canvas.drawLine((HEIGHT+6)/2,HEIGHT+start,WIDTH,HEIGHT+start,paint);
        RectF oval1 = new RectF();
        oval1.left = start+WIDTH-DIAMETER/3-6.0f;
        oval1.right =start+WIDTH+DIAMETER*2/3-6.0f;
        oval1.top = 6.0f;
        oval1.bottom =DIAMETER;
        canvas.drawArc(oval1, 210, 300, false, paint);
        canvas.drawText((float)(Math.round(progress*10))/10+"%", oval1.left+textSize, oval1.top+DIAMETER/2,textPaint);
       if(progress>0) {
           paint.setStyle(Paint.Style.FILL);
           float tempSmallWidth = 180/smallRoundScale * progress;
           canvas.drawArc(oval, 90,tempSmallWidth<180?tempSmallWidth:180, false, paint);
           if(progress>smallRoundScale){
               float tempRectWidth = WIDTH/rectScale*(progress);
               canvas.drawRect((HEIGHT+6)/2,start,tempRectWidth<WIDTH?tempRectWidth:WIDTH,start+HEIGHT,paint);
           }
           if(progress>rectScale+smallRoundScale){
               float tempBigWidth =  300 / bigRoundScale * (progress - rectScale - smallRoundScale);
               if(progress<100) {
                   canvas.drawArc(oval1, 210,tempBigWidth < 280 ? tempBigWidth : 280, false, paint);
               }else{
                   canvas.drawArc(oval1, 210, 300, false, paint);
               }
           }
       }
    }
}
