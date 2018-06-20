/*
 * *
 *  * Created by Salman Saleem on 6/21/18 12:43 AM
 *  * Copyright (c) 2018 . All rights reserved.
 *  * Last modified 6/21/18 12:34 AM
 *
 */

package com.tcv.vassistchat.models.users.contacts;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Salman Saleem on 20/02/2016.
 *
 */
public class ContactsModel extends RealmObject {
    @PrimaryKey
    private int id;
    private int contactID;
    private String username;
    private String phone;
    private String phoneTmp;
    private boolean Linked;
    private boolean Activate;
    private boolean Exist;
    private String image;
    private String status;
    private String status_date;
    private String socketId;

    @Ignore
    private boolean online;


    private String registered_id;

    public ContactsModel() {

    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public String getRegistered_id() {
        return registered_id;
    }

    public void setRegistered_id(String registered_id) {
        this.registered_id = registered_id;
    }

    public String getSocketId() {
        return socketId;
    }

    public void setSocketId(String socketId) {
        this.socketId = socketId;
    }


    public String getPhoneTmp() {
        return phoneTmp;
    }

    public void setPhoneTmp(String phoneTmp) {
        this.phoneTmp = phoneTmp;
    }


    public boolean isActivate() {
        return Activate;
    }

    public void setActivate(boolean activate) {
        Activate = activate;
    }

    public boolean isExist() {
        return Exist;
    }

    public void setExist(boolean exist) {
        Exist = exist;
    }


    public int getContactID() {
        return contactID;
    }

    public void setContactID(int contactID) {
        this.contactID = contactID;
    }


    public String getStatus_date() {
        return status_date;
    }

    public void setStatus_date(String status_date) {
        this.status_date = status_date;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isLinked() {
        return Linked;
    }

    public void setLinked(boolean linked) {
        Linked = linked;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
