/*
 * *
 *  * Created by Salman Saleem on 6/21/18 12:43 AM
 *  * Copyright (c) 2018 . All rights reserved.
 *  * Last modified 6/21/18 12:32 AM
 *
 */

package com.tcv.vassistchat.models.users.contacts;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Salman Saleem on 3/14/17.
 *
 *
 * @Author : https://twitter.com/Ben__Cherif
 * @Skype :
 */

public class UsersBlockModel extends RealmObject {
    @PrimaryKey
    private int id;
    private ContactsModel contactsModel;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ContactsModel getContactsModel() {
        return contactsModel;
    }

    public void setContactsModel(ContactsModel contactsModel) {
        this.contactsModel = contactsModel;
    }
}
