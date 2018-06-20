/*
 * *
 *  * Created by Salman Saleem on 6/21/18 12:43 AM
 *  * Copyright (c) 2018 . All rights reserved.
 *  * Last modified 6/21/18 12:32 AM
 *
 */

package com.tcv.vassistchat.models;

/**
 * Created by Salman Saleem on 4/11/17.
 *
 *
 * @Author : https://twitter.com/Ben__Cherif
 * @Skype :
 */

public class RegisterIDResponse {

    private boolean success;
    private String message;
    private String registered_id;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRegistered_id() {
        return registered_id;
    }

    public void setRegistered_id(String registered_id) {
        this.registered_id = registered_id;
    }
}
