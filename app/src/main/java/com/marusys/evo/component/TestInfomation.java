package com.marusys.evo.component;

import android.media.AudioAttributes;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;

import com.marusys.evo.audiotest.R;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

/***{@docRoot}
 * TestInfomation save the information for test
 */
public class TestInfomation {
    private final HashMap<String, Integer> mMediaTrackMap = new HashMap<String, Integer>();
    private static TestInfomation mInstance = null;
    private final static String TAG = common.AppInfo.OWNER + "[TestInfomation]";
    private static AudioAttributes.Builder mAudioAttributesBuilder; // User selection value
    private static int mAudioFocusValue = AudioManager.AUDIOFOCUS_GAIN;

    private final String[] mMediaAttUsageList = {
            "USAGE_UNKNOWN",
            "USAGE_MEDIA",
            "USAGE_VOICE_COMMUNICATION",
            "USAGE_VOICE_COMMUNICATION_SIGNALLING",
            "USAGE_ALARM",
            "USAGE_NOTIFICATION",
            "USAGE_NOTIFICATION_RINGTONE",
            "USAGE_NOTIFICATION_COMMUNICATION_REQUEST",
            "USAGE_NOTIFICATION_COMMUNICATION_INSTANT",
            "USAGE_NOTIFICATION_COMMUNICATION_DELAYED",
            "USAGE_NOTIFICATION_EVENT",
            "USAGE_ASSISTANCE_ACCESSIBILITY",
            "USAGE_ASSISTANCE_NAVIGATION_GUIDANCE",
            "USAGE_ASSISTANCE_SONIFICATION",
            "USAGE_GAME",
            "USAGE_ASSISTANT"
    };

    private final String[] mMediaAttContentList = {
            "CONTENT_TYPE_UNKNOWN",
            "CONTENT_TYPE_SPEECH",
            "CONTENT_TYPE_MUSIC",
            "CONTENT_TYPE_MOVIE",
            "CONTENT_TYPE_SONIFICATION"
    };

    /* Ent.Mode Enum list
    */
    private final String[] mEntModeList = {
        "USB",
        "BT",
        "FM",
        "YANDEX"
    };

    private final String[] mAudioFocus = {
            "AUDIOFOCUS_NONE",
            "AUDIOFOCUS_GAIN",
            "AUDIOFOCUS_GAIN_TRANSIENT",
            "AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK",
            "AUDIOFOCUS_GAIN_TRANSIENT_EXCLUSIVE",
            "AUDIOFOCUS_LOSS",
            "AUDIOFOCUS_LOSS_TRANSIENT",
            "AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK"
    };

    private TestInfomation() {
        mMediaTrackMap.put("sinway 1kHz 1 channel (long)", R.raw.sinway_1k_1ch_long);
        mMediaTrackMap.put("sinway 1kHz 2 channel (long)", R.raw.sinway_1k_2ch_long);
        mMediaTrackMap.put("sinway 1kHz 2 channel (short)", R.raw.sinway_1k_2ch_short);
        // mMediaTrackMap.put("Dynasty Miia (FLAG)", R.raw.dynasty_miia);
    };

    public static TestInfomation getInstance() {
        if(null == mInstance) {
            mInstance = new TestInfomation();
            mAudioAttributesBuilder = new AudioAttributes.Builder();
        }
        return mInstance;
    }

    public String[] getTrackList() {
        return mMediaTrackMap.keySet().toArray(new String[0]);
    }
    public String[] getMediaAttUsageList() {
        return mMediaAttUsageList;
    }
    public String[] getMediaAttContentList() {
        return mMediaAttContentList;
    }
    public String[] getEntModeList() {
        return mEntModeList;
    }
    public String[] getAudioFocusList() {
        return mAudioFocus;
    }

    public String[] getUPAObstacleZoneList() {
        return new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8" };
    }
    public String[] getRVCGearList() {
        return new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8" };
    }
    public int getMediaFileRawId(String media_key) {
        Integer value = mMediaTrackMap.get(media_key);
        if (null == value) {
            return -1;
        } else {
            return value.intValue();
        }
    }

