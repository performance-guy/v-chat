/*
 * *
 *  * Created by Salman Saleem on 6/21/18 12:43 AM
 *  * Copyright (c) 2018 . All rights reserved.
 *  * Last modified 6/21/18 12:32 AM
 *
 */

package com.tcv.vassistchat.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.tcv.vassistchat.services.BootService;


/**
 * Created by Salman Saleem on 12/8/16.
 *
 *
 * @Author : https://twitter.com/Ben__Cherif
 * @Skype :
 */

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {
        String iAction = intent.getAction();
        if (iAction.equals("android.intent.action.BOOT_COMPLETED")) {
            context.startService(new Intent(context, BootService.class));
        }
    }
}