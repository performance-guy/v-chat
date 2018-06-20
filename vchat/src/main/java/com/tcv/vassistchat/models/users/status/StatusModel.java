/*
 * *
 *  * Created by Salman Saleem on 6/21/18 12:43 AM
 *  * Copyright (c) 2018 . All rights reserved.
 *  * Last modified 6/21/18 12:34 AM
 *
 */

package com.tcv.vassistchat.models.users.status;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Salman Saleem on 28/04/2016.
 *
 */
public class StatusModel extends RealmObject {
    @PrimaryKey
    private int id;
    private String status;
    private int userID;
    private boolean current;

    public StatusModel() {

    }

    public boolean getCurrent() {
        return current;
    }

    public void setCurrent(boolean current) {
        this.current = current;
    }


    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
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


}
