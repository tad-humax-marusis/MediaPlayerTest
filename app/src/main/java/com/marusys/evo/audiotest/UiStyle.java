package com.marusys.evo.audiotest;

import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

public class UiStyle {
    //    private final String TAG = AppInfo.OWNER+"LayoutStyle";
    private static UiStyle mInstance = null;

    public static final int STYLE_0 = 0; // Base as default
    public static final int STYLE_1 = 1;
    public static final int STYLE_2 = 2;
    public static final int STYLE_3 = 3;
    public static final int STYLE_4 = 4;
    public static final int STYLE_5 = 5;

    public static final int LAYOUT_STYLE_0 = 0; // Base as default
    public static final int LAYOUT_STYLE_1 = 1;
    public static final int LAYOUT_STYLE_2 = 2;
    public static final int LAYOUT_STYLE_3 = 3;
    public static final int LAYOUT_STYLE_4 = 4;

    public static final int TOP_MARGIN = 5;
    public static final int BOT_MARGIN = 5;

    public static UiStyle getInstance() {
        if(null==mInstance) {
            mInstance = new UiStyle();
        }
        return mInstance;
    }

    public int GetColor(int Rid) {
        return common.AppInfo.getInstance().getAppResource().getColor(Rid);
    }

    public LinearLayout.LayoutParams newLayoutStyle(int type) {
        LinearLayout.LayoutParams layout = null;
        switch (type) {
            case LAYOUT_STYLE_1:
                // FULL_WIDTH + WRAP_HEIGHT | {0--}
                layout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layout.topMargin = TOP_MARGIN;
                layout.gravity = Gravity.CENTER_VERTICAL|Gravity.LEFT;
                break;
            case LAYOUT_STYLE_2:
                // FULL_WIDTH + WRAP_HEIGHT | {-0-}
                layout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layout.topMargin = TOP_MARGIN;
                layout.gravity = Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL;
                break;
            case LAYOUT_STYLE_3:
                // FULL_WIDTH + WRAP_HEIGHT | {-0-} | extended up/down margin
                layout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layout.topMargin    = 15;
                layout.bottomMargin = 15;
                layout.gravity = Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL;
                break;
            case LAYOUT_STYLE_4:
                // FULL_WIDTH + WRAP_HEIGHT | {--0}
                layout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layout.topMargin = TOP_MARGIN;
                layout.gravity = Gravity.CENTER_VERTICAL|Gravity.RIGHT;
                break;
            case LAYOUT_STYLE_0:
                // FULL_WIDTH + FULL_HEIGHT
            default:
                layout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                break;
        }
        return layout;
    }

    public TextView newTextViewStyle(int style, int layout_style) {
        TextView textview = new TextView(common.AppInfo.getInstance().getApplicationContext());
        textview.setLayoutParams(newLayoutStyle(layout_style));

        int text_type         = Typeface.NORMAL;
        int text_color        = Color.WHITE;
        int back_ground_color = Color.TRANSPARENT;
        float text_size         = 0.0F;

        switch (style) {
            case STYLE_1:
                text_type = Typeface.ITALIC;
                text_color = Color.WHITE;
                break;
            case STYLE_2:
                text_type  = Typeface.BOLD;
                text_color = Color.WHITE;
                text_size  = 20.0F;
                back_ground_color = GetColor(R.color.darkslategrey);
                textview.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                break;
            case STYLE_3:
                text_type = Typeface.BOLD_ITALIC;
                break;
            case STYLE_4:
                text_color = Color.WHITE;
                back_ground_color = GetColor(R.color.darkslategrey);
                break;
            case STYLE_5:
            default:
                return textview;
        }
        textview.setTypeface(null, text_type);
        textview.setTextColor(text_color);
        textview.setBackgroundColor(back_ground_color);
        if(0.0F != text_size) {
            textview.setTextSize(text_size);
        }
        return textview;
    }

    public Button newButtonStyle(int style, int layout_style) {
        Button button = new Button(common.AppInfo.getInstance().getApplicationContext());
        button.setLayoutParams(newLayoutStyle(layout_style));

        int text_type         = Typeface.NORMAL;
        int text_color        = Color.BLACK;
        int back_ground_color = Color.TRANSPARENT;
        int type_face = Typeface.NORMAL;
        switch (style) {
            case STYLE_1:
                text_type = Typeface.BOLD;
                break;
            case STYLE_2:
                text_type = Typeface.ITALIC;
                break;
            case STYLE_3:
                text_type = Typeface.BOLD|Typeface.ITALIC;
                break;
            case STYLE_4:
                text_type = Typeface.BOLD;
                text_color = Color.WHITE;
                back_ground_color = GetColor(R.color.dimgrey);;
                break;
            case STYLE_5:
                text_type = Typeface.BOLD;
                text_color = Color.BLACK;
                back_ground_color = GetColor(R.color.dodgerblue);;
                break;
            case STYLE_0:
            default:
                return button;
        }

        button.setTextColor(text_color);
        button.setBackgroundColor(back_ground_color);
        button.setTypeface(null, text_type);
        return button;
    }

    public CheckBox newCheckBoxStyle(int style, int layout_style) {
        CheckBox checkbox = new CheckBox(common.AppInfo.getInstance().getApplicationContext());
        checkbox.setLayoutParams(newLayoutStyle(layout_style));

        int text_type         = Typeface.NORMAL;
        int text_color        = Color.BLACK;
        int back_ground_color = Color.TRANSPARENT;
        switch (style) {
            case STYLE_1:
                text_color = Color.WHITE;
                break;
            case STYLE_2:
                text_color = Color.GRAY;
                break;
            default:
                return checkbox;
        }

        checkbox.setTextColor(text_color);
        checkbox.setBackgroundColor(back_ground_color);
        checkbox.setTypeface(null, text_type);
        return checkbox;
    }

    public Spinner newSpinnerStyle(int style, int layout_style) {
        Spinner spinner = new Spinner(common.AppInfo.getInstance().getApplicationContext());
        spinner.setLayoutParams(newLayoutStyle(layout_style));

        int back_ground_color = Color.TRANSPARENT;
        int scroll_size = spinner.getScrollBarSize();;

        switch (style) {
            case STYLE_1:
                spinner.setScrollBarSize(scroll_size*2);
                back_ground_color = GetColor(R.color.darkslategrey);
                break;
            case STYLE_0:
            default:
                return spinner;
        }

        spinner.setBackgroundColor(back_ground_color);
        return spinner;
    }

    /**@apiNote
     * The serface don't have another style view
     */
    public SurfaceView newSurfaceView() {
        SurfaceView surface_view = new SurfaceView(common.AppInfo.getInstance().getApplicationContext());
        LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        return surface_view;
    }

    public SeekBar newSeekBarStyle(int style, int layout_style) {
        SeekBar seekbar = new SeekBar(common.AppInfo.getInstance().getApplicationContext());
        seekbar.setLayoutParams(newLayoutStyle(layout_style));
        // STYLE = TO DO
        return seekbar;
    }
}