    public void selAttUsageValue(String media_key) {
        switch (media_key) {
            case "USAGE_UNKNOWN":
                mAudioAttributesBuilder.setUsage(AudioAttributes.USAGE_UNKNOWN);
                break;
            case "USAGE_MEDIA":
                mAudioAttributesBuilder.setUsage(AudioAttributes.USAGE_MEDIA);
                break;
            case "USAGE_VOICE_COMMUNICATION":
                mAudioAttributesBuilder.setUsage(AudioAttributes.USAGE_VOICE_COMMUNICATION);
                break;
            case "USAGE_VOICE_COMMUNICATION_SIGNALLING":
                mAudioAttributesBuilder.setUsage(AudioAttributes.USAGE_VOICE_COMMUNICATION_SIGNALLING);
                break;
            case "USAGE_ALARM":
                mAudioAttributesBuilder.setUsage(AudioAttributes.USAGE_ALARM);
                break;
            case "USAGE_NOTIFICATION":
                mAudioAttributesBuilder.setUsage(AudioAttributes.USAGE_NOTIFICATION);
                break;
            case "USAGE_NOTIFICATION_RINGTONE":
                mAudioAttributesBuilder.setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE);
                break;
            case "USAGE_NOTIFICATION_COMMUNICATION_REQUEST":
                mAudioAttributesBuilder.setUsage(AudioAttributes.USAGE_NOTIFICATION_COMMUNICATION_REQUEST);
                break;
            case "USAGE_NOTIFICATION_COMMUNICATION_INSTANT":
                mAudioAttributesBuilder.setUsage(AudioAttributes.USAGE_NOTIFICATION_COMMUNICATION_INSTANT);
                break;
            case "USAGE_NOTIFICATION_COMMUNICATION_DELAYED":
                mAudioAttributesBuilder.setUsage(AudioAttributes.USAGE_NOTIFICATION_COMMUNICATION_DELAYED);
                break;
            case "USAGE_NOTIFICATION_EVENT":
                mAudioAttributesBuilder.setUsage(AudioAttributes.USAGE_NOTIFICATION_EVENT);
                break;
            case "USAGE_ASSISTANCE_ACCESSIBILITY":
                mAudioAttributesBuilder.setUsage(AudioAttributes.USAGE_ASSISTANCE_ACCESSIBILITY);
                break;
            case "USAGE_ASSISTANCE_NAVIGATION_GUIDANCE":
                mAudioAttributesBuilder.setUsage(AudioAttributes.USAGE_ASSISTANCE_NAVIGATION_GUIDANCE);
                break;
            case "USAGE_ASSISTANCE_SONIFICATION":
                mAudioAttributesBuilder.setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION);
                break;
            case "USAGE_GAME":
                mAudioAttributesBuilder.setUsage(AudioAttributes.USAGE_GAME);
                break;
            case "USAGE_ASSISTANT":
                mAudioAttributesBuilder.setUsage(AudioAttributes.USAGE_ASSISTANT);
                break;
            default:
                mAudioAttributesBuilder.setUsage(AudioAttributes.USAGE_MEDIA);
                break;
        }
    }

    public void selAttContentValue(String media_key) {
        switch (media_key) {
            case "CONTENT_TYPE_UNKNOWN":
                mAudioAttributesBuilder.setContentType(AudioAttributes.CONTENT_TYPE_UNKNOWN);
                break;
            case "CONTENT_TYPE_SPEECH":
                mAudioAttributesBuilder.setContentType(AudioAttributes.CONTENT_TYPE_SPEECH);
                break;
            case "CONTENT_TYPE_MUSIC":
                mAudioAttributesBuilder.setContentType(AudioAttributes.CONTENT_TYPE_MUSIC);
                break;
            case "CONTENT_TYPE_MOVIE":
                mAudioAttributesBuilder.setContentType(AudioAttributes.CONTENT_TYPE_MOVIE);
                break;
            case "CONTENT_TYPE_SONIFICATION":
                mAudioAttributesBuilder.setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION);
                break;
            default:
                mAudioAttributesBuilder.setContentType(AudioAttributes.CONTENT_TYPE_MUSIC);
                break;
        }
    }

    public void setAudioFocus(String audio_focus_name) {
        switch (audio_focus_name) {
            case "AUDIOFOCUS_NONE":
                mAudioFocusValue = AudioManager.AUDIOFOCUS_NONE;
                break;
            case "AUDIOFOCUS_GAIN":
                mAudioFocusValue = AudioManager.AUDIOFOCUS_GAIN;
                break;
            case "AUDIOFOCUS_GAIN_TRANSIENT":
                mAudioFocusValue = AudioManager.AUDIOFOCUS_GAIN_TRANSIENT;
                break;
            case "AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK":
                mAudioFocusValue = AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK;
                break;
            case "AUDIOFOCUS_GAIN_TRANSIENT_EXCLUSIVE":
                mAudioFocusValue = AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_EXCLUSIVE;
                break;
            case "AUDIOFOCUS_LOSS":
                mAudioFocusValue = AudioManager.AUDIOFOCUS_LOSS;
                break;
            case "AUDIOFOCUS_LOSS_TRANSIENT":
                mAudioFocusValue = AudioManager.AUDIOFOCUS_LOSS_TRANSIENT;
                break;
            case "AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK":
                mAudioFocusValue = AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK;
                break;
            default:
                mAudioFocusValue = AudioManager.AUDIOFOCUS_GAIN;
                break;
        }
    }

    public AudioAttributes getSelectAudioAttributes() {
        Log.d(TAG, "Create new Bundle!");
        Bundle bundle = new Bundle();
        bundle.putInt("android.car.media.AUDIOFOCUS_EXTRA_REQUEST_ZONE_ID", 1);
        Log.d(TAG, "AudioAttributesBuilder start set Bundle and ID Zone!");
        try {
            Method addBundleMethod = mAudioAttributesBuilder.getClass().getDeclaredMethod("addBundle", Bundle.class);
            addBundleMethod.setAccessible(true); // Make the method accessible
            // Populate your bundle as needed
            addBundleMethod.invoke(mAudioAttributesBuilder, bundle);
            Log.d(TAG, "AudioAttributesBuilder set Bundle suggest!");
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException  e) {
            Log.d(TAG, "AudioAttributesBuilder set Bundle fail! e: " + e.toString());
        }
        return mAudioAttributesBuilder.build();
    }

    public int getAudioFocus() {
        return mAudioFocusValue;
    }
}