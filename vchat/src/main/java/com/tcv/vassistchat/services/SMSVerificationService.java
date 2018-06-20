/*
 * *
 *  * Created by Salman Saleem on 6/21/18 12:43 AM
 *  * Copyright (c) 2018 . All rights reserved.
 *  * Last modified 6/21/18 12:34 AM
 *
 */

package com.tcv.vassistchat.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.tcv.vassistchat.activities.main.MainActivity;
import com.tcv.vassistchat.activities.main.PreMainActivity;
import com.tcv.vassistchat.activities.main.welcome.CompleteRegistrationActivity;
import com.tcv.vassistchat.api.APIHelper;
import com.tcv.vassistchat.helpers.AppHelper;
import com.tcv.vassistchat.helpers.PreferenceManager;

/**
 * Created by Salman Saleem on 23/02/2016.
 *
 */
public class SMSVerificationService extends IntentService {


    public SMSVerificationService() {
        super(SMSVerificationService.class.getSimpleName());
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            String code = intent.getStringExtra("code");
            boolean registration = intent.getBooleanExtra("register", true);
            verifyUser(code, registration);
        }
    }

    private void verifyUser(String code, boolean registration) {
        if (registration) {
            APIHelper.initializeAuthService().verifyUser(code).subscribe(joinModelResponse -> {
                if (joinModelResponse.isSuccess()) {
                    PreferenceManager.setIsNewUser(SMSVerificationService.this, true);
                    PreferenceManager.setID(SMSVerificationService.this, joinModelResponse.getUserID());
                    PreferenceManager.setToken(SMSVerificationService.this, joinModelResponse.getToken());
                    PreferenceManager.setIsWaitingForSms(SMSVerificationService.this, false);
                    PreferenceManager.setPhone(SMSVerificationService.this, PreferenceManager.getMobileNumber(SMSVerificationService.this));
                    if (joinModelResponse.isHasBackup()) {
                        PreferenceManager.saveBackupFolder(SMSVerificationService.this, joinModelResponse.getBackup_hash());
                        PreferenceManager.setHasBackup(SMSVerificationService.this, true);
                        Intent intent = new Intent(SMSVerificationService.this, PreMainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } else if (joinModelResponse.isHasProfile()) {
                        PreferenceManager.setHasBackup(SMSVerificationService.this, false);
                        PreferenceManager.setIsNeedInfo(SMSVerificationService.this, false);
                        Intent intent = new Intent(SMSVerificationService.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } else {
                        PreferenceManager.setHasBackup(SMSVerificationService.this, false);
                        PreferenceManager.setIsNeedInfo(SMSVerificationService.this, true);
                        Intent intent = new Intent(SMSVerificationService.this, CompleteRegistrationActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                    }
                    LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(SMSVerificationService.this);
                    localBroadcastManager.sendBroadcast(new Intent(getPackageName() + "closeWelcomeActivity"));

                } else {
                    AppHelper.CustomToast(SMSVerificationService.this, joinModelResponse.getMessage());
                }
            }, throwable -> {
                AppHelper.LogCat("SMS verification failure  SMSVerificationService" + throwable.getMessage());
            });
        } else {
            APIHelper.initialApiUsersContacts().deleteAccountConfirmation(code).subscribe(statusResponse -> {
                if (statusResponse.isSuccess()) {
                    LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(SMSVerificationService.this);
                    localBroadcastManager.sendBroadcast(new Intent(getPackageName() + "closeDeleteAccountActivity"));
                } else {
                    AppHelper.CustomToast(SMSVerificationService.this, statusResponse.getMessage());
                }
            }, throwable -> {
                AppHelper.LogCat("SMS verification failure  SMSVerificationService" + throwable.getMessage());
            });

        }
    }
}
