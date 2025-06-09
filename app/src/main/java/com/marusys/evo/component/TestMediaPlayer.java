package com.marusys.evo.component;

import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import common.AppInfo;

public class TestMediaPlayer implements MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener, SurfaceHolder.Callback {
    private final String TAG = AppInfo.OWNER + "[TestMediaPlayer]";
    private PlayerComponent mPlayerComponent = null;

    private MediaPlayer mMediaPlayer;
    private MediaMetadataRetriever mMediaMetadataRetriever; // For claim media data
    private AudioAttributes mAudioAttributes = null;
    private int mAudioFocus = 0;

    private static TestMediaPlayer mInstance = null;
    public static TestMediaPlayer getInstance() {
        if(mInstance==null) {
            mInstance = new TestMediaPlayer();
        }
        return mInstance;
    }

    private TestMediaPlayer() {
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnCompletionListener(this);
        mMediaPlayer.setOnPreparedListener(this);
        mMediaMetadataRetriever = new MediaMetadataRetriever();
    };

    private final Runnable mRunnablePlaybackTimeUpdateTask = new Runnable() {
        public void run() {
            if (mMediaPlayer.isPlaying()) {
                int playtime = mMediaPlayer.getCurrentPosition();
                int duration = mMediaPlayer.getDuration();
                mPlayerComponent.duration.setText(String.format("%s ms / %s", String.valueOf(playtime), String.valueOf(duration + " ms")));
            }
        }
    };

    class PlaybackTimerTask extends TimerTask {
        @Override
        public void run() {
            // Updating TextView must be in UI thread
            mRunnablePlaybackTimeUpdateTask.run();
        }
    }

    private Timer mPlaybackTimer = null;
    private PlaybackTimerTask mPlaybackTimerTask = null;

    /**
      * Override method for Surface View and Media Player call back funtion
      * .. TO DO ...
      * .. TO DO ...
      * .. TO DO ...
     */

    @Override
    public void onCompletion(MediaPlayer mp) {
        if(null != mPlayerComponent) {
            mPlayerComponent.duration.setText("Complete");
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        // Cancel previous Timer
        if (mPlaybackTimer != null) {
            mPlaybackTimer.cancel();
            mPlaybackTimerTask.cancel();
        }
        // Once Timer is canceled, it is required to create a new timer.
        mPlaybackTimer = new Timer();
        mPlaybackTimerTask = new PlaybackTimerTask();
        // Timer starts after 1000 ms with 1000 ms period
        mPlaybackTimer.schedule(mPlaybackTimerTask, 1000, 1000);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        mMediaPlayer.setDisplay(holder);
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

    }

    public void initPlayerComponent(PlayerComponent p) {
        if(null != p) {
            mPlayerComponent = p;
            Log.d(TAG, "initPlayerComponent Suggest");

            mPlayerComponent.surfaceHolder = mPlayerComponent.surfaceView.getHolder();
            mPlayerComponent.surfaceHolder.addCallback(this);
        } else {
            Log.d(TAG, "initPlayerComponent False");
        }
    }

    public void Play() {
        Log.d(TAG, "On Button [Play]");
        AssetFileDescriptor afd;
        try {
            afd = AppInfo.getInstance().getAppResource().openRawResourceFd(mPlayerComponent.raw_id);
        } catch (Resources.NotFoundException e) {
            Log.d(TAG, "Play: " + e.toString());
            return;
        }
        try {
            mMediaPlayer.reset();

            if(null != mAudioAttributes) {
                mMediaPlayer.setAudioAttributes(mAudioAttributes);
                Log.d(TAG, "Play: setAudioAttributes( " + mAudioAttributes.getUsage() + " " + mAudioAttributes.getContentType() + " )");
            }

            mMediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getDeclaredLength());

            mMediaMetadataRetriever.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getDeclaredLength());

            // Title
            mPlayerComponent.title.setText(String.format("Title: %s",  mMediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)));
            // Artist
            mPlayerComponent.artist.setText(String.format("Artist: %s", mMediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)));
            // Album
            mPlayerComponent.album.setText(String.format("Album: %s", mMediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM)));
            // Genre
            mPlayerComponent.genre.setText(String.format("Genre: %s", mMediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE)));

            mMediaPlayer.prepare();
            mMediaPlayer.start();
        } catch (IOException e) {
            Log.d(TAG, "Play: " + e.toString());
            e.printStackTrace();
        }
    }

    public void Resume() {
        try {
            mMediaPlayer.start();
        } catch (IllegalStateException e) {
            Log.d(TAG, "Resume: " + e.toString());
        }
    }

    public void Pause() {
        try {
            mMediaPlayer.pause();
        } catch (IllegalStateException e) {
            Log.d(TAG, "Pause: " + e.toString());
        }
    }

    public void Stop() {
        Log.d(TAG, "On Button [Stop]");
        try {
            mMediaPlayer.pause();
        } catch (IllegalStateException e) {
            Log.d(TAG, "Stop: " + e.toString());
        }
    }

    private AudioManager.OnAudioFocusChangeListener onAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            switch (focusChange) {
                case AudioManager.AUDIOFOCUS_GAIN:
                    Log.d(TAG, "AUDIOFOCUS_GAIN" + String.valueOf(focusChange));
                    Toast.makeText(AppInfo.getInstance().getApplicationContext(), "AUDIOFOCUS_GAIN", Toast.LENGTH_LONG).show();
                    Play();
                    break;
                case AudioManager.AUDIOFOCUS_LOSS:
                    Log.d(TAG, "AUDIOFOCUS_LOSS" + String.valueOf(focusChange));
                    Toast.makeText(AppInfo.getInstance().getApplicationContext(), "AUDIOFOCUS_LOSS", Toast.LENGTH_LONG).show();
                    Stop();
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                    Log.d(TAG, "AUDIOFOCUS_LOSS_TRANSIENT" + String.valueOf(focusChange));
                    Toast.makeText(AppInfo.getInstance().getApplicationContext(), "AUDIOFOCUS_LOSS_TRANSIENT", Toast.LENGTH_LONG).show();
                    Pause();
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                    Log.d(TAG, "AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK" + String.valueOf(focusChange));
                    Toast.makeText(AppInfo.getInstance().getApplicationContext(), "AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK", Toast.LENGTH_LONG).show();
                    break;
                default:
                    Log.d(TAG, "Unknown" + String.valueOf(focusChange));
                    Toast.makeText(AppInfo.getInstance().getApplicationContext(), "unknown", Toast.LENGTH_LONG).show();
                    Stop();
                    break;
            }
        }
    };

    public void applyAttribute() {
        Handler handler = new Handler(Looper.getMainLooper());
        try {
            mAudioAttributes = TestInfomation.getInstance().getSelectAudioAttributes();
            mAudioFocus = TestInfomation.getInstance().getAudioFocus();
            Log.d(TAG, "applyAttribute: " + mAudioAttributes.toString());
            int res = TestAudioManagerController.getInstance().requestFocusAudio(mAudioFocus, mAudioAttributes, onAudioFocusChangeListener, handler);
            mMediaPlayer.reset();
            if(AudioManager.AUDIOFOCUS_REQUEST_GRANTED == res) Play();
        } catch (IllegalArgumentException e) {
            Log.d(TAG, "applyAttribute: " + e.toString());
        }
    }
    public PlayerComponent getPlayerComponent() {
        return mPlayerComponent;
    }
}