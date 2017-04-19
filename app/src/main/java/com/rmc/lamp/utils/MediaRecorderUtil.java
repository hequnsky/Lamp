package com.rmc.lamp.utils;

import android.media.MediaRecorder;
import android.util.Log;

/**
 * 作者：hequnsky on 2016/7/29 09:26
 * <p>
 * 邮箱：heuqnsky@gmail.com
 */
public class MediaRecorderUtil {
    public static MediaRecorder recorder;

    public static void startRecordering(String filePath) {
        try {
            if (recorder != null) {
                recorder.release();
                recorder = null;
            }
            recorder = new MediaRecorder();
            // 设置麦克风为音频源
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            // 设置音频文件的编码
            recorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
            // 设置输出文件的格式
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);//可以设置成 MediaRecorder.AudioEncoder.AMR_NB

            recorder.setOutputFile(filePath);

            recorder.prepare();
            recorder.start();
        } catch (Exception e) {
        }
    }

    public static void stopRecordering() {
        if (recorder != null) {

            try {
                //下面三个参数必须加，不加的话会奔溃，在mediarecorder.stop();
                //报错为：RuntimeException:stop failed
                recorder.setOnErrorListener(null);
                recorder.setOnInfoListener(null);
                recorder.setPreviewDisplay(null);
                recorder.stop();
            } catch (IllegalStateException e) {
                // TODO: handle exception
                Log.i("Exception", Log.getStackTraceString(e));
            } catch (RuntimeException e) {
                // TODO: handle exception
                Log.i("Exception", Log.getStackTraceString(e));
            } catch (Exception e) {
                // TODO: handle exception
                Log.i("Exception", Log.getStackTraceString(e));
            }
            recorder.reset();
            recorder.release();
            recorder = null;
        }
    }
}
