/*
 * *
 *  * Created by Salman Saleem on 6/21/18 12:43 AM
 *  * Copyright (c) 2018 . All rights reserved.
 *  * Last modified 6/21/18 12:34 AM
 *
 */

package com.tcv.vassistchat.helpers;

import com.tcv.vassistchat.models.messages.ConversationsModel;

import java.util.Comparator;

/**
 * Created by Salman Saleem on 6/24/16.
 *
 *
 *
 */

public class StringDateComparatorConversations implements Comparator<ConversationsModel> {

    public int compare(ConversationsModel app1, ConversationsModel app2) {

        String date1 = app1.getMessageDate();
        String date2 = app2.getMessageDate();
        return date2.compareTo(date1);
    }
    /* try {
            Collections.sort(messagesModelList, new StringDateComparatorMessages());
        } catch (Exception e) {
            AppHelper.LogCat("messages compare " + e.getMessage());
        }*/
}

