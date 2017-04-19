package com.rmc.lamp.mvp.control.ui;

import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.rmc.lamp.mvp.main.ui.R;
import com.rmc.lamp.net.Client;

import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TranslationActivity extends Activity implements View.OnClickListener {
    @Bind(R.id.image_yao)
    ImageView image_yao;
    @Bind(R.id.dance)
    ImageView dance;
    @Bind(R.id.shake)
    ImageView shake;
    SensorManager sensorManager;
    Sensor sensor;
    Vibrator vibrator;
    // 速度阈值，当摇晃速度达到这值后产生作用
    private static final int SPEED_SHRESHOLD = 3000;
    // 两次检测的时间间隔
    private static final int UPTATE_INTERVAL_TIME = 70;
    private static final int INTERVAL_TIME = 1000;
    // 上次检测时间
    private long LastTime;
    private long LastTimes;

    int[] scolor = new int[]{R.color.silver, R.color.orange, R.color.yellow,
            R.color.green, R.color.lime, R.color.blue
            , R.color.pink, R.color.purple, R.color.silver
            , R.color.blue
            , R.color.purple};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translation);
        ButterKnife.bind(this);
        init();
        dance.setOnClickListener(this);
        shake.setOnClickListener(this);
    }

    /**
     * 初始化界面
     */
    private void init() {

        //获取传感器的管理对象
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        //获得一个具体的传感器对象
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        //获取震动的对象
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        image_yao.setBackgroundResource(R.mipmap.dance_main_pic);
        dance.setBackgroundResource(R.mipmap.dance_mini_pic_enable);
        shake.setBackgroundResource(R.mipmap.shake_mini_pic_disable);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dance:
                image_yao.setBackgroundResource(R.mipmap.dance_main_pic);
                dance.setBackgroundResource(R.mipmap.dance_mini_pic_enable);
                shake.setBackgroundResource(R.mipmap.shake_mini_pic_disable);
                break;
            case R.id.shake:
                image_yao.setBackgroundResource(R.mipmap.shake_main_pic);
                dance.setBackgroundResource(R.mipmap.dance_mini_pic_disable);
                shake.setBackgroundResource(R.mipmap.shake_mini_pic_enable);
                break;
            default:
                break;

        }
    }

    private SensorEventListener sensorEventListener = new SensorEventListener() {

        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            //两次传感器摇晃获取信息的时间间隔
            long NowTime = System.currentTimeMillis();
            long times = NowTime - LastTime;
            if ((times) < UPTATE_INTERVAL_TIME) {
                return;
            } else {
                Random random = new Random();
                String color = scolor[random.nextInt(9)] + "";
                sendTcp(color.substring(1, 7));
                LastTime = NowTime;
// 传感器信息改变时执行该方法
                float[] values = sensorEvent.values;
                float x = values[0]; // x轴方向的重力加速度，向右为正
                float y = values[1]; // y轴方向的重力加速度，向前为正
                float z = values[2]; // z轴方向的重力加速度，向上为正
                int medumValue = 10;
                if (Math.abs(x) > 8 || Math.abs(y) > medumValue || Math.abs(z) > 14) {
                    vibrator.vibrate(500);//手机震动
                    startShakeByPropertyAnim(image_yao, 0.9f, 1.1f, 10f, 1000);
                }
            }
        }

        private void sendTcp(String color) {
            String s1 = "FA" + " " + "AF" + " " + "01" + " " + color.substring(0, 2) + " " + color.substring(2, 4) + " " + color.substring(4, 6) + " " +
                    "00" + " " + "00" + " " + "00" + " " + "00" + " " + "AF" + " " + "FA";
            Log.i("INFO", s1);


            long NowTime = System.currentTimeMillis();
            long times = NowTime - LastTimes;
            if ((times) < INTERVAL_TIME) {
                return;
            } else {
                LastTimes = NowTime;
                try {
                    Client.getInstance().sendColorMsg(s1);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };

    protected void onResume() {
        //注册传感器监听器
        // 第一个参数是Listener，第二个参数是所得传感器类型，第三个参数值获取传感器信息的频率
        sensorManager.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_UI);
        super.onResume();
    }

    protected void onPause() {
        // 取消传感器监听器
        sensorManager.unregisterListener(sensorEventListener);
        super.onPause();
    }


    private void startShakeByPropertyAnim(View view, float scaleSmall, float scaleLarge, float shakeDegrees, long duration) {
        if (view == null) {
            return;
        }
        //TODO 验证参数的有效性


        //先变小后变大
        PropertyValuesHolder scaleXValuesHolder = PropertyValuesHolder.ofKeyframe(View.SCALE_X,
                Keyframe.ofFloat(0f, 1.0f),
                Keyframe.ofFloat(0.25f, scaleSmall),
                Keyframe.ofFloat(0.5f, scaleLarge),
                Keyframe.ofFloat(0.75f, scaleLarge),
                Keyframe.ofFloat(1.0f, 1.0f)
        );
        PropertyValuesHolder scaleYValuesHolder = PropertyValuesHolder.ofKeyframe(View.SCALE_Y,
                Keyframe.ofFloat(0f, 1.0f),
                Keyframe.ofFloat(0.25f, scaleSmall),
                Keyframe.ofFloat(0.5f, scaleLarge),
                Keyframe.ofFloat(0.75f, scaleLarge),
                Keyframe.ofFloat(1.0f, 1.0f)
        );

        //先往左再往右
        PropertyValuesHolder rotateValuesHolder = PropertyValuesHolder.ofKeyframe(View.ROTATION,
                Keyframe.ofFloat(0f, 0f),
                Keyframe.ofFloat(0.1f, -shakeDegrees),
                Keyframe.ofFloat(0.2f, shakeDegrees),
                Keyframe.ofFloat(0.3f, -shakeDegrees),
                Keyframe.ofFloat(0.4f, shakeDegrees),
                Keyframe.ofFloat(0.5f, -shakeDegrees),
                Keyframe.ofFloat(0.6f, shakeDegrees),
                Keyframe.ofFloat(0.7f, -shakeDegrees),
                Keyframe.ofFloat(0.8f, shakeDegrees),
                Keyframe.ofFloat(0.9f, -shakeDegrees),
                Keyframe.ofFloat(1.0f, 0f)
        );

        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(view, scaleXValuesHolder, scaleYValuesHolder, rotateValuesHolder);
        objectAnimator.setDuration(duration);
        objectAnimator.start();
    }
}
