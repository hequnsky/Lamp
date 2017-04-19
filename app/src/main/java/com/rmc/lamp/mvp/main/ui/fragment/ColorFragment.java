package com.rmc.lamp.mvp.main.ui.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.rmc.lamp.bean.InteractModel;
import com.rmc.lamp.mvp.control.ui.HeartActivity;
import com.rmc.lamp.mvp.control.ui.LoveActivity;
import com.rmc.lamp.mvp.control.ui.Main2Activity;
import com.rmc.lamp.mvp.control.ui.TongYanActivity;
import com.rmc.lamp.mvp.control.ui.ToolsActivity;
import com.rmc.lamp.mvp.control.ui.TranslationActivity;
import com.rmc.lamp.mvp.main.ui.R;
import com.rmc.lamp.mvp.main.ui.adapter.InteractAdapter;
import com.rmc.lamp.mvp.main.ui.adapter.ThemeAdapter;
import com.rmc.lamp.net.Client;
import com.rmc.lamp.rx.RxBus;
import com.rmc.lamp.rx.RxBusResult;
import com.rmc.lamp.service.TcpService;
import com.rmc.lamp.widget.BalloonsView;
import com.vilyever.socketclient.SocketClient;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;


/**
 * 作者：hequnsky on 2016/7/25 11:27
 * <p>
 * 邮箱：heuqnsky@gmail.com
 */
public class ColorFragment extends Fragment {

    Context mContext;
    public static final String BUNDLE_TITLE = "title";
    private String mTitle = "DefaultValue";
    int LayoutId = 0;
    // 两次检测的时间间隔
    private static final int UPTATE_INTERVAL_TIME = 1000;
    // 上次检测时间
    private long LastTime;
    int i = 1;
    Intent intent;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Bundle arguments = getArguments();
        mContext = getActivity();
        if (arguments != null) {
            mTitle = arguments.getString(BUNDLE_TITLE);
        }
        switchLayout(mTitle);
        View view = inflater.inflate(LayoutId, null);
        switchId(view, mTitle);

