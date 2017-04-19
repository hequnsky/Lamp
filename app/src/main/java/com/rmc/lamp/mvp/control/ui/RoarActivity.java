package com.rmc.lamp.mvp.control.ui;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.rmc.lamp.bean.RoarModel;
import com.rmc.lamp.mvp.control.ui.adpater.HeartAdapter;
import com.rmc.lamp.mvp.main.ui.R;
import com.rmc.lamp.net.Client;
import com.rmc.lamp.utils.MediaRecorderUtil;
import com.rmc.lamp.utils.PermissionsChecker;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RoarActivity extends Activity implements View.OnClickListener {

    private static final int REQUEST_CODE = 0; // 请求码

    // 所需的全部权限
    static final String[] PERMISSIONS = new String[]{
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.MODIFY_AUDIO_SETTINGS
    };
    private PermissionsChecker mPermissionsChecker; // 权限检测器
    @Bind(R.id.roar_recycleview)
    RecyclerView roar_recycleview;
    @Bind(R.id.text_roar)
    TextView text_roar;

    @Bind(R.id.roar_db)
    TextView roar_db;

    MyCount mc;

    File extDir = Environment.getExternalStorageDirectory();
    private String filePath;
    double total;
    HeartAdapter mHeartAdapter;
    int first = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roar);
        ButterKnife.bind(this);

        init();
    }
    private void init() {
        mPermissionsChecker = new PermissionsChecker(this);
        text_roar.setOnClickListener(this);
        List<RoarModel> mRoarModel = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            RoarModel interactModel = new RoarModel();
            interactModel.setImageId(R.mipmap.bubbles_0_7);
            mRoarModel.add(interactModel);
        }
        roar_recycleview.setLayoutManager(new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL));
        mHeartAdapter = new HeartAdapter(R.layout.bubbles_item, mRoarModel);
        roar_recycleview.setAdapter(mHeartAdapter);
        Log.i("INFO", mHeartAdapter.getItemCount() + "");
    }


    @Override
    public void onClick(View view) {
        Start();
        text_roar.setEnabled(false);
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
            text_roar.setText("检测中" + "(" + l / 1000 + ")");
            double ratio = MediaRecorderUtil.recorder.getMaxAmplitude();
            if (ratio > 1)
                ratio = 20 * Math.log10(ratio);
            int ratios= (int) ratio;
            SendTcpMsg(ratios);

            Log.i("INFO", ratio + "");
            total = total + ratio;
            final double finalRatio = ratio;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    int s = (int) finalRatio;
                    roar_db.setText(s + "");
                    rmove(s);
                }
            });

        }

        @Override
        public void onFinish() {
            text_roar.setText("开始");
            text_roar.setEnabled(true);
            MediaRecorderUtil.stopRecordering();
            int svg = (int) (total / 9);
            roar_db.setText("0");
            plainDialog(svg);
            total=0;

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
        if (first > 1) {
            reset();
        }
        first++;
    }


    public void rmove(int total) {

        try {
            if (total < 80) {
                mHeartAdapter.remove(0);
            } else if (total < 90) {
                for (int i = 0; i < 2; i++) {
                    mHeartAdapter.remove(0);
                }
            } else {
                for (int i = 0; i < 3; i++) {
                    mHeartAdapter.remove(0);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void reset() {
        int itemCount = mHeartAdapter.getItemCount();
//        int totalitem = 0;
//        if (itemCount < 1) {
//            totalitem = 16;
//        } else {
//            totalitem = 16;
//        }
        List<RoarModel> zRoarModel = new ArrayList<>();
        for (int i = 0; i < 17 - itemCount; i++) {
            RoarModel interactModel = new RoarModel();
            interactModel.setImageId(R.mipmap.bubbles_0_7);
            zRoarModel.add(interactModel);
        }
        mHeartAdapter.addData(zRoarModel);

    }

    private void plainDialog(int sbg) {

        new AlertDialog.Builder(RoarActivity.this)
                .setTitle("分享挑战")
                .setMessage("伙计你真棒，你的咆哮达到" + sbg+ "" + "分贝,继续加油！")
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
     * @param sm
     */
    public void SendTcpMsg(int sm){
        String s1 = null;
        if(sm==0){
            s1 = "FA" + " " + "AF" + " " + "02" + " " + "00" + " " + "00" + " " + "00" + " " + "00"
                    + " " + "00" + " " + "00" + " " + "00" + " " + "AF" + " " + "FA" ;

        }else {
             s1 = "FA" + " " + "AF" + " " + "02" + " " + "00" + " " + "00" + " " + "00" + " " + Integer.toHexString(sm)
                    + " " + "00" + " " + "00" + " " + "00" + " " + "AF" + " " + "FA" ;
        }
        Log.i("INFO", s1);
        try {
            Client.getInstance().sendColorMsg(s1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override protected void onResume() {
        super.onResume();

        // 缺少权限时, 进入权限配置页面
        if (mPermissionsChecker.lacksPermissions(PERMISSIONS)){
            startPermissionsActivity();
        }
    }

    private void startPermissionsActivity() {
        PermissionsActivity.startActivityForResult(this, REQUEST_CODE,PERMISSIONS);
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 拒绝时, 关闭页面, 缺少主要权限, 无法运行
        if (requestCode == REQUEST_CODE && resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
            finish();
        }
    }
}
