package com.rmc.lamp.net;


import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class UDP {


    public static final String TAG = UDP.class.getSimpleName();

    private static UDP instance;

    private DatagramSocket mDatagramSocket;

    private int rPort = 4448;//接收端口

    private ExecutorService executorService = Executors.newFixedThreadPool(2);//线程池,一个用来发送,一个用来接收

    private Thread receiverThread;


    private UDP() {
    }

    public static UDP getInstance() {
        if (instance == null) {
            instance = new UDP();
        }
        return instance;
    }

    public void send(String s, String host, int port) {
        send(s.getBytes(), host, port);
    }

    public void send(String s, String host, int port, String charsetName) {
        send(s.getBytes(Charset.forName(charsetName)), host, port);
    }

    public void send(final byte[] data, final String host, final int sPort) {

        executorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    initDatagramSocket();
                    if (mDatagramSocket != null) {
                        Log.i(TAG, "InetAddress.getByName(host) "+InetAddress.getByName(host));
                        DatagramPacket packet = new DatagramPacket(data, data.length, InetAddress.getByName(host), sPort);
                        mDatagramSocket.send(packet);
                    }
                    Log.d(TAG, "run: "+String.format("发送%s,到%s,端口:%d", new String(data), host, sPort));
                } catch (IOException e) {
                    Log.d(TAG, "run: "+e.toString());
                }
            }
        });
    }

    private void initDatagramSocket() throws SocketException {
        if (mDatagramSocket == null) {
            mDatagramSocket = new DatagramSocket(rPort);
        }
    }

    public void close() {
        if (receiverThread != null) {
            receiverThread.interrupt();
            receiverThread = null;
        }
    }

    public void startReceived(final OnReceivedCallback callback) {
        if (callback == null) return;
        close();
        receiverThread = new Thread(new ReceiverRunnable(callback));
        receiverThread.start();
    }

    private class ReceiverRunnable implements Runnable {
        private OnReceivedCallback callback;

        public ReceiverRunnable(OnReceivedCallback callback) {
            this.callback = callback;
        }

        @Override
        public void run() {
            while (!Thread.interrupted()) {
                try {

                    initDatagramSocket();
                    byte[] data = new byte[1024];
                    DatagramPacket packet = new DatagramPacket(data, data.length);
                    mDatagramSocket.receive(packet);
                    String text = new String(packet.getData(), packet.getOffset(), packet.getLength());
                    String address = packet.getAddress().getHostName();
                    int port = packet.getPort();
                    callback.onReceived(address, port, text);
                } catch (IOException e) {
                    Log.d(TAG, "run: "+e.toString());
                }
            }
        }
    }

    public interface OnReceivedCallback {
        void onReceived(String address, int port, String text);
    }
}
