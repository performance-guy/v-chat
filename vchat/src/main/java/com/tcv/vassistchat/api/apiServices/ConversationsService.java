/*
 * *
 *  * Created by Salman Saleem on 6/21/18 12:43 AM
 *  * Copyright (c) 2018 . All rights reserved.
 *  * Last modified 6/21/18 12:34 AM
 *
 */

package com.tcv.vassistchat.api.apiServices;

import com.tcv.vassistchat.app.WhatsCloneApplication;
import com.tcv.vassistchat.models.messages.ConversationsModel;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;


/**
 * Created by Salman Saleem on 20/04/2016.
 *
 */
public class ConversationsService {
    private Realm realm;

    public ConversationsService(Realm realm) {
        this.realm = realm;

    }

    /**
     * method to get Conversations list
     *
     * @return return value
     */
    public Observable<RealmResults<ConversationsModel>> getConversations() {
        RealmResults<ConversationsModel> conversationsModels = realm.where(ConversationsModel.class).findAllSorted("LastMessageId", Sort.DESCENDING);
        return Observable.just(conversationsModels).filter(RealmResults::isLoaded);
    }


}
