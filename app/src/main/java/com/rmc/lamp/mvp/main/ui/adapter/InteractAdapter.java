package com.rmc.lamp.mvp.main.ui.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.rmc.lamp.bean.InteractModel;
import com.rmc.lamp.mvp.main.ui.R;

import java.util.List;

/**
 * 作者：hequnsky on 2016/7/25 14:54
 * <p>
 * 邮箱：heuqnsky@gmail.com
 */
public class InteractAdapter  extends BaseItemDraggableAdapter<InteractModel>{
    Context mContext;

    public InteractAdapter(int layoutResId, List<InteractModel> data,Context context) {
        super(layoutResId, data);
        this.mContext=context;
    }
    @Override
    protected void convert(BaseViewHolder baseViewHolder, InteractModel interactModel) {
        baseViewHolder.setText(R.id.title,interactModel.getItemName());
        baseViewHolder.setImageResource(R.id.image,interactModel.getImageResId());
    }
}
