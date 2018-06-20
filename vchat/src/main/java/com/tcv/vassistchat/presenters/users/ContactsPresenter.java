/*
 * *
 *  * Created by Salman Saleem on 6/21/18 12:43 AM
 *  * Copyright (c) 2018 . All rights reserved.
 *  * Last modified 6/21/18 12:34 AM
 *
 */

package com.tcv.vassistchat.presenters.users;


import android.Manifest;
import android.os.Handler;
import android.support.v7.app.AlertDialog;

import com.tcv.vassistchat.R;
import com.tcv.vassistchat.activities.NewConversationContactsActivity;
import com.tcv.vassistchat.activities.PrivacyActivity;
import com.tcv.vassistchat.api.APIService;
import com.tcv.vassistchat.api.apiServices.UsersService;
import com.tcv.vassistchat.app.WhatsCloneApplication;
import com.tcv.vassistchat.helpers.AppHelper;
import com.tcv.vassistchat.helpers.PermissionHandler;
import com.tcv.vassistchat.helpers.PreferenceManager;
import com.tcv.vassistchat.helpers.UtilsPhone;
import com.tcv.vassistchat.interfaces.Presenter;
import com.tcv.vassistchat.models.users.contacts.ContactsModel;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;

/**
 * Created by Salman Saleem on 20/02/2016.
 *
 */
public class ContactsPresenter implements Presenter {
    private NewConversationContactsActivity newConversationContactsActivity;
    private PrivacyActivity privacyActivity;
    private Realm realm;
    private UsersService mUsersContacts;


    public ContactsPresenter(NewConversationContactsActivity newConversationContactsActivity) {
        this.newConversationContactsActivity = newConversationContactsActivity;
        this.realm = WhatsCloneApplication.getRealmDatabaseInstance();

    }


    public ContactsPresenter(PrivacyActivity privacyActivity) {
        this.privacyActivity = privacyActivity;
        this.realm = WhatsCloneApplication.getRealmDatabaseInstance();
    }


    @Override
    public void onStart() {
    }

    @Override
    public void onCreate() {
        if (privacyActivity != null) {
            APIService mApiService = APIService.with(privacyActivity);
            mUsersContacts = new UsersService(realm, privacyActivity, mApiService);
            getPrivacyTerms();
        } else if (newConversationContactsActivity != null) {
            APIService mApiService = APIService.with(newConversationContactsActivity);
            mUsersContacts = new UsersService(realm, newConversationContactsActivity, mApiService);
            getContacts();
        }

    }


    public void getContacts() {
        if (newConversationContactsActivity != null) {
            if (mUsersContacts.getLinkedContactsSize() == 0) {
                loadDataFromServer();
            }
            try {
                mUsersContacts.getAllContacts().subscribe(contactsModels -> {
                    newConversationContactsActivity.ShowContacts(contactsModels);
                }, throwable -> {
                    newConversationContactsActivity.onErrorLoading(throwable);
                }, () -> {
                    newConversationContactsActivity.onHideLoading();
                });
                try {
                    PreferenceManager.setContactSize(newConversationContactsActivity, mUsersContacts.getLinkedContactsSize());
                } catch (Exception e) {
                    AppHelper.LogCat(" Exception size contact fragment");
                }
            } catch (Exception e) {
                AppHelper.LogCat("getAllContacts Exception ContactsPresenter ");
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
        if (!realm.isClosed())
            realm.close();
    }

    @Override
    public void onLoadMore() {

    }


    @Override
    public void onRefresh() {
        if (newConversationContactsActivity != null) {
            if (PermissionHandler.checkPermission(newConversationContactsActivity, Manifest.permission.READ_CONTACTS)) {
                AppHelper.LogCat("Read contact data permission already granted.");
                newConversationContactsActivity.onShowLoading();

                Observable.create((ObservableOnSubscribe<List<ContactsModel>>) subscriber -> {
                    try {
                        List<ContactsModel> contactsModels = UtilsPhone.GetPhoneContacts();
                        subscriber.onNext(contactsModels);
                        subscriber.onComplete();
                    } catch (Exception throwable) {
                        subscriber.onError(throwable);
                    }
                }).subscribeOn(Schedulers.computation()).subscribe(contacts -> {
                    mUsersContacts.updateContacts(contacts).subscribe(contactsModelList -> {
                        getContacts();
                        AppHelper.CustomToast(newConversationContactsActivity, newConversationContactsActivity.getString(R.string.success_response_contacts));
                    }, throwable -> {
                        newConversationContactsActivity.onErrorLoading(throwable);
                        AlertDialog.Builder alert = new AlertDialog.Builder(newConversationContactsActivity.getApplicationContext());
                        alert.setMessage(newConversationContactsActivity.getString(R.string.error_response_contacts));
                        alert.setPositiveButton(R.string.ok, (dialog, which) -> {
                        });
                        alert.setCancelable(false);
                        alert.show();
                    }, () -> {
                        newConversationContactsActivity.onHideLoading();
                    });
                }, throwable -> {
                    AppHelper.LogCat(" " + throwable.getMessage());
                });

                mUsersContacts.getContactInfo(PreferenceManager.getID(newConversationContactsActivity)).subscribe(contactsModel -> AppHelper.LogCat("getContactInfo"), AppHelper::LogCat);

            } else {
                AppHelper.LogCat("Please request Read contact data permission.");
                PermissionHandler.requestPermission(newConversationContactsActivity, Manifest.permission.READ_CONTACTS);
            }
        }


    }

    @Override
    public void onStop() {

    }


    private void loadDataFromServer() {

        Observable.create((ObservableOnSubscribe<List<ContactsModel>>) subscriber -> {
            try {
                List<ContactsModel> contactsModels = UtilsPhone.GetPhoneContacts();
                subscriber.onNext(contactsModels);
                subscriber.onComplete();
            } catch (Exception throwable) {
                subscriber.onError(throwable);
            }
        }).subscribeOn(Schedulers.computation()).subscribe(contacts -> {
            mUsersContacts.updateContacts(contacts).subscribe(contactsModelList -> {
                getContacts();
                new Handler().postDelayed(() -> {
                    mUsersContacts.getContactInfo(PreferenceManager.getID(newConversationContactsActivity)).subscribe(contactsModel -> AppHelper.LogCat("info user ContactsPresenter"), throwable -> AppHelper.LogCat("On error ContactsPresenter"));
                }, 2000);
            }, throwable -> {
                newConversationContactsActivity.onErrorLoading(throwable);
            }, () -> {

            });
        }, throwable -> {
            AppHelper.LogCat(" " + throwable.getMessage());
        });

    }


    private void getPrivacyTerms() {
        mUsersContacts.getPrivacyTerms().subscribe(statusResponse -> {
            if (statusResponse.isSuccess()) {
                privacyActivity.showPrivcay(statusResponse.getMessage());
            } else {
                AppHelper.LogCat(" " + statusResponse.getMessage());
            }

        }, throwable -> {
            AppHelper.LogCat(" " + throwable.getMessage());
        });
    }
}