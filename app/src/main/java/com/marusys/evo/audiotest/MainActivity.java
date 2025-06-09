package com.marusys.evo.audiotest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.NotificationManager;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.marusys.evo.component.PlayerComponent;
import com.marusys.evo.component.TestAudioManagerController;
import com.marusys.evo.component.TestFunction;
import com.marusys.evo.component.TestInfomation;
import com.marusys.evo.component.TestMediaPlayer;

import common.AppInfo;

public class MainActivity extends AppCompatActivity {
    private LinearLayout mMainLayout = null;
    private final String TAG = AppInfo.OWNER + "[MainActivity]";
    private SurfaceView mSurfaceView;
    private static MainActivity mInstance;
    private static CommandReceiver mCommandReceiver = new CommandReceiver();
    private String mUsbMountPoint;

    public static MainActivity getInstance() {
        return mInstance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        IntentFilter filter = new IntentFilter(Intent.ACTION_MEDIA_MOUNTED);
//        filter.addDataScheme("file");
//        registerReceiver(mCommandReceiver, filter);
        IntentFilter filter_1 = new IntentFilter(Intent.ACTION_MEDIA_MOUNTED);
        filter_1.addDataScheme("file");
        IntentFilter filter_2 = new IntentFilter(Intent.ACTION_MEDIA_UNMOUNTED);
        filter_2.addDataScheme("file");
        registerReceiver(mCommandReceiver, filter_1);
        registerReceiver(mCommandReceiver, filter_2);

        mInstance = this;
        setContentView(R.layout.activity_main);
        common.AppInfo.getInstance().setApplicationContext(getApplicationContext());
        common.AppInfo.getInstance().setWindowManager(getWindowManager());
        this.getSystemService(NotificationManager.class);
        mMainLayout = (LinearLayout) findViewById(R.id.main_layout);
        generateUI();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    /**
     * @implNote Basic button clicker when no item imported
     */
    View.OnClickListener defaultButtonClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Button btn = (Button) v;
            Log.d(TAG, "onClick: Clicked item " + btn.getText().toString());
        }
    };

    private TextView AddTextView(String text, int item_style, int layout_style) {
        TextView text_view = UiStyle.getInstance().newTextViewStyle(item_style, layout_style);
        text_view.setText(text);
        mMainLayout.addView(text_view);
        return text_view;
    }

    // Return Surface View
    private SurfaceView AddSurfaceView() {
        SurfaceView surfaceview = UiStyle.getInstance().newSurfaceView();
        mMainLayout.addView(surfaceview);
        return surfaceview;
    }

    private PlayerComponent AddMediaPlayerView() {
        PlayerComponent pc = new PlayerComponent();
        pc.surfaceView = AddSurfaceView();
        pc.selection = AddTextView("Please Select Test Media File", UiStyle.STYLE_1, UiStyle.LAYOUT_STYLE_1);
        pc.duration = AddTextView("duration / total time", UiStyle.STYLE_1, UiStyle.LAYOUT_STYLE_1);
        pc.title = AddTextView("Title", UiStyle.STYLE_1, UiStyle.LAYOUT_STYLE_1);
        pc.artist = AddTextView("Artist", UiStyle.STYLE_1, UiStyle.LAYOUT_STYLE_1);
        pc.album = AddTextView("Album", UiStyle.STYLE_1, UiStyle.LAYOUT_STYLE_1);
        pc.genre = AddTextView("Genre", UiStyle.STYLE_1, UiStyle.LAYOUT_STYLE_1);
        return pc;
    }

    private Button AddButton(String content, View.OnClickListener event, int item_style, int layout_style) {
        Button button = UiStyle.getInstance().newButtonStyle(item_style, layout_style);
        button.setText(content);
        if (event == null) {
            button.setOnClickListener(defaultButtonClicked);
        }
        button.setOnClickListener(event);
        mMainLayout.addView(button);
        return button;
    }

    private CheckBox AddCheckBox(String content, CompoundButton.OnCheckedChangeListener onCheck, int item_style, int layout_style) {
        CheckBox check_box = UiStyle.getInstance().newCheckBoxStyle(item_style, layout_style);
        check_box.setText(content);
        check_box.setOnCheckedChangeListener(onCheck);
        mMainLayout.addView(check_box);
        return check_box;
    }

    private Spinner AddDropDownBox(String[] list, AdapterView.OnItemSelectedListener event, int item_style, int layout_style) {
        Spinner spinner = UiStyle.getInstance().newSpinnerStyle(item_style, layout_style);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(event);
        mMainLayout.addView(spinner);
        return spinner;
    }

    private SeekBar Adddro(SeekBar.OnSeekBarChangeListener listener, int item_style, int layout_style) {
        SeekBar seekBar = UiStyle.getInstance().newSeekBarStyle(item_style, layout_style);
        seekBar.setOnSeekBarChangeListener(listener);
        mMainLayout.addView(seekBar);
        return seekBar;
    }

    private void setPermission() {
        int permission = this.checkSelfPermission(android.Manifest.permission.MODIFY_AUDIO_SETTINGS);
        try {
            if (PackageManager.PERMISSION_GRANTED != permission) {
                Log.d(TAG, "requestPermissions requestPermissions");
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.MODIFY_AUDIO_SETTINGS}, PackageManager.PERMISSION_GRANTED);
            }
        } catch (IllegalArgumentException e) {
            Log.d(TAG, "[init/permission] error: " + e.getMessage());
        }
        permission = this.checkSelfPermission(android.Manifest.permission.MODIFY_AUDIO_SETTINGS);
        Log.d(TAG, "permission = " +  ((PackageManager.PERMISSION_GRANTED == permission) ? "PERMISSION_GRANTED" : "PERMISSION_NOT_GRANTED"));
        return;
    }

    public View.OnClickListener setPermissionButton() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPermission();
            }
        };
    }

    public void onHandleBroadcast(Intent intent) {
        switch (intent.getAction().toString()) {
            case "PLAY_MUSIC":
                TestMediaPlayer.getInstance().Stop();
                TestAudioManagerController.getInstance().setStreamType("STREAM_MUSIC");
                TestInfomation.getInstance().selAttUsageValue("USAGE_MEDIA");
                TestInfomation.getInstance().selAttContentValue("CONTENT_TYPE_MUSIC");
                TestMediaPlayer.getInstance().applyAttribute();;
                break;
            case "PLAY_NAV":
                TestMediaPlayer.getInstance().Stop();
                TestInfomation.getInstance().selAttUsageValue("USAGE_ASSISTANCE_NAVIGATION_GUIDANCE");
                TestMediaPlayer.getInstance().applyAttribute();;
                break;
            case "PLAY_SYS":
                TestMediaPlayer.getInstance().Stop();
                TestInfomation.getInstance().selAttUsageValue("USAGE_ALARM");
                TestMediaPlayer.getInstance().applyAttribute();;
                break;
            case "PLAY_CALL":
                TestMediaPlayer.getInstance().Stop();
                TestInfomation.getInstance().selAttUsageValue("USAGE_VOICE_COMMUNICATION");
                TestMediaPlayer.getInstance().applyAttribute();;
                break;
            case "PLAY_VOICE":
                TestMediaPlayer.getInstance().Stop();
                TestInfomation.getInstance().selAttUsageValue("USAGE_ASSISTANT");
                TestMediaPlayer.getInstance().applyAttribute();;
                break;
            case "PLAY_ALARM":
                TestMediaPlayer.getInstance().Stop();
                TestInfomation.getInstance().selAttUsageValue("USAGE_ALARM");
                TestMediaPlayer.getInstance().applyAttribute();;
                break;
            case "PLAY":
                TestMediaPlayer.getInstance().Play();
                break;
            case "STOP":
                TestMediaPlayer.getInstance().Stop();
                break;
            default:
                break;
        }
    }

    private void generateUI() {
//        AddTextView("Device Infomation", UiStyle.STYLE_2, UiStyle.LAYOUT_STYLE_2);
        /** NOTE
         * This Zone show device environment which run test. In future when need check
         */

//        AddTextView("Package Name: " + getApplicationContext().getPackageName(),
//                UiStyle.STYLE_1, UiStyle.LAYOUT_STYLE_1);
//        AddTextView("Screen size (WxH): " + TestFunction.getInstance().getScreenSize(),
//                UiStyle.STYLE_1, UiStyle.LAYOUT_STYLE_1);
//        AddTextView("AndroidOS: " + System.getProperty("os.version"),
//                UiStyle.STYLE_1, UiStyle.LAYOUT_STYLE_1);
//        AddTextView("SDK: " + android.os.Build.VERSION.SDK,
//                UiStyle.STYLE_1, UiStyle.LAYOUT_STYLE_1);
//        AddTextView("Device: " + android.os.Build.DEVICE,
//                UiStyle.STYLE_1, UiStyle.LAYOUT_STYLE_1);
//        AddTextView("Model: " + android.os.Build.MODEL,
//                UiStyle.STYLE_1, UiStyle.LAYOUT_STYLE_1);
//        AddTextView("Product: " + android.os.Build.PRODUCT,
//                UiStyle.STYLE_1, UiStyle.LAYOUT_STYLE_1);
//        AddTextView("Version: " + TestFunction.getInstance().currentVersion(),
//                UiStyle.STYLE_1, UiStyle.LAYOUT_STYLE_1);

        // Surface View
        AddTextView("Player View", UiStyle.STYLE_2, UiStyle.LAYOUT_STYLE_2);
        PlayerComponent playerComponent = AddMediaPlayerView();
        TestFunction.getInstance().initMediaPlayer(playerComponent);

        // Test feature
        AddTextView("Test (Build-In) Media File", UiStyle.STYLE_2, UiStyle.LAYOUT_STYLE_2);

        AddTextView("Select Media File:", UiStyle.STYLE_1, UiStyle.LAYOUT_STYLE_1);
        // Drop Down Box for select media test file

        AddDropDownBox(
            TestInfomation.getInstance().getTrackList(),
            TestFunction.getInstance().onDropdownMediaSelected(),
            UiStyle.STYLE_0,
            UiStyle.LAYOUT_STYLE_3
        );

        AddButton(
            "PLAY",
            TestFunction.getInstance().callPlay(),
            UiStyle.STYLE_4,
            UiStyle.LAYOUT_STYLE_2);

        AddButton(
            "PAUSE",
            TestFunction.getInstance().callPause(),
            UiStyle.STYLE_4,
            UiStyle.LAYOUT_STYLE_2);

        AddButton(
            "RESUME",
            TestFunction.getInstance().callResume(),
            UiStyle.STYLE_4,
            UiStyle.LAYOUT_STYLE_2);

        AddButton(
            "STOP",
            TestFunction.getInstance().callStop(),
            UiStyle.STYLE_4,
            UiStyle.LAYOUT_STYLE_2);

        AddTextView("AudioManager", UiStyle.STYLE_2, UiStyle.LAYOUT_STYLE_2);

        AddTextView("Stream Type Selection", UiStyle.STYLE_1, UiStyle.LAYOUT_STYLE_1);
        AddDropDownBox(
                TestAudioManagerController.getInstance().getStreamTypeList(),
                TestFunction.getInstance().onDropdownMediaStreamSelected(),
                UiStyle.STYLE_0,
                UiStyle.LAYOUT_STYLE_3
        );

        AddTextView("Adjust Type Selection", UiStyle.STYLE_1, UiStyle.LAYOUT_STYLE_1);
        AddDropDownBox(
                TestAudioManagerController.getInstance().getAdjustTypeList(),
                TestFunction.getInstance().onDropdownMediaAdjustSelected(),
                UiStyle.STYLE_0,
                UiStyle.LAYOUT_STYLE_3
        );

//        AddButton(
//                "Set Stream Volume",
//                TestFunction.getInstance().onClick_SetStreamVolume(),
//                UiStyle.STYLE_4,
//                UiStyle.LAYOUT_STYLE_2);

//        AddTextView("Volume Change Bar", UiStyle.STYLE_1, UiStyle.LAYOUT_STYLE_1);
//        AddSeekBar(TestFunction.getInstance().onVolumeChangeListener(), UiStyle.STYLE_0, UiStyle.LAYOUT_STYLE_1);
//
//        AddTextView("Send Key", UiStyle.STYLE_2, UiStyle.LAYOUT_STYLE_2);

        AddTextView("Select Media Att. USAGE", UiStyle.STYLE_1, UiStyle.LAYOUT_STYLE_1);

        AddDropDownBox(
                TestInfomation.getInstance().getMediaAttUsageList(),
                TestFunction.getInstance().onDropdown_getMediaAttUsageList(),
                UiStyle.STYLE_0,
                UiStyle.LAYOUT_STYLE_3
        );

        AddTextView("Select Media Att. CONTENT", UiStyle.STYLE_1, UiStyle.LAYOUT_STYLE_1);

        AddDropDownBox(
                TestInfomation.getInstance().getMediaAttContentList(),
                TestFunction.getInstance().onDropdown_getMediaAttContentList(),
                UiStyle.STYLE_0,
                UiStyle.LAYOUT_STYLE_3
        );

        AddTextView("Select request audio focus", UiStyle.STYLE_1, UiStyle.LAYOUT_STYLE_1);

        AddDropDownBox(
                TestInfomation.getInstance().getAudioFocusList(),
                TestFunction.getInstance().onDropdown_getAudioFocusList(),
                UiStyle.STYLE_0,
                UiStyle.LAYOUT_STYLE_3
        );

        AddButton(
                "Apply Selection",
                TestFunction.getInstance().onClick_ApplyNewMediaAtt(),
                UiStyle.STYLE_4,
                UiStyle.LAYOUT_STYLE_2
        );

        AddCheckBox(
                "Set Streamming Audio",
                TestFunction.getInstance().onTestStreamming(),
                UiStyle.STYLE_4,
                UiStyle.LAYOUT_STYLE_2);


        AddCheckBox(
                "Dump pcm audio input bluetooth hfp",
                TestFunction.getInstance().onSetDumpMic(TestAudioManagerController.DUMP_AUDIO_PCM_BT_IN),
                UiStyle.STYLE_0,
                UiStyle.LAYOUT_STYLE_0);
        AddCheckBox(
                "Dump pcm audio input mic",
                TestFunction.getInstance().onSetDumpMic(TestAudioManagerController.DUMP_AUDIO_PCM_MIC_IN),
                UiStyle.STYLE_0,
                UiStyle.LAYOUT_STYLE_0);
        AddCheckBox(
                "Dump pcm audio output mic (audio send to bt hfp phone)",
                TestFunction.getInstance().onSetDumpMic(TestAudioManagerController.DUMP_AUDIO_PCM_MIC_OUT),
                UiStyle.STYLE_0,
                UiStyle.LAYOUT_STYLE_0);
        AddCheckBox(
                "Dump radio input",
                TestFunction.getInstance().onSetDumpMic(TestAudioManagerController.DUMP_AUDIO_PCM_RADIO_IN),
                UiStyle.STYLE_0,
                UiStyle.LAYOUT_STYLE_0);
        AddCheckBox(
                "Dump all audio out awe (8 channels)",
                TestFunction.getInstance().onSetDumpMic(TestAudioManagerController.DUMP_AUDIO_AWE_ALL_OUT),
                UiStyle.STYLE_0,
                UiStyle.LAYOUT_STYLE_0);
        AddCheckBox(
                "Dump audio input awe stream media",
                TestFunction.getInstance().onSetDumpMic(TestAudioManagerController.DUMP_AUDIO_AWE_IN_STREAM_MEDIA),
                UiStyle.STYLE_0,
                UiStyle.LAYOUT_STYLE_0);
        AddCheckBox(
                "Dump audio input awe stream media streaming",
                TestFunction.getInstance().onSetDumpMic(TestAudioManagerController.DUMP_AUDIO_AWE_IN_STREAM_STREAMING),
                UiStyle.STYLE_0,
                UiStyle.LAYOUT_STYLE_0);
        AddCheckBox(
                "Dump audio input awe stream navigation",
                TestFunction.getInstance().onSetDumpMic(TestAudioManagerController.DUMP_AUDIO_AWE_IN_STREAM_NAVIGATION),
                UiStyle.STYLE_0,
                UiStyle.LAYOUT_STYLE_0);
        AddCheckBox(
                "Dump audio input awe stream voice assistant",
                TestFunction.getInstance().onSetDumpMic(TestAudioManagerController.DUMP_AUDIO_AWE_IN_STREAM_VOICE_ASSISTANT),
                UiStyle.STYLE_0,
                UiStyle.LAYOUT_STYLE_0);
        AddCheckBox(
                "Dump audio input awe stream incomming call",
                TestFunction.getInstance().onSetDumpMic(TestAudioManagerController.DUMP_AUDIO_AWE_IN_INCOMMING_CALL),
                UiStyle.STYLE_0,
                UiStyle.LAYOUT_STYLE_0);
        AddCheckBox(
                "Dump audio input awe stream adas sound (no sound)",
                TestFunction.getInstance().onSetDumpMic(TestAudioManagerController.DUMP_AUDIO_AWE_IN_ADAS_SOUND),
                UiStyle.STYLE_0,
                UiStyle.LAYOUT_STYLE_0);
        AddCheckBox(
                "Dump audio input awe stream media out going call",
                TestFunction.getInstance().onSetDumpMic(TestAudioManagerController.DUMP_AUDIO_AWE_IN_OUTGOING_CALL),
                UiStyle.STYLE_0,
                UiStyle.LAYOUT_STYLE_0);

        AddTextView("Selection Audio Mode", UiStyle.STYLE_2, UiStyle.LAYOUT_STYLE_2);

        AddDropDownBox(
                TestInfomation.getInstance().getEntModeList(),
                TestFunction.getInstance().onDropdown_getEntModeList(),
                UiStyle.STYLE_0,
                UiStyle.LAYOUT_STYLE_3
        );

        AddTextView("UPA", UiStyle.STYLE_2, UiStyle.LAYOUT_STYLE_2);

        AddTextView("Selection Play UPA Error", UiStyle.STYLE_2, UiStyle.LAYOUT_STYLE_2);
        AddCheckBox(
                "UPA Error",
                TestFunction.getInstance().onTestUAPSound(),
                UiStyle.STYLE_4,
                UiStyle.LAYOUT_STYLE_2);

        AddTextView("Selection UPA Obstacle Zone", UiStyle.STYLE_2, UiStyle.LAYOUT_STYLE_2);
        AddDropDownBox(
                TestInfomation.getInstance().getUPAObstacleZoneList(),
                TestFunction.getInstance().onDropdown_getUPAObstacleZoneList(),
                UiStyle.STYLE_0,
                UiStyle.LAYOUT_STYLE_3
        );

        AddCheckBox(
                "Set Continuous Tone",
                TestFunction.getInstance().onTestUAPTone(),
                UiStyle.STYLE_4,
                UiStyle.LAYOUT_STYLE_2);

//        AddTextView("I don't know what is this", UiStyle.STYLE_2, UiStyle.LAYOUT_STYLE_2);
//        AddCheckBox(
//                "System on off",
//                TestFunction.getInstance().onTestSystemOffByUser(),
//                UiStyle.STYLE_4,
//                UiStyle.LAYOUT_STYLE_2);

        AddTextView("Selects RVC Gear", UiStyle.STYLE_2, UiStyle.LAYOUT_STYLE_2);
        AddDropDownBox(
                TestInfomation.getInstance().getRVCGearList(),
                TestFunction.getInstance().onDropdown_getRVCGearList(),
                UiStyle.STYLE_0,
                UiStyle.LAYOUT_STYLE_3
        );
        AddTextView("Blank", UiStyle.STYLE_1, UiStyle.LAYOUT_STYLE_2);
    }
}

// 25264b5c62831f9a669d5928e11374c2f21ac196