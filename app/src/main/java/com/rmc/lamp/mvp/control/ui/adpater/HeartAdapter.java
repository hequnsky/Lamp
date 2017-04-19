package com.rmc.lamp.mvp.control.ui.adpater;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.rmc.lamp.bean.RoarModel;
import com.rmc.lamp.mvp.main.ui.R;

import java.util.List;

/**
 * 作者：hequnsky on 2016/7/28 12:06
 * <p>
 * 邮箱：heuqnsky@gmail.com
 */
public class HeartAdapter extends BaseItemDraggableAdapter<RoarModel> {


    public HeartAdapter(int LayoutId, List<RoarModel> data) {
        super(LayoutId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, RoarModel roarModel) {
        baseViewHolder.setImageResource(R.id.roar_img, roarModel.getImageId());

    }
}