        return view;
    }

    public static ColorFragment newInstance(String title) {
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_TITLE, title);
        ColorFragment fragment = new ColorFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    /**
     * @param Title
     */
    public void switchLayout(String Title) {

        switch (Title) {
            case "我是色彩":
                LayoutId = R.layout.color_fragment;
                break;
            case "我是互动":
                LayoutId = R.layout.interact_fragment;
                break;
            case "我是情景":
                LayoutId = R.layout.scenario_fragment;
                break;
            case "我是设置":
                LayoutId = R.layout.setting_fragment;
                break;
            default:
                break;
        }

    }

    /**
     * @param view
     * @param Title
     */
    public void switchId(View view, String Title) {

        switch (Title) {
            case "我是色彩":
                Color(view);
                break;
            case "我是互动":
                interact(view);
                break;
            case "我是情景":
                scenario(view);
                break;
            case "我是设置":
                Setting(view);
                break;
            default:
                break;
        }

    }

    /**
     * @param view
     */
    public void interact(View view) {
        String[] names = new String[]{"撸蛋", "咆哮", "童言声色", "心动", "摇一摇", "八音符"};
        int[] icon = new int[]{R.mipmap.icon_interaction_bounce, R.mipmap.icon_interaction_roar, R.mipmap.icon_interaction_baby_love_color,
                R.mipmap.icon_interaction_heart_beat, R.mipmap.icon_interaction_shake, R.mipmap.icon_interaction_baby_love_color};

        List<InteractModel> model = null;
        model = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            InteractModel m = new InteractModel();
            m.setImageResId(icon[i]);
            m.setItemName(names[i]);
            model.add(m);
        }
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycle_view);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        InteractAdapter mInteractAdapter = new InteractAdapter(R.layout.interact_item, model, mContext);
        recyclerView.setAdapter(mInteractAdapter);
        mInteractAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                if (i == 5) {
                    startActivity(new Intent(getActivity(), Main2Activity.class));
                } else if (i == 4) {
                    startActivity(new Intent(getActivity(), TranslationActivity.class));
                } else if (i == 3) {
                    startActivity(new Intent(getActivity(), HeartActivity.class));
                } else if (i == 2) {
                    startActivity(new Intent(getActivity(), TongYanActivity.class));
                } else if (i == 1) {
                    startActivity(new Intent(getActivity(), LoveActivity.class));
//                    startActivity(new Intent(getActivity(), Main2Activity.class));
                } else {
                    startActivity(new Intent(getActivity(), ToolsActivity.class));

                }
            }
        });


    }

    /**
     * @param view
     */
    public void scenario(View view) {

        String[] names = new String[]{"平日", "双休日", "商业活动", "一般节日", "重大节假日"};
        int[] icon = new int[]{R.mipmap.pic_usual, R.mipmap.pic_weekend, R.mipmap.pic_business,
                R.mipmap.pic_festival, R.mipmap.pic_holiday};

        List<InteractModel> model = null;
        model = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            InteractModel m = new InteractModel();
            m.setImageResId(icon[i]);
            m.setItemName(names[i]);
            model.add(m);
        }
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycle_view_sce);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        ThemeAdapter mInteractAdapter = new ThemeAdapter(R.layout.item_draw, model, mContext);
        recyclerView.setAdapter(mInteractAdapter);



        mInteractAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                Log.i("INFO", i + "");

            }
        });

    }

    /**
     * @param view
     */
    public void Color(View view) {
        BalloonsView balloon = (BalloonsView) view.findViewById(R.id.balloon);
        RxBus.getInstance().toObserverableOnMainThread("setColor", new RxBusResult() {
            @Override
            public void onRxBusResult(Object o) {

                String s = (o.toString().substring(3));
                String s1 = "FA" + " " + "AF" + " " + "01" + " " + s.substring(0, 2) + " " + s.substring(2, 4) + " " + s.substring(4, 6) + " " +
                        "00" + " " + "00" + " " + "00" + " " + "00" + " " + "AF" + " " + "FA";
                Log.i("INFO", s1);


                long NowTime = System.currentTimeMillis();
                long times = NowTime - LastTime;
                if ((times) < UPTATE_INTERVAL_TIME) {
                    return;
                } else {
                    LastTime = NowTime;
                    try {
                        Client.getInstance().sendColorMsg(s1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }


            }
        });
    }


    public void Setting(View view) {
        final EditText mUserName = (EditText) view.findViewById(R.id.et_username);
        final EditText mPassWord = (EditText) view.findViewById(R.id.et_pwd);
        final TextInputLayout mTextInputLayout = (TextInputLayout) view.findViewById(R.id.til_username);
        Button ok = (Button) view.findViewById(R.id.ok);
        Button dis = (Button) view.findViewById(R.id.dis);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 方式一：通过TextInputLayout获取到里面的子控件EditText后在获取编辑的内容
                String ip = mUserName.getText().toString();
                // 方式二：直接通过EditText获取到里面的编辑内容
                String port = mPassWord.getText().toString();
                if (ip.length() < 1) {
                    mTextInputLayout.setError("请填写IP地址");
                } else {
                    intent = new Intent(mContext, TcpService.class);
                    intent.putExtra("IP",ip);
                    intent.putExtra("PORT",port);
                    mContext.startService(intent);
//                    try {
//                        Client.getInstance().init(ip, Integer.parseInt(port));
//                        Client.getInstance().Connection();
//                        Client.getInstance().SendMsg("0x01");
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
                }
            }
        });
        dis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Client.getInstance().disConnection();
                if (intent != null) {
                    mContext.stopService(intent);
                }

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RxBus.getInstance().release();
        EventBus.getDefault().unregister(getActivity());
    }


    @Override
    public void onResume() {
        super.onResume();
        if (i == 1) {
            try {
                String tcpstate;
                SocketClient.State clientState = Client.getInstance().getClientState();
                if (clientState.equals(SocketClient.State.Disconnected)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("请连接设备！");
                    builder.setIcon(android.R.drawable.ic_dialog_info);
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                        }
                    });
                    builder.setNegativeButton("取消", null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    i++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
