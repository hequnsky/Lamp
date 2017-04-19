package com.rmc.lamp.net;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.vilyever.socketclient.SocketClient;
import com.vilyever.socketclient.helper.SocketClientAddress;
import com.vilyever.socketclient.helper.SocketClientDelegate;
import com.vilyever.socketclient.helper.SocketResponsePacket;



public class Client {
    private static final String TAG = "Client";
    private static Client instance;
    SocketClient localSocketClient = null;

    public static Client getInstance() {
        if (instance == null) {
            instance = new Client();
        }
        return instance;
    }

    public void init(String ip, int port) {
        localSocketClient = new SocketClient(new SocketClientAddress(ip, port, 15 * 1000));
//        localSocketClient.getHeartBeatHelper().setHeartBeatInterval(1000 * 30);
//        localSocketClient.getHeartBeatHelper().setRemoteNoReplyAliveTimeout(1000 * 60);

    }

    /**
     * 连接网络
     */
    public void Connection() {
        try {
            localSocketClient.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 断开网络
     */
    public void disConnection() {
        try {
            localSocketClient.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 发送消息
     *
     * @param msg
     */
    public void SendMsg(final String msg) {
        localSocketClient.registerSocketClientDelegate(new SocketClientDelegate() {
            @Override
            public void onConnected(SocketClient client) {
                Log.i(TAG, "网络已连接！ " + client.getAddress().getRemoteIP() + "已连接");

                client.sendString(msg);
                Intent intent = new Intent();


            }

            @Override
            public void onDisconnected(SocketClient client) {

                Log.i(TAG, "网络已断开！ " + client.getAddress().getRemoteIP() + "已断开");
            }

            @Override
            public void onResponse(SocketClient client, @NonNull SocketResponsePacket responsePacket) {
                Log.i(TAG, "onResponse: " + client.getAddress().getRemoteIP() + responsePacket.getMessage());
            }
        });
    }

    public void sendColorMsg(String msg) {
        SocketClient.State state = localSocketClient.getState();
        localSocketClient.sendString(msg);
    }

    public SocketClient.State getClientState() {
        try {
            SocketClient.State state = localSocketClient.getState();
            return state;
        } catch (Exception e) {
            return SocketClient.State.Disconnected;
        }


    }
}
