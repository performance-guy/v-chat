/*
 * *
 *  * Created by Salman Saleem on 6/21/18 12:43 AM
 *  * Copyright (c) 2018 . All rights reserved.
 *  * Last modified 6/21/18 12:33 AM
 *
 */

package com.tcv.vassistchat.api;

import com.tcv.vassistchat.api.apiServices.AuthService;
import com.tcv.vassistchat.api.apiServices.ConversationsService;
import com.tcv.vassistchat.api.apiServices.GroupsService;
import com.tcv.vassistchat.api.apiServices.MessagesService;
import com.tcv.vassistchat.api.apiServices.UsersService;
import com.tcv.vassistchat.app.WhatsCloneApplication;

/**
 * Created by Salman Saleem on 4/11/17.
 *
 *
 * @Author : https://twitter.com/Ben__Cherif
 * @Skype :
 */

public class APIHelper {

    public static UsersService initialApiUsersContacts() {
        APIService mApiService = APIService.with(WhatsCloneApplication.getInstance());
        return new UsersService(WhatsCloneApplication.getRealmDatabaseInstance(), WhatsCloneApplication.getInstance(), mApiService);
    }


    public static GroupsService initializeApiGroups() {
        APIService mApiService = APIService.with(WhatsCloneApplication.getInstance());
        return new GroupsService(WhatsCloneApplication.getRealmDatabaseInstance(), WhatsCloneApplication.getInstance(), mApiService);
    }

    public static ConversationsService initializeConversationsService() {
        return new ConversationsService(WhatsCloneApplication.getRealmDatabaseInstance());
    }

    public static MessagesService initializeMessagesService() {
        return new MessagesService(WhatsCloneApplication.getRealmDatabaseInstance());
    }

    public static AuthService initializeAuthService() {
        APIService mApiService = APIService.with(WhatsCloneApplication.getInstance());
        return new AuthService(WhatsCloneApplication.getInstance(), mApiService);
    }
}
