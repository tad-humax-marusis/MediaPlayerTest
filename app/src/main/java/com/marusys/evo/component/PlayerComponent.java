package com.marusys.evo.component;

import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

public class PlayerComponent {
    public SurfaceView surfaceView;
    public TextView title;
    public TextView artist;
    public TextView album;
    public TextView genre;

    public TextView selection;
    public int raw_id;

    public TextView duration;
    public SurfaceHolder surfaceHolder;
}