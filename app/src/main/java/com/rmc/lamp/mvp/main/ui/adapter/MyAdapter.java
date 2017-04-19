package com.rmc.lamp.mvp.main.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.rmc.lamp.mvp.main.ui.fragment.ColorFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：hequnsky on 2016/7/25 11:30
 * <p>
 * 邮箱：heuqnsky@gmail.com
 */
public class MyAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments = new ArrayList<>();
    private String[] titles = {//
            "我是色彩",//
            "我是互动",//
            "我是情景", //
            "我是设置"};

    public MyAdapter(FragmentManager fm) {
        super(fm);
        fragments.add(ColorFragment.newInstance(titles[0]));
        fragments.add(ColorFragment.newInstance(titles[1]));
        fragments.add(ColorFragment.newInstance(titles[2]));
        fragments.add(ColorFragment.newInstance(titles[3]));


    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    
    }
}
