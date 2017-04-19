package com.rmc.lamp.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

/**
 * 作者：hequnsky on 2016/7/25 16:26
 * <p>
 * 邮箱：heuqnsky@gmail.com
 */
public class ImageUitls {

    public  static  void loadLocal(Context context, String url, ImageView view){
        Glide.with(context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(view);

    }



}
