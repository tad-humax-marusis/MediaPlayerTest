package com.marusys.evo.component;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.TextView;
import android.os.Handler;

import java.util.HashMap;

import common.AppInfo;

public class TestAudioManagerController {
    private final String TAG = common.AppInfo.OWNER + "[TestAudioManagerController]";
    private HashMap<String, Integer> mStreamTypeMap;
    private HashMap<String, Integer> mAdjustTypeMap;

    private AudioManager mAudioManager;

    private int mStreamType;
    private int mAdjustEnum;
    private int mFlagEnum;

    private TextView mMuteState;

    private static TestAudioManagerController mInstance = null;

    public static TestAudioManagerController getInstance() {
        if (mInstance == null) {
            mInstance = new TestAudioManagerController();
        }
        return mInstance;
    }

    private TestAudioManagerController() {
        mAudioManager = (AudioManager)AppInfo.getInstance().getSystemService(Context.AUDIO_SERVICE);

        mStreamTypeMap = new HashMap<String, Integer>();
        mStreamTypeMap.put("STREAM_MUSIC",         AudioManager.STREAM_MUSIC);
        mStreamTypeMap.put("STREAM_VOICE_CALL",    AudioManager.STREAM_VOICE_CALL);
        mStreamTypeMap.put("STREAM_SYSTEM",        AudioManager.STREAM_SYSTEM);
        mStreamTypeMap.put("STREAM_RING",          AudioManager.STREAM_RING);
        mStreamTypeMap.put("STREAM_ALARM",         AudioManager.STREAM_ALARM);
        mStreamTypeMap.put("STREAM_NOTIFICATION",  AudioManager.STREAM_NOTIFICATION);
        mStreamTypeMap.put("STREAM_DTMF",          AudioManager.STREAM_DTMF);
        mStreamTypeMap.put("STREAM_ACCESSIBILITY", AudioManager.STREAM_ACCESSIBILITY);

        mAdjustTypeMap = new HashMap<String, Integer>();
        mAdjustTypeMap.put("ADJUST_MUTE"        ,AudioManager.ADJUST_MUTE);
        mAdjustTypeMap.put("ADJUST_RAISE"       ,AudioManager.ADJUST_RAISE);
        mAdjustTypeMap.put("ADJUST_LOWER"       ,AudioManager.ADJUST_LOWER);
        mAdjustTypeMap.put("ADJUST_SAME"        ,AudioManager.ADJUST_SAME);
        mAdjustTypeMap.put("ADJUST_UNMUTE"      ,AudioManager.ADJUST_UNMUTE);
        mAdjustTypeMap.put("ADJUST_TOGGLE_MUTE" ,AudioManager.ADJUST_TOGGLE_MUTE);

        // This is mix value by OR(|) but i it waste a time so this feature need to_do
        mFlagEnum = AudioManager.FLAG_SHOW_UI;
    }

    public String[] getStreamTypeList() {
        return mStreamTypeMap.keySet().toArray(new String[0]);
    }

    public String[] getAdjustTypeList() {
        return mAdjustTypeMap.keySet().toArray(new String[0]);
    }

    public void setStreamType(String key) {
        Integer i = mStreamTypeMap.get(key);
        if(null != i) {
            mStreamType = i.intValue();
        }
    };

    public int getSelStreamType() {
        return mStreamType;
    };

    public void setAdjustValue(String key) {
        Integer i = mAdjustTypeMap.get(key);
        if(null != i) {
            mAdjustEnum = i.intValue();
        }
    };

    public void doSetStreamVolume() {
        try {
            mAudioManager.setStreamVolume(mStreamType, mAdjustEnum, AudioManager.FLAG_SHOW_UI);
        } catch (SecurityException e) {
            Log.d(TAG, "doSetStreamVolume: " + e.toString());
        }
    };

    public void audioSetParameters(String param_str) {
        Log.d(TAG, "setParameters: " + param_str);
        mAudioManager.setParameters(param_str);
    };

    public int mMicSetDump = 0;

    public static final int DUMP_AUDIO_PCM_BT_IN                     = 0b00000000000001;
    public static final int DUMP_AUDIO_PCM_MIC_IN                    = 0b00000000000010;
    public static final int DUMP_AUDIO_PCM_MIC_OUT                   = 0b00000000000100;
    public static final int DUMP_AUDIO_PCM_RADIO_IN                  = 0b00000000001000;
    public static final int DUMP_AUDIO_AWE_ALL_OUT                   = 0b00000000010000;
    public static final int DUMP_AUDIO_AWE_MIC_OUT                   = 0b00000000100000;
    public static final int DUMP_AUDIO_AWE_IN_STREAM_MEDIA           = 0b00000001000000;
    public static final int DUMP_AUDIO_AWE_IN_STREAM_STREAMING       = 0b00000010000000;
    public static final int DUMP_AUDIO_AWE_IN_STREAM_NAVIGATION      = 0b00000100000000;
    public static final int DUMP_AUDIO_AWE_IN_STREAM_VOICE_ASSISTANT = 0b00001000000000;
    public static final int DUMP_AUDIO_AWE_IN_INCOMMING_CALL         = 0b00010000000000;
    public static final int DUMP_AUDIO_AWE_IN_ADAS_SOUND             = 0b00100000000000;
    public static final int DUMP_AUDIO_AWE_IN_OUTGOING_CALL          = 0b01000000000000;

    public void setMicDump(int mic_dump_value, boolean value) {
        if(value) {
            mMicSetDump = mMicSetDump|mic_dump_value;
        } else {
            mMicSetDump = mMicSetDump&(~mic_dump_value);
        }
        String array = "dump_audio=" + Integer.toString(mMicSetDump);
        Log.d(TAG, array);
        mAudioManager.setParameters(array);
    };

    int requestFocusAudio(int audioFocus, AudioAttributes attr, AudioManager.OnAudioFocusChangeListener focusChangeCallback, Handler handler) {
        Log.d(TAG, "AudioAttributes :" + attr.toString());

        switch (audioFocus) {
            case AudioManager.AUDIOFOCUS_NONE:
            case AudioManager.AUDIOFOCUS_GAIN:
            case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT:
            case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK:
            case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_EXCLUSIVE:
            case AudioManager.AUDIOFOCUS_LOSS:
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                break;
            default:
                audioFocus = AudioManager.AUDIOFOCUS_GAIN;
        }

        AudioFocusRequest focusRequest = new AudioFocusRequest.Builder(audioFocus)
                .setAudioAttributes(attr)
                .setAcceptsDelayedFocusGain(true)
                .setOnAudioFocusChangeListener(focusChangeCallback, handler)
                .build();

        int res = mAudioManager.requestAudioFocus(focusRequest);
        switch (res) {
            case AudioManager.AUDIOFOCUS_REQUEST_FAILED:  Log.d(TAG, "requestAudioFocus res : AUDIOFOCUS_REQUEST_FAILED"); break;
            case AudioManager.AUDIOFOCUS_REQUEST_GRANTED: Log.d(TAG, "requestAudioFocus res : AUDIOFOCUS_REQUEST_GRANTED"); break;
            case AudioManager.AUDIOFOCUS_REQUEST_DELAYED: Log.d(TAG, "requestAudioFocus res : AUDIOFOCUS_REQUEST_DELAYED"); break;
            default:
                Log.d(TAG, "res : " + res);
                break;
        }
        return res;
    };
}