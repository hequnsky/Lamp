package com.rmc.lamp.mvp.control.ui;

import android.annotation.TargetApi;
import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.rmc.lamp.mvp.main.ui.R;
import com.rmc.lamp.net.Client;

import java.io.IOException;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TongYanActivity extends Activity implements View.OnClickListener {

    @Bind(R.id.yellow)
    ImageView yellow;

    @Bind(R.id.blue)
    ImageView blue;

    @Bind(R.id.pink)
    ImageView pink;

    @Bind(R.id.red)
    ImageView red;

    @Bind(R.id.purple)
    ImageView purple;

    @Bind(R.id.orange)
    ImageView orange;

    @Bind(R.id.cyan)
    ImageView cyan;

    @Bind(R.id.green)
    ImageView green;

    @Bind(R.id.beijing)
    ImageView beijing;
    int buttonResult = 0;
    int NextStart = 0;
    int[][] colorbg = new int[8][2];
    int[] colormusic = new int[]{R.raw.red_chs, R.raw.orange_chs, R.raw.yellow_chs, R.raw.green_chs, R.raw.cyan_chs,
            R.raw.blue_chs, R.raw.purple_chs, R.raw.pink_chs};
    int[] result_chs = new int[]{R.raw.right_chs, R.raw.wrong_chs};

    String colors;
    private MediaPlayer mp = new MediaPlayer();


    @TargetApi(Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tong_yan);
        ButterKnife.bind(this);
        initOnClickListener();


    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initOnClickListener() {
        orange.setOnClickListener(this);
        yellow.setOnClickListener(this);
        pink.setOnClickListener(this);
        cyan.setOnClickListener(this);
        purple.setOnClickListener(this);
        blue.setOnClickListener(this);
        green.setOnClickListener(this);
        red.setOnClickListener(this);

        colorbg[0][0] = R.mipmap.red_apple;
        colorbg[0][1] = R.mipmap.red_apple;

        colorbg[1][0] = R.mipmap.orange_cloud;
        colorbg[1][1] = R.mipmap.orange;

        colorbg[2][0] = R.mipmap.yellow_banana;
        colorbg[2][1] = R.mipmap.yellow_duck;

        colorbg[3][0] = R.mipmap.green_bean;
        colorbg[3][1] = R.mipmap.green_grass;

        colorbg[4][0] = R.mipmap.cyan_clothes;
        colorbg[4][1] = R.mipmap.cyan_stone;

        colorbg[5][0] = R.mipmap.blue_sky_sea;
        colorbg[5][1] = R.mipmap.blue_stone;

        colorbg[6][0] = R.mipmap.purple_flower;
        colorbg[6][1] = R.mipmap.purple_windmill;

        colorbg[7][0] = R.mipmap.pink_doll;
        colorbg[7][1] = R.mipmap.pink_flower;
        int initStart = getLine();
        NextStart = initStart;
        beijing.setBackgroundResource(colorbg[initStart][getRow()]);
        try {
            StartMusic(colormusic[initStart]);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 下个事件
     *
     * @param view
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void Next(View view) throws IOException {
        NextGame();
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void NextGame() {
        NextStart = getLine();
        beijing.setBackgroundResource(colorbg[NextStart][getRow()]);
        try {
            StartMusic(colormusic[NextStart]);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.yellow:
                buttonResult = 2;
                try {
                    getResult(buttonResult);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.pink:
                buttonResult = 7;
                try {
                    getResult(buttonResult);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.red:
                buttonResult = 0;
                try {
                    getResult(buttonResult);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.purple:
                buttonResult = 6;
                try {
                    getResult(buttonResult);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.blue:
                buttonResult = 5;
                try {
                    getResult(buttonResult);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.green:
                buttonResult = 3;
                try {
                    getResult(buttonResult);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.cyan:
                buttonResult = 4;
                try {
                    getResult(buttonResult);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.orange:
                buttonResult = 1;
                try {
                    getResult(buttonResult);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    /**
     * 返回行数
     *
     * @return
     */
    public int getLine() {
        int line = new Random().nextInt(7);

        return line;
    }

    /**
     * 返回列数
     *
     * @return
     */
    public int getRow() {
        int row = new Random().nextInt(1);

        return row;
    }

    /**
     * 播放音乐
     *
     * @param music_raw
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void StartMusic(int music_raw) throws IOException {

        mp = MediaPlayer.create(TongYanActivity.this, music_raw);
        mp.start();
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void getResult(int resultCount) throws IOException {

        if (resultCount == NextStart) {
            StartMusic(result_chs[0]);
            setColor(NextStart);
        } else {
            StartMusic(result_chs[1]);
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                NextGame();
            }
        }, 1800);
    }

    /**
     * 给服务端设置颜色
     * @param color
     */
    public void setColor(int color) {

        switch (color) {
            case 0:
                colors = "FF0000";
                break;
            case 1:
                colors = "FF8000";
                break;
            case 2:
                colors = "FFFF00";
                break;
            case 3:
                colors = "00FF00";
                break;
            case 4:
                colors = "00FFFF";
                break;
            case 5:
                colors = "0000FF";
                break;
            case 6:
                colors = "8000FF";
                break;
            case 7:
                colors = "FF85B0";
                break;
        }
        String s1 = "FA" + " " + "AF" + " " + "01" + " " + colors.substring(0, 2) + " " + colors.substring(2, 4) + " " + colors.substring(4, 6) + " " +
                "00" + " " + "00" + " " + "00" + " " + "00" + " " + "AF" + " " + "FA";
        Log.i("INFO", s1);
        try {
            Client.getInstance().sendColorMsg(s1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}