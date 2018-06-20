/*
 * *
 *  * Created by Salman Saleem on 6/21/18 12:43 AM
 *  * Copyright (c) 2018 . All rights reserved.
 *  * Last modified 6/21/18 12:33 AM
 *
 */

package com.tcv.vassistchat.services.firebase;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.tcv.vassistchat.app.AppConstants;
import com.tcv.vassistchat.helpers.PreferenceManager;
import com.tcv.vassistchat.models.users.Pusher;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Salman Saleem on 4/11/17.
 *
 *
 * @Author : https://twitter.com/Ben__Cherif
 * @Skype :
 */

public class RegistrationIntentService extends FirebaseInstanceIdService {
    // abbreviated tag name
    private static final String TAG = RegistrationIntentService.class.getName();

    @Override
    public void onTokenRefresh() {
        if (PreferenceManager.getToken(this) == null) return;
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        if (refreshedToken == null) return;
        EventBus.getDefault().post(new Pusher(AppConstants.EVENT_BUS_REFRESH_TOKEN_FCM, refreshedToken));
    }
}