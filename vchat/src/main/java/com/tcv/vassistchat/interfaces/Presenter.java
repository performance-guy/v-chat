/*
 * *
 *  * Created by Salman Saleem on 6/21/18 12:43 AM
 *  * Copyright (c) 2018 . All rights reserved.
 *  * Last modified 6/21/18 12:38 AM
 *
 */

package com.tcv.vassistchat.interfaces;

/**
 * .
 *
 *
 *
 */

public interface Presenter {
    void onStart();

    void onCreate();

    void onPause();

    void onResume();

    void onDestroy();

    void onLoadMore();

    void onRefresh();

    void onStop();


}
