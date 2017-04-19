package com.rmc.lamp.mvp.control.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.rmc.lamp.mvp.main.ui.R;
import com.rmc.lamp.net.Client;
import com.rmc.lamp.rx.RxBus;
import com.rmc.lamp.rx.RxBusResult;
import com.rmc.lamp.utils.PresentationLayout;

import java.util.ArrayList;
import java.util.List;

public class ToolsActivity extends AppCompatActivity {
    private static final String[] TAGS = new String[]{"红色", "橘色", "黄色", "紫色", "绿色", "蓝色", "紫色", "粉色"};//
    private PresentationLayout mPresentationLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tools);
        mPresentationLayout = (PresentationLayout) findViewById(R.id.presentation);
        List<PresentationLayout.Tag> mSource = new ArrayList<>();
        for (String t : TAGS) {
            PresentationLayout.Tag tag = new PresentationLayout.Tag(t, 0);
            mSource.add(tag);
        }
        mPresentationLayout.inputTags(mSource);

        RxBus.getInstance().toObserverableOnMainThread("Color", new RxBusResult() {
            @Override
            public void onRxBusResult(Object o) {

                String s = (o.toString().substring(3));
                String s1 = "FA" + " " + "AF" + " " + "01" + " " + s.substring(0, 2) + " " + s.substring(2, 4) + " " + s.substring(4, 6) + " " +
                        "00" + " " + "00" + " " + "00" + " " + "00" + " " + "AF" + " " + "FA";
                Log.i("INFO", s1);


                try {
                    Client.getInstance().sendColorMsg(s1);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

}






