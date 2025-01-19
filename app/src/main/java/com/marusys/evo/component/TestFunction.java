package com.marusys.evo.component;

import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.SeekBar;

public class TestFunction {
    private final String TAG = common.AppInfo.OWNER+"TestFunction";
    private TestFunction() {};
    private static TestFunction mInstance = null;

    public static TestFunction getInstance() {
        if(mInstance==null) {
            mInstance = new TestFunction();
        }
        return mInstance;
    }

    public String currentVersion() {
        double release=Double.parseDouble(Build.VERSION.RELEASE.replaceAll("(\\d+[.]\\d+)(.*)","$1"));
        String codeName="Unsupported";//below Jelly Bean

        if(release >= 4.1 && release < 4.4) codeName = "Jelly Bean";
        else if(release < 5)   codeName="Kit Kat";
        else if(release < 6)   codeName="Lollipop";
        else if(release < 7)   codeName="Marshmallow";
        else if(release < 8)   codeName="Nougat";
        else if(release < 9)   codeName="Oreo";
        else if(release < 10)  codeName="Pie";
        else if(release >= 10) codeName="Android "+((int)release);

        //since API 29 no more candy code names
        return codeName+" ("+release+"), api level "+Build.VERSION.SDK_INT;
    }

    public String getScreenSize() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        common.AppInfo.getInstance().getWindowManager().getDefaultDisplay().getRealMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        return (String.valueOf(width) + "x" + String.valueOf(height));
    }

    public View.OnClickListener callPlay() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TestMediaPlayer.getInstance().Play();
            }
        };
    }

    public View.OnClickListener callResume() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TestMediaPlayer.getInstance().Resume();
            }
        };
    }

    public View.OnClickListener callPause() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TestMediaPlayer.getInstance().Pause();
            }
        };
    }

    public View.OnClickListener callStop() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TestMediaPlayer.getInstance().Stop();
            }
        };
    }

    public CompoundButton.OnCheckedChangeListener onTestStreamming() {
        return new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String str = "streamming=" + (isChecked?"true":"false");
                TestAudioManagerController.getInstance().audioSetParameters(str);
            }
        };
    }

    public CompoundButton.OnCheckedChangeListener onTestUAPSound() {
        return new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String str = "upa_audio_error=" + (isChecked?"true":"false");
                TestAudioManagerController.getInstance().audioSetParameters(str);
            }
        };
    }

    public CompoundButton.OnCheckedChangeListener onTestSystemOffByUser() {
        return new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String str = "systemOffByUser=" + (isChecked?"on":"off");
                TestAudioManagerController.getInstance().audioSetParameters(str);
            }
        };
    }

    /*** @apiNote
     * Set enable dump audio mic out (48k bps) go in from PCM
     * @return
     */
    public CompoundButton.OnCheckedChangeListener onSetDumpMic(int positionId) {
        return new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                TestAudioManagerController.getInstance().setMicDump(positionId, isChecked);
            }
        };
    }

    public SeekBar.OnSeekBarChangeListener onVolumeChangeListener() {
        return new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d(TAG, "onProgressChanged: " + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.d(TAG, "onStartTrackingTouch: ");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d(TAG, "onStopTrackingTouch: ");
            }
        };
    }

    public AdapterView.OnItemSelectedListener onDropdownMediaSelected() {
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selected_item = parentView.getSelectedItem().toString();
                int media_raw_id = TestInfomation.getInstance().getMediaFileRawId(selected_item);
                PlayerComponent mpc = TestMediaPlayer.getInstance().getPlayerComponent();
                if(null != mpc) {
                    mpc.selection.setText(selected_item);
                    mpc.raw_id = media_raw_id;
                }
                Log.d(TAG, "onItemSelected = " + selected_item);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // nothing
            }
        };
    }

    public AdapterView.OnItemSelectedListener onDropdownMediaStreamSelected() {
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selected_item = parentView.getSelectedItem().toString();
                TestAudioManagerController.getInstance().setStreamType(selected_item);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // nothing
            }
        };
    }

    public AdapterView.OnItemSelectedListener onDropdownMediaAdjustSelected() {
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selected_item = parentView.getSelectedItem().toString();
                TestAudioManagerController.getInstance().setAdjustValue(selected_item);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // nothing
            }
        };
    }

    public View.OnClickListener onClick_SetStreamVolume() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TestAudioManagerController.getInstance().doSetStreamVolume();
            }
        };
    }

    public void initMediaPlayer(PlayerComponent pc) {
        TestMediaPlayer.getInstance().initPlayerComponent(pc);
    }

    /**
     * THIS ZONE TEST FOR AUDIO ATTRIBUTE
     * ...
     * ...
     */

    public AdapterView.OnItemSelectedListener onDropdown_getMediaAttUsageList() {
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selected_item = parentView.getSelectedItem().toString();
                TestInfomation.getInstance().selAttUsageValue(selected_item);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        };
    }

    public AdapterView.OnItemSelectedListener onDropdown_getMediaAttContentList() {
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selected_item = parentView.getSelectedItem().toString();
                TestInfomation.getInstance().selAttContentValue(selected_item);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        };
    }

    public AdapterView.OnItemSelectedListener onDropdown_getEntModeList() {
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selected_item = parentView.getSelectedItem().toString();
                String array = "audio_entertainment_mode=" + selected_item;
                TestAudioManagerController.getInstance().audioSetParameters(array);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        };
    }

    public AdapterView.OnItemSelectedListener onDropdown_getUPAObstacleZoneList() {
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selected_item = parentView.getSelectedItem().toString();
                String str = "upa_audio_normal=" + selected_item;
                TestAudioManagerController.getInstance().audioSetParameters(str);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        };
    }
    public View.OnClickListener onClick_ApplyNewMediaAtt() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TestMediaPlayer.getInstance().applyAttribute();
            }
        };
    }
}