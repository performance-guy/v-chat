/*
 * *
 *  * Created by Salman Saleem on 6/21/18 12:43 AM
 *  * Copyright (c) 2018 . All rights reserved.
 *  * Last modified 6/21/18 12:34 AM
 *
 */

package com.tcv.vassistchat.models.groups;

import io.realm.RealmList;

/**
 * Created by Salman Saleem on 01/11/2015.
 *
 *
 *
 */
public class GroupResponse {
    private boolean success;
    private String message;
    private int groupID;
    private String groupImage;
    private RealmList<MembersGroupModel> membersGroupModels;

    public GroupResponse() {

    }

    public RealmList<MembersGroupModel> getMembersGroupModels() {
        return membersGroupModels;
    }

    public void setMembersGroupModels(RealmList<MembersGroupModel> membersGroupModels) {
        this.membersGroupModels = membersGroupModels;
    }


    public String getGroupImage() {
        return groupImage;
    }

    public void setGroupImage(String groupImage) {
        this.groupImage = groupImage;
    }

    public int getGroupID() {
        return groupID;
    }

    public void setGroupID(int groupID) {
        this.groupID = groupID;
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
