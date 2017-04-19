package com.rmc.lamp.mvp.main.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.jaeger.library.StatusBarUtil;
import com.lzy.widget.AlphaIndicator;
import com.rmc.lamp.mvp.main.ui.adapter.MyAdapter;
import com.rmc.lamp.service.TcpService;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.viewPager)
    ViewPager viewPager;
    @Bind(R.id.alphaIndicator)
    AlphaIndicator alphaIndicator;
    MyAdapter mMyAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        StatusBarUtil.setColor(MainActivity.this, getResources().getColor(R.color.blue));
        init();
    }

    /**
     * 初始化界面
     */
    private void init() {
        mMyAdapter = new MyAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mMyAdapter);
        alphaIndicator.setViewPager(viewPager);

    }


}