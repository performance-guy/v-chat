/*
 * *
 *  * Created by Salman Saleem on 6/21/18 12:43 AM
 *  * Copyright (c) 2018 . All rights reserved.
 *  * Last modified 6/21/18 12:32 AM
 *
 */

package com.tcv.vassistchat.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

/**
 * Created by Salman Saleem on 12/18/16.
 *
 *
 * @Author : https://twitter.com/Ben__Cherif
 * @Skype :
 */

public class CircleVideo extends GLSurfaceView {
    public CircleVideo(Context context) {
        super(context);
    }

    public CircleVideo(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private Path clippingPath;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (w != oldw || h != oldh) {
            int radius = Math.min(w, h) / 2;
            clippingPath = new Path();
            clippingPath.addCircle(w / 2, h / 2, radius, Path.Direction.CW);
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        int count = canvas.save();
        canvas.clipPath(clippingPath);
        super.dispatchDraw(canvas);
        canvas.restoreToCount(count);
    }
}
