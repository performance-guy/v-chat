/*
 * *
 *  * Created by Salman Saleem on 6/21/18 12:43 AM
 *  * Copyright (c) 2018 . All rights reserved.
 *  * Last modified 6/21/18 12:33 AM
 *
 */

package com.tcv.vassistchat.helpers;

import android.annotation.TargetApi;
import android.os.Build;
import android.telephony.PhoneNumberFormattingTextWatcher;

/**
 * Created by Salman Saleem on 11/3/16.
 *
 *
 * @Author : https://twitter.com/Ben__Cherif
 * @Skype :
 */

public class PhoneNumberWatcher extends PhoneNumberFormattingTextWatcher {


    @SuppressWarnings("unused")
    public PhoneNumberWatcher() {
        super();
    }

    //TODO solve it! support for android kitkat
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PhoneNumberWatcher(String countryCode) {
        super(countryCode);
    }
}