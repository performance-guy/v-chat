/*
 * *
 *  * Created by Salman Saleem on 6/21/18 12:43 AM
 *  * Copyright (c) 2018 . All rights reserved.
 *  * Last modified 6/21/18 12:32 AM
 *
 */

package com.tcv.vassistchat.presenters.calls;

import com.tcv.vassistchat.R;
import com.tcv.vassistchat.activities.call.CallDetailsActivity;
import com.tcv.vassistchat.activities.call.IncomingCallActivity;
import com.tcv.vassistchat.animations.AnimationsUtil;
import com.tcv.vassistchat.api.APIService;
import com.tcv.vassistchat.api.apiServices.UsersService;
import com.tcv.vassistchat.app.AppConstants;
import com.tcv.vassistchat.app.WhatsCloneApplication;
import com.tcv.vassistchat.fragments.home.CallsFragment;
import com.tcv.vassistchat.helpers.AppHelper;
import com.tcv.vassistchat.interfaces.Presenter;
import com.tcv.vassistchat.models.calls.CallsInfoModel;
import com.tcv.vassistchat.models.calls.CallsModel;
import com.tcv.vassistchat.models.users.Pusher;

import org.greenrobot.eventbus.EventBus;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Salman Saleem on 12/3/16.
 *
 *
 * @Author : https://twitter.com/Ben__Cherif
 * @Skype :
 */

public class CallsPresenter implements Presenter {

    private CallsFragment callsFragment;
    private CallDetailsActivity callDetailsActivity;
    private IncomingCallActivity incomingCallActivity;
    private Realm realm;
    private UsersService mUsersContacts;
    private int userID;
    private int callID;


    public Realm getRealm() {
        return realm;
    }

    public CallsPresenter(CallsFragment callsFragment) {
        this.callsFragment = callsFragment;
        this.realm = WhatsCloneApplication.getRealmDatabaseInstance();

    }

    public CallsPresenter(IncomingCallActivity incomingCallActivity, int userID) {
        this.incomingCallActivity = incomingCallActivity;
        this.userID = userID;
        this.realm = WhatsCloneApplication.getRealmDatabaseInstance();
    }

    public CallsPresenter(CallDetailsActivity callDetailsActivity) {
        this.callDetailsActivity = callDetailsActivity;
        this.realm = WhatsCloneApplication.getRealmDatabaseInstance();
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onCreate() {
        if (incomingCallActivity != null) {
            APIService mApiService = APIService.with(incomingCallActivity);
            mUsersContacts = new UsersService(realm, incomingCallActivity, mApiService);
            getCallerInfo(userID);
        } else if (callDetailsActivity != null) {
            APIService mApiService = APIService.with(callDetailsActivity);
            mUsersContacts = new UsersService(realm, callDetailsActivity, mApiService);
            callID = callDetailsActivity.getIntent().getIntExtra("callID", 0);
            userID = callDetailsActivity.getIntent().getIntExtra("userID", 0);


            getCallerDetailsInfo(userID);
            getCallDetails(callID);
            getCallsDetailsList(callID);
        } else {
            if (!EventBus.getDefault().isRegistered(callsFragment))
                EventBus.getDefault().register(callsFragment);
            APIService mApiService = APIService.with(callsFragment.getActivity());
            mUsersContacts = new UsersService(realm, callsFragment.getActivity(), mApiService);
            getCallsList();
        }
    }

    private void getCallerDetailsInfo(int userID) {

        mUsersContacts.getContactInfo(userID).subscribe(contactsModel -> {
            callDetailsActivity.showUserInfo(contactsModel);
        }, throwable -> {
            AppHelper.LogCat(throwable.getMessage());
        });

    }

    private void getCallDetails(int callID) {
        mUsersContacts.getCallDetails(callID).subscribe(callsModel -> {
            callDetailsActivity.showCallInfo(callsModel);
        }, AppHelper::LogCat);
    }

    private void getCallsDetailsList(int callID) {

        try {
            mUsersContacts.getAllCallsDetails(callID).subscribe(callsInfoModels -> {
                callDetailsActivity.UpdateCallsDetailsList(callsInfoModels);
            }, AppHelper::LogCat);

        } catch (Exception e) {
            AppHelper.LogCat("calls presenter " + e.getMessage());
        }
    }

    private void getCallsList() {

        callsFragment.onShowLoading();
        try {
            mUsersContacts.getAllCalls().subscribe(callsModels -> {
                callsFragment.UpdateCalls(callsModels);
                callsFragment.onHideLoading();
            }, callsFragment::onErrorLoading, callsFragment::onHideLoading);

        } catch (Exception e) {
            AppHelper.LogCat("calls presenter " + e.getMessage());
        }
    }

    private void getCallerInfo(int userID) {
        mUsersContacts.getContactInfo(userID).subscribe(contactsModel -> {
            incomingCallActivity.showUserInfo(contactsModel);
        }, throwable -> {
            AppHelper.LogCat(throwable.getMessage());
        });
    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onDestroy() {
        if (callsFragment != null)
            EventBus.getDefault().unregister(callsFragment);
        realm.close();
    }

    public void removeCall() {
        Realm realm = WhatsCloneApplication.getRealmDatabaseInstance();
        AppHelper.showDialog(callDetailsActivity, callDetailsActivity.getString(R.string.delete_call_dialog));
        realm.executeTransactionAsync(realm1 -> {
            CallsModel callsModel = realm1.where(CallsModel.class).equalTo("id", callID).findFirst();
            RealmResults<CallsInfoModel> callsInfoModel = realm1.where(CallsInfoModel.class).equalTo("callId", callsModel.getId()).findAll();
            callsInfoModel.deleteAllFromRealm();
            callsModel.deleteFromRealm();
        }, () -> {
            AppHelper.hideDialog();
            EventBus.getDefault().post(new Pusher(AppConstants.EVENT_BUS_DELETE_CALL_ITEM, callID));
            callDetailsActivity.finish();
            AnimationsUtil.setSlideOutAnimation(callDetailsActivity);
        }, error -> {
            AppHelper.LogCat(error.getMessage());
            AppHelper.hideDialog();
        });

    }

    @Override
    public void onLoadMore() {

    }

    @Override
    public void onRefresh() {
        getCallsList();
    }

    @Override
    public void onStop() {

    }
}
