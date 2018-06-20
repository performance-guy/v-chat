/*
 * *
 *  * Created by Salman Saleem on 6/21/18 12:43 AM
 *  * Copyright (c) 2018 . All rights reserved.
 *  * Last modified 6/21/18 12:34 AM
 *
 */

package com.tcv.vassistchat.presenters.users;


import com.tcv.vassistchat.activities.profile.ProfilePreviewActivity;
import com.tcv.vassistchat.api.APIService;
import com.tcv.vassistchat.api.apiServices.GroupsService;
import com.tcv.vassistchat.api.apiServices.UsersService;
import com.tcv.vassistchat.app.AppConstants;
import com.tcv.vassistchat.app.WhatsCloneApplication;
import com.tcv.vassistchat.helpers.AppHelper;
import com.tcv.vassistchat.helpers.PreferenceManager;
import com.tcv.vassistchat.interfaces.Presenter;
import com.tcv.vassistchat.models.messages.ConversationsModel;
import com.tcv.vassistchat.models.users.Pusher;

import org.greenrobot.eventbus.EventBus;

import io.realm.Realm;

/**
 * Created by Salman Saleem on 20/02/2016.
 */
public class ProfilePreviewPresenter implements Presenter {
    private ProfilePreviewActivity profilePreviewActivity;
    private Realm realm;


    public ProfilePreviewPresenter(ProfilePreviewActivity profilePreviewActivity) {
        this.profilePreviewActivity = profilePreviewActivity;
        this.realm = WhatsCloneApplication.getRealmDatabaseInstance();

    }


    @Override
    public void onStart() {

    }

    @Override
    public void
    onCreate() {
        if (profilePreviewActivity != null) {
            APIService mApiService = APIService.with(profilePreviewActivity);

            if (profilePreviewActivity.getIntent().hasExtra("userID")) {
                int userID = profilePreviewActivity.getIntent().getExtras().getInt("userID");
                UsersService mUsersContacts = new UsersService(realm, profilePreviewActivity, mApiService);
                mUsersContacts.getContactInfo(userID).subscribe(contactsModel -> {
                    profilePreviewActivity.ShowContact(contactsModel);
                    int ConversationID = getConversationId(contactsModel.getId(), PreferenceManager.getID(profilePreviewActivity), realm);
                    if (ConversationID != 0) {
                        realm.executeTransaction(realm1 -> {
                            ConversationsModel conversationsModel = realm1.where(ConversationsModel.class).equalTo("id", ConversationID).findFirst();
                            conversationsModel.setRecipientImage(contactsModel.getImage());
                            realm1.copyToRealmOrUpdate(conversationsModel);
                            EventBus.getDefault().post(new Pusher(AppConstants.EVENT_UPDATE_CONVERSATION_OLD_ROW, ConversationID));
                        });
                    }
                }, throwable -> {
                    profilePreviewActivity.onErrorLoading(throwable);
                });

            }

            if (profilePreviewActivity.getIntent().hasExtra("groupID")) {
                GroupsService mGroupsService = new GroupsService(realm, profilePreviewActivity, mApiService);
                int groupID = profilePreviewActivity.getIntent().getExtras().getInt("groupID");


                mGroupsService.getGroupInfo(groupID).subscribe(groupsModel -> {
                    profilePreviewActivity.ShowGroup(groupsModel);
                    int ConversationID = getConversationGroupId(groupsModel.getId(), realm);
                    if (ConversationID != 0) {
                        realm.executeTransaction(realm1 -> {
                            ConversationsModel conversationsModel = realm1.where(ConversationsModel.class).equalTo("id", ConversationID).findFirst();
                            assert conversationsModel != null;
                            conversationsModel.setRecipientImage(groupsModel.getGroupImage());
                            conversationsModel.setRecipientUsername(groupsModel.getGroupName());
                            realm1.copyToRealmOrUpdate(conversationsModel);
                            EventBus.getDefault().post(new Pusher(AppConstants.EVENT_UPDATE_CONVERSATION_OLD_ROW, ConversationID));
                        });
                    }
                }, throwable -> {
                    profilePreviewActivity.onErrorLoading(throwable);
                });

            }
        }
    }


    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onDestroy() {
        realm.close();
    }

    @Override
    public void onLoadMore() {

    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onStop() {

    }

    /**
     * method to get a conversation id
     *
     * @param recipientId this is the first parameter for getConversationId method
     * @param senderId    this is the second parameter for getConversationId method
     * @return conversation id
     */
    private int getConversationId(int recipientId, int senderId, Realm realm) {
        try {
            ConversationsModel conversationsModelNew = realm.where(ConversationsModel.class)
                    .beginGroup()
                    .equalTo("RecipientID", recipientId)
                    .or()
                    .equalTo("RecipientID", senderId)
                    .endGroup().findAll().first();
            return conversationsModelNew.getId();
        } catch (Exception e) {
            AppHelper.LogCat("Conversation id Exception ContactFragment" + e.getMessage());
            return 0;
        }
    }

    private int getConversationGroupId(int GroupID, Realm realm) {
        try {
            ConversationsModel conversationsModel = realm.where(ConversationsModel.class).equalTo("groupID", GroupID).findFirst();
            return conversationsModel.getId();
        } catch (Exception e) {
            AppHelper.LogCat("Conversation id Exception ContactFragment" + e.getMessage());
            return 0;
        }
    }
}