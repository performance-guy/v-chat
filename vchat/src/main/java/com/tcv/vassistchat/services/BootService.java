/*
 * *
 *  * Created by Salman Saleem on 6/21/18 12:43 AM
 *  * Copyright (c) 2018 . All rights reserved.
 *  * Last modified 6/21/18 12:34 AM
 *
 */

package com.tcv.vassistchat.services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.tcv.vassistchat.helpers.PreferenceManager;

/**
 * Created by Salman Saleem on 6/25/16.
 *
 *
 *
 */

public class BootService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
        new Handler().postDelayed(() -> {
            if (PreferenceManager.getToken(BootService.this) != null && !PreferenceManager.isNeedProvideInfo(this)) {
                startService(new Intent(BootService.this, MainService.class));
            }
        }, 1000);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // service On start
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        // service finished
        super.onDestroy();
    }
}
