/*
 * *
 *  * Created by Salman Saleem on 6/21/18 12:43 AM
 *  * Copyright (c) 2018 . All rights reserved.
 *  * Last modified 6/21/18 12:34 AM
 *
 */

package com.tcv.vassistchat.helpers;

import com.tcv.vassistchat.models.messages.MessagesModel;

import java.util.Comparator;

/**
 * Created by Salman Saleem on 6/24/16.
 *
 *
 *
 */

public class StringDateComparatorMessages implements Comparator<MessagesModel> {

    public int compare(MessagesModel app1, MessagesModel app2) {
        String date1 = app1.getDate();
        String date2 = app2.getDate();
        return date1.compareTo(date2);
    }
    /* try {
            Collections.sort(messagesModelList, new StringDateComparatorMessages());
        } catch (Exception e) {
            AppHelper.LogCat("messages compare " + e.getMessage());
        }*/
}

