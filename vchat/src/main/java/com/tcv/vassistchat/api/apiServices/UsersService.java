/*
 * *
 *  * Created by Salman Saleem on 6/21/18 12:43 AM
 *  * Copyright (c) 2018 . All rights reserved.
 *  * Last modified 6/21/18 12:34 AM
 *
 */

package com.tcv.vassistchat.api.apiServices;

import android.content.Context;

import com.tcv.vassistchat.api.APIContact;
import com.tcv.vassistchat.api.APIService;
import com.tcv.vassistchat.app.EndPoints;
import com.tcv.vassistchat.app.WhatsCloneApplication;
import com.tcv.vassistchat.helpers.PreferenceManager;
import com.tcv.vassistchat.helpers.UtilsPhone;
import com.tcv.vassistchat.models.BackupModel;
import com.tcv.vassistchat.models.NetworkModel;
import com.tcv.vassistchat.models.RegisterIDResponse;
import com.tcv.vassistchat.models.RegisterIdModel;
import com.tcv.vassistchat.models.SettingsResponse;
import com.tcv.vassistchat.models.auth.JoinModelResponse;
import com.tcv.vassistchat.models.calls.CallSaverModel;
import com.tcv.vassistchat.models.calls.CallsInfoModel;
import com.tcv.vassistchat.models.calls.CallsModel;
import com.tcv.vassistchat.models.messages.UpdateMessageModel;
import com.tcv.vassistchat.models.users.contacts.BlockResponse;
import com.tcv.vassistchat.models.users.contacts.ContactsModel;
import com.tcv.vassistchat.models.users.contacts.ProfileResponse;
import com.tcv.vassistchat.models.users.contacts.SyncContacts;
import com.tcv.vassistchat.models.users.contacts.UsersBlockModel;
import com.tcv.vassistchat.models.users.status.EditStatus;
import com.tcv.vassistchat.models.users.status.StatusModel;
import com.tcv.vassistchat.models.users.status.StatusResponse;

import org.reactivestreams.Subscription;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;
import okhttp3.RequestBody;

/**
 * Created by Salman Saleem on 20/02/2016.
 *
 */
public class UsersService {
    private APIContact mApiContact;
    private Context mContext;
    private Realm realm;
    private APIService mApiService;
    private Subscription subscription;

    public UsersService(Realm realm, Context context, APIService mApiService) {
        this.mContext = context;
        this.realm = realm;
        this.mApiService = mApiService;

    }

    public UsersService(Context context, APIService mApiService) {
        this.mContext = context;
        this.mApiService = mApiService;

    }

    /**
     * method to initialize the api contact
     *
     * @return return value
     */
    private APIContact initializeApiContact() {
        if (mApiContact == null) {
            mApiContact = this.mApiService.RootService(APIContact.class, EndPoints.BACKEND_BASE_URL);
        }
        return mApiContact;
    }


    /**
     * method to get all contacts
     *
     * @return return value
     */
    public Observable<RealmResults<ContactsModel>> getAllContacts() {
        RealmResults<ContactsModel> contactsModel = realm.where(ContactsModel.class).notEqualTo("id", PreferenceManager.getID(mContext)).equalTo("Exist", true).findAllSorted("Linked", Sort.DESCENDING, "username", Sort.ASCENDING).sort("Activate", Sort.DESCENDING);
        return Observable.just(contactsModel);
    }

    /**
     * method to get linked contacts
     *
     * @return return value
     */
    public Observable<RealmResults<ContactsModel>> getLinkedContacts() {
        RealmResults<ContactsModel> contactsModel = realm.where(ContactsModel.class).notEqualTo("id", PreferenceManager.getID(mContext)).equalTo("Exist", true).equalTo("Linked", true).equalTo("Activate", true).findAllSorted("username", Sort.ASCENDING);
        return Observable.just(contactsModel);
    }

    public int getLinkedContactsSize() {
        RealmResults<ContactsModel> contactsModel = realm.where(ContactsModel.class).notEqualTo("id", PreferenceManager.getID(mContext)).equalTo("Exist", true).equalTo("Linked", true).equalTo("Activate", true).findAllSorted("username", Sort.ASCENDING);
        return contactsModel.size();
    }

