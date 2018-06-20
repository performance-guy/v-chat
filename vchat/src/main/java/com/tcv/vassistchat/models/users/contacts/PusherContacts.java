/*
 * *
 *  * Created by Salman Saleem on 6/21/18 12:43 AM
 *  * Copyright (c) 2018 . All rights reserved.
 *  * Last modified 6/21/18 12:34 AM
 *
 */

package com.tcv.vassistchat.models.users.contacts;

import java.util.List;

/**
 * Created by Salman Saleem on 04/05/2016.
 *
 */
public class PusherContacts {
    private String action;
    private List<ContactsModel> contactsModelList;
    private Throwable throwable;

    public PusherContacts(String action) {
        this.action = action;
    }

    public PusherContacts(String action, Throwable throwable) {
        this.action = action;
        this.throwable = throwable;
    }

    public PusherContacts(String action, List<ContactsModel> contactsModelList) {
        this.action = action;
        this.contactsModelList = contactsModelList;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public List<ContactsModel> getContactsModelList() {
        return contactsModelList;
    }

    public void setContactsModelList(List<ContactsModel> contactsModelList) {
        this.contactsModelList = contactsModelList;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }
}