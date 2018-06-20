/*
 * *
 *  * Created by Salman Saleem on 6/21/18 12:43 AM
 *  * Copyright (c) 2018 . All rights reserved.
 *  * Last modified 6/21/18 12:34 AM
 *
 */

package com.tcv.vassistchat.models.auth;

/**
 * Created by Salman Saleem on 01/11/2015.
 *
 *
 *
 */
public class JoinModelResponse {
    private boolean success;
    private boolean smsVerification;
    private boolean hasBackup;
    private boolean hasProfile;
    private String message;
    private String mobile;
    private String code;
    private int userID;
    private String token;
    private String backup_hash;


    public String getBackup_hash() {
        return backup_hash;
    }

    public void setBackup_hash(String backup_hash) {
        this.backup_hash = backup_hash;
    }

    public boolean isSmsVerification() {
        return smsVerification;
    }

    public void setSmsVerification(boolean smsVerification) {
        this.smsVerification = smsVerification;
    }

    public boolean isHasProfile() {
        return hasProfile;
    }

    public void setHasProfile(boolean hasProfile) {
        this.hasProfile = hasProfile;
    }

    public boolean isHasBackup() {
        return hasBackup;
    }

    public void setHasBackup(boolean hasBackup) {
        this.hasBackup = hasBackup;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

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
}
