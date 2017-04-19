package com.rmc.lamp.mvp.control.ui;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.rmc.lamp.mvp.main.ui.R;
import com.rmc.lamp.net.Client;
import com.rmc.lamp.utils.MediaRecorderUtil;
import com.rmc.lamp.utils.PermissionsChecker;
import com.shinelw.library.ColorArcProgressBar;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LoveActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 0; // 请求码

    // 所需的全部权限
    static final String[] PERMISSIONS = new String[]{
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.MODIFY_AUDIO_SETTINGS
    };
    private PermissionsChecker mPermissionsChecker; // 权限检测器
    @Bind(R.id.bar)
    ColorArcProgressBar bar;
    @Bind(R.id.love_start)
    Button love_start;

    MyCount mc;

    File extDir = Environment.getExternalStorageDirectory();
    private String filePath;
    double total;
    int first = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_love);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        mPermissionsChecker = new PermissionsChecker(this);
        bar.setCurrentValues(0);
        love_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                love_start.setEnabled(false);
                Start();

            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();

        // 缺少权限时, 进入权限配置页面
        if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
            startPermissionsActivity();
        }
    }

    private void startPermissionsActivity() {
        PermissionsActivity.startActivityForResult(this, REQUEST_CODE, PERMISSIONS);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 拒绝时, 关闭页面, 缺少主要权限, 无法运行
        if (requestCode == REQUEST_CODE && resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
            finish();
        }
    }


    /**
     * 时间计时器
     */
    class MyCount extends CountDownTimer {
        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) {
            love_start.setText("倒计时" + "(" + l / 1000 + ")");
            double ratio = MediaRecorderUtil.recorder.getMaxAmplitude();
            if (ratio > 1)
                ratio = 20 * Math.log10(ratio);
            int ratios = (int) ratio;
            SendTcpMsg(ratios);

            Log.i("INFO", ratio + "");
            total = total + ratio;
            final double finalRatio = ratio;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    int s = (int) finalRatio;
                    bar.setCurrentValues(s);

                }
            });

        }

        @Override
        public void onFinish() {
            love_start.setEnabled(true);
            MediaRecorderUtil.stopRecordering();
            int svg = (int) (total / 10);
            plainDialog(svg);
            total=0;
            love_start.setText("开始");
        }
    }

    /**
     * 开始
     */
    public void Start() {

        filePath = new File(extDir, "mediarecorder" + "_" + System.currentTimeMillis() + "").getAbsolutePath() + ".amr";
        mc = new MyCount(20000, 1000);
        mc.start();
        MediaRecorderUtil.startRecordering(filePath);

    }

    private void plainDialog(int sbg) {

        new AlertDialog.Builder(LoveActivity.this)
                .setTitle("分享挑战")
                .setMessage("伙计你真棒，你的咆哮达到" + sbg + "" + "分贝,继续加油！")
                .setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {

                            }
                        })
                .setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                            }
                        }).setCancelable(false).show();

    }

    /**
     * 发送TCP消息
     *
     * @param sm
     */
    public void SendTcpMsg(int sm) {
        String s1 = null;
        if (sm == 0) {
            s1 = "FA" + " " + "AF" + " " + "02" + " " + "00" + " " + "00" + " " + "00" + " " + "00"
                    + " " + "00" + " " + "00" + " " + "00" + " " + "AF" + " " + "FA";

        } else {
            s1 = "FA" + " " + "AF" + " " + "02" + " " + "00" + " " + "00" + " " + "00" + " " + Integer.toHexString(sm)
                    + " " + "00" + " " + "00" + " " + "00" + " " + "AF" + " " + "FA";
        }
        Log.i("INFO", s1);
        try {
            Client.getInstance().sendColorMsg(s1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
