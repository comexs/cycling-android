package com.alex.cycling.service;

import android.content.Context;
import android.speech.tts.TextToSpeech;

import com.alex.cycling.client.WorkStatus;

import java.util.Locale;

/**
 * Created by comexs on 16/4/19.
 */
public class TTSUtils implements TextToSpeech.OnInitListener {

    private TextToSpeech tts;
    private boolean isStart = false;
    private InitSuccess initSuccess;


    public TTSUtils(Context context, InitSuccess initSuccess) {
        tts = new TextToSpeech(context, this);
        this.initSuccess = initSuccess;
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            isStart = true;
            if (null != initSuccess) initSuccess.init();
            int result = tts.setLanguage(Locale.ENGLISH);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {

            }
        }
    }


    public interface InitSuccess {
        void init();
    }

    public static String getTtsSpeak(WorkStatus status) {
        String result = null;
        switch (status) {
            case start:
                result = "开始跑步";
                break;
            case pause:
                result = "跑步暂停";
                break;
            case stop:
                result = "跑步停止";
                break;
            case end:
                result = "跑步结束";
                break;
            case recovery:
                result = "恢复跑步";
                break;
        }
        return result;
    }


    public void speek(WorkStatus speek) {
        if (isStart) {
            tts.speak(getTtsSpeak(speek), TextToSpeech.QUEUE_FLUSH, null);
        }
    }


    public void stop() {
        tts.stop(); // 不管是否正在朗读TTS都被打断
        tts.shutdown(); // 关闭，释放资源
    }

}
