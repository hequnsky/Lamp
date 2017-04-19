package com.rmc.lamp.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.rmc.lamp.net.Client;

public class TcpService extends Service {
    public TcpService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("tag", "服务-------create");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("tag", "服务-------destory");
        Client.getInstance().disConnection();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.i("tag", "服务-------start");
        try {
            String ip = intent.getStringExtra("IP");
            String port = intent.getStringExtra("PORT");
            Client.getInstance().init(ip, Integer.parseInt(port));
            Client.getInstance().Connection();
            Client.getInstance().SendMsg("0x01");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