    /**
     * method to get linked contacts
     *
     * @return return value
     */
    public Observable<RealmResults<UsersBlockModel>> getBlockedContacts() {
        RealmResults<UsersBlockModel> contactsModel = realm.where(UsersBlockModel.class).notEqualTo("contactsModel.id", PreferenceManager.getID(mContext)).equalTo("contactsModel.Linked", true).equalTo("contactsModel.Activate", true).findAllSorted("contactsModel.username", Sort.ASCENDING);
        return Observable.just(contactsModel).filter(RealmResults::isLoaded);
    }

    /**
     * method to update(syncing) contacts
     *
     * @param contacts
     * @return return value
     */
    public Observable<List<ContactsModel>> updateContacts(List<ContactsModel> contacts) {

        SyncContacts syncContacts = new SyncContacts();
        syncContacts.setContactsModelList(contacts);
        return initializeApiContact().contacts(syncContacts)
                .subscribeOn(Schedulers.io())
                // Read results in Android Main Thread (UI)
                .observeOn(AndroidSchedulers.mainThread())
                .map(this::copyOrUpdateContacts);


    }

    /**
     * method to get general user information
     *
     * @param userID this is parameter  getContact for method
     * @return return value
     */
    public ContactsModel getContact(int userID) {

        Realm realm = WhatsCloneApplication.getRealmDatabaseInstance();
        ContactsModel contactsModel = realm.where(ContactsModel.class).equalTo("id", userID).findFirst();
        if (!realm.isClosed()) realm.close();
        return contactsModel;
    }

    /**
     * method to get user information from the server
     *
     * @param userID this is parameter for getContactInfo method
     * @return return  value
     */
    public Observable<ContactsModel> getContactInfo(int userID) {

        Observable<ContactsModel> observable = initializeApiContact().contact(userID)
                // Request API data on IO Scheduler
                .subscribeOn(Schedulers.io())
                // Write to Realm on Computation scheduler
                .observeOn(Schedulers.computation())
                .map(this::copyOrUpdateContactInfo)
                .observeOn(AndroidSchedulers.mainThread())
                .map(contactsModel -> getContact(userID));
        // Read any cached results
        ContactsModel cachedWeather = getContact(userID);
        if (cachedWeather != null)
            // Merge with the observable from API
            observable = observable.mergeWith(Observable.just(cachedWeather));
        return observable;
    }

    /**
     * method to get all status
     *
     * @return return value
     */
    public RealmResults<StatusModel> getAllStatus() {
        return realm.where(StatusModel.class).equalTo("userID", PreferenceManager.getID(mContext)).findAllSorted("id", Sort.DESCENDING);
    }

    /**
     * method to get user status from server
     *
     * @return return value
     */
    public Observable<List<StatusModel>> getUserStatus() {
        Observable<List<StatusModel>> observable = initializeApiContact().status()
                // Request API data on IO Scheduler
                .subscribeOn(Schedulers.io())
                // Write to Realm on Computation scheduler
                .observeOn(Schedulers.computation())
                .map(this::copyOrUpdateStatus)
                .observeOn(AndroidSchedulers.mainThread())
                .map(status -> getAllStatus());
        // Read any cached results
        List<StatusModel> cachedWeather = getAllStatus();
        if (cachedWeather != null)
            // Merge with the observable from API
            observable = observable.mergeWith(Observable.just(cachedWeather));
        return observable;
    }

    /**
     * method to delete user status
     *
     * @param status this is parameter for deleteStatus method
     * @return return  value
     */
    public Observable<StatusResponse> deleteStatus(int status) {
        return initializeApiContact().deleteStatus(status)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(statusResponse -> statusResponse);
    }

    /**
     * method to delete all user status
     *
     * @return return value
     */
    public Observable<StatusResponse> deleteAllStatus() {
        return initializeApiContact().deleteAllStatus()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(statusResponse -> statusResponse);
    }

