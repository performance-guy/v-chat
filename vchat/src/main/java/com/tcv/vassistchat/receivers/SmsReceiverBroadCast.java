/*
 * *
 *  * Created by Salman Saleem on 6/21/18 12:43 AM
 *  * Copyright (c) 2018 . All rights reserved.
 *  * Last modified 6/21/18 12:34 AM
 *
 */

package com.tcv.vassistchat.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import com.tcv.vassistchat.app.AppConstants;
import com.tcv.vassistchat.helpers.AppHelper;
import com.tcv.vassistchat.helpers.PreferenceManager;
import com.tcv.vassistchat.services.SMSVerificationService;


/**
 * Created by Salman Saleem on 23/02/2016.
 *
 */
public class SmsReceiverBroadCast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        final Bundle bundle = intent.getExtras();
        try {
            if (bundle != null) {
                Object[] pdusObj = (Object[]) bundle.get("pdus");
                for (Object aPdusObj : pdusObj) {
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) aPdusObj);
                    String message = currentMessage.getDisplayMessageBody();

                    // verification code from sms
                    String verificationCode = getVerificationCode(message);
                    AppHelper.LogCat("code received SmsReceiverBroadCast : " + verificationCode);

                    if (PreferenceManager.getToken(context) != null) return;
                    if (verificationCode != null && verificationCode.length() == 6) {
                        if (PreferenceManager.getID(context) != 0 || PreferenceManager.getToken(context) != null) {
                            Intent mIntent = new Intent(context, SMSVerificationService.class);
                            mIntent.putExtra("code", verificationCode);
                            mIntent.putExtra("register", false);
                            context.startService(mIntent);
                        } else {
                            Intent mIntent = new Intent(context, SMSVerificationService.class);
                            mIntent.putExtra("code", verificationCode);
                            mIntent.putExtra("register", true);
                            context.startService(mIntent);
                        }

                    }

                }
            }
        } catch (Exception e) {
            AppHelper.LogCat("Exception : SmsReceiverBroadCast " + e.getMessage());
        }
    }

    /**
     * Getting the Code from sms message body
     * ':' is the separator of OTP from the message
     *
     * @param message this is parameter for  getVerificationCodemethod
     * @return return value
     */
    private String getVerificationCode(String message) {
        String code;
        int index = message.indexOf(AppConstants.CODE_DELIMITER);

        if (index != -1) {
            int start = index + 2;
            int length = 6;
            code = message.substring(start, start + length);
            return code;
        }

        return null;
    }
}