    /**
     * method to update user status
     *
     * @param statusID this is parameter for updateStatus method
     * @return return  value
     */
    public Observable<StatusResponse> updateStatus(int statusID) {
        return initializeApiContact().updateStatus(statusID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(statusResponse -> statusResponse);
    }

    /**
     * method to edit user status
     *
     * @param newStatus this is the first parameter for editStatus method
     * @param statusID  this is the second parameter for editStatus method
     * @return return  value
     */
    public Observable<StatusResponse> editStatus(String newStatus, int statusID) {
        EditStatus editStatus = new EditStatus();
        editStatus.setNewStatus(newStatus);
        editStatus.setStatusID(statusID);
        return initializeApiContact().editStatus(editStatus)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(statusResponse -> statusResponse);
    }

    /**
     * method to edit username
     *
     * @param newName this is parameter for editUsername method
     * @return return  value
     */
    public Observable<StatusResponse> editUsername(String newName) {
        EditStatus editUsername = new EditStatus();
        editUsername.setNewStatus(newName);
        return initializeApiContact().editUsername(editUsername)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(statusResponse -> statusResponse);
    }

    /**
     * method to edit group name
     *
     * @param newName this is the first parameter for editGroupName method
     * @param groupID this is the second parameter for editGroupName method
     * @return return  value
     */
    public Observable<StatusResponse> editGroupName(String newName, int groupID) {
        EditStatus editGroupName = new EditStatus();
        editGroupName.setNewStatus(newName);
        editGroupName.setStatusID(groupID);
        return initializeApiContact().editGroupName(editGroupName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(statusResponse -> statusResponse);
    }


    /**
     * method to get current status fron local
     *
     * @return return value
     */
    public Observable<StatusModel> getCurrentStatusFromLocal() {
        StatusModel statusModels = realm.where(StatusModel.class).equalTo("userID", PreferenceManager.getID(mContext)).equalTo("current", true).findFirst();
        if (statusModels != null)
            return Observable.just(statusModels).filter(statusFromLocal -> statusFromLocal.isLoaded()).switchIfEmpty(Observable.just(new StatusModel()));
        else
            return Observable.just(new StatusModel());
    }

    public Observable<BlockResponse> saveEmittedCall(CallSaverModel callSaverModel) {
        return initializeApiContact().saveEmittedCall(callSaverModel)
                .subscribeOn(Schedulers.io())/*
                .observeOn(AndroidSchedulers.mainThread())*/
                .map(usersResponse -> usersResponse);

    }

    public Observable<BlockResponse> saveReceivedCall(CallSaverModel callSaverModel) {
        return initializeApiContact().saveReceivedCall(callSaverModel)
                .subscribeOn(Schedulers.io())/*
                .observeOn(AndroidSchedulers.mainThread())*/
                .map(usersResponse -> usersResponse);

    }

    public Observable<BlockResponse> saveAcceptedCall(CallSaverModel callSaverModel) {
        return initializeApiContact().saveAcceptedCall(callSaverModel)
                .subscribeOn(Schedulers.io())/*
                .observeOn(AndroidSchedulers.mainThread())*/
                .map(usersResponse -> usersResponse);

    }

    public Observable<BlockResponse> block(int userId) {
        return initializeApiContact().block(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(usersResponse -> usersResponse);

    }

    public Observable<BlockResponse> unbBlock(int userId) {
        return initializeApiContact().unBlock(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(usersResponse -> usersResponse);

    }

    public Observable<BackupModel> userHasBackup(String hasBackup) {
        return initializeApiContact().userHasBackup(hasBackup)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(usersResponse -> usersResponse);

    }

    /**
     * method to delete user status
     *
     * @param phone this is parameter for deleteStatus method
     * @return return  value
     */
    public Observable<JoinModelResponse> deleteAccount(String phone, String country) {
        return initializeApiContact().deleteAccount(phone, country)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(response -> response);
    }

    /**
     * method to delete user status
     *
     * @return return  value
     */
    public Observable<ProfileResponse> uploadImage(RequestBody image) {
        return initializeApiContact().uploadImage(image)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(response -> response);
    }


    /**
     * method to copy or update user status
     *
     * @param statusModels this is parameter for copyOrUpdateStatus method
     * @return return  value
     */
    private List<StatusModel> copyOrUpdateStatus(List<StatusModel> statusModels) {
        Realm realm = WhatsCloneApplication.getRealmDatabaseInstance();
        realm.beginTransaction();
        List<StatusModel> statusModels1 = realm.copyToRealmOrUpdate(statusModels);
        realm.commitTransaction();
        if (!realm.isClosed()) realm.close();
        return statusModels1;
    }

    /**
     * method to copy or update contacts list
     *
     * @param mListContacts this is parameter for copyOrUpdateContacts method
     * @return return  value
     */
    private List<ContactsModel> copyOrUpdateContacts(List<ContactsModel> mListContacts) {
        Realm realm = WhatsCloneApplication.getRealmDatabaseInstance();
        List<ContactsModel> finalList = checkContactList(mListContacts, realm);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
        }

        realm.beginTransaction();
        List<ContactsModel> contactsModels = realm.copyToRealmOrUpdate(finalList);
        realm.commitTransaction();
        if (!realm.isClosed()) realm.close();
        return contactsModels;
    }


    private List<ContactsModel> checkContactList(List<ContactsModel> contactsModelList, Realm realm) {
        if (contactsModelList.size() != 0) {
            realm.executeTransactionAsync(realm1 -> {
                RealmResults<ContactsModel> contactsModels = realm1.where(ContactsModel.class).findAll();
                contactsModels.deleteAllFromRealm();
            });
        }
        return contactsModelList;
    }

    private boolean checkIfContactExist(int id, Realm realm) {
        RealmQuery<ContactsModel> query = realm.where(ContactsModel.class).equalTo("id", id);
        return query.count() != 0;

    }

    /**
     * method to copy or update user information
     *
     * @param contactsModel this is parameter for copyOrUpdateContactInfo method
     * @return return  value
     */
    private ContactsModel copyOrUpdateContactInfo(ContactsModel contactsModel) {
        Realm realm = WhatsCloneApplication.getRealmDatabaseInstance();
        ContactsModel realmContact;
        if (UtilsPhone.checkIfContactExist(mContext, contactsModel.getPhone())) {
            realm.beginTransaction();
            contactsModel.setExist(true);
            realmContact = realm.copyToRealmOrUpdate(contactsModel);
            realm.commitTransaction();
        } else {
            realm.beginTransaction();
            contactsModel.setExist(false);
            realmContact = realm.copyToRealmOrUpdate(contactsModel);
            realm.commitTransaction();

        }
        if (!realm.isClosed()) realm.close();

        return realmContact;
    }


    public Observable<SettingsResponse> getAppSettings() {
        return initializeApiContact().getAppSettings()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(applicationVersion -> applicationVersion);
    }

    /**
     * method to get app privacy & terms
     *
     * @return return  value
     */
    public Observable<StatusResponse> getPrivacyTerms() {
        return initializeApiContact().getPrivacyTerms()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(privacyTerms -> privacyTerms);
    }

    /**
     * *
     * method to get all calls
     *
     * @return return value
     */
    public Observable<RealmResults<CallsModel>> getAllCalls() {
        RealmResults<CallsModel> callsModel = realm.where(CallsModel.class).findAllSorted("date", Sort.DESCENDING);
        return Observable.just(callsModel);
    }

    /**
     * *
     * method to get all calls details
     *
     * @return return value
     */
    public Observable<RealmResults<CallsInfoModel>> getAllCallsDetails(int callID) {
        RealmResults<CallsInfoModel> callsInfoModel = realm.where(CallsInfoModel.class)
                .equalTo("callId", callID)
                .findAllSorted("date", Sort.DESCENDING);
        return Observable.just(callsInfoModel);
    }

    /**
     * method to get general call information
     *
     * @param callID this is parameter  getContact for method
     * @return return value
     */
    public Observable<CallsModel> getCallDetails(int callID) {
        CallsModel callsModel = realm.where(CallsModel.class).equalTo("id", callID).findFirst();
        if (callsModel != null)
            return Observable.just(callsModel).filter(callsModel1 -> callsModel1.isLoaded()).switchIfEmpty(Observable.just(new CallsModel()));
        else
            return Observable.just(new CallsModel());
    }


    public Observable<NetworkModel> checkIfUserSession() {
        return initializeApiContact().checkNetwork()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(networkModel -> networkModel);
    }


    public Observable<RegisterIDResponse> updateRegisteredId(RegisterIdModel registerIdModel) {
        return initializeApiContact().updateRegisteredId(registerIdModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(response -> response);
    }


    public Observable<StatusResponse> sendMessage(UpdateMessageModel updateMessageModel) {
        return initializeApiContact().sendMessage(updateMessageModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(statusResponse -> statusResponse);
    }

    public Observable<StatusResponse> sendGroupMessage(UpdateMessageModel updateMessageModel) {
        return initializeApiContact().sendGroupMessage(updateMessageModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(statusResponse -> statusResponse);
    }

    public Observable<StatusResponse> deleteAccountConfirmation(String code) {
        return initializeApiContact().deleteAccountConfirmation(code)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(statusResponse -> statusResponse);
    }
}
