/*
 * *
 *  * Created by Salman Saleem on 6/21/18 12:43 AM
 *  * Copyright (c) 2018 . All rights reserved.
 *  * Last modified 6/21/18 12:34 AM
 *
 */

package com.tcv.vassistchat.interfaces;

/**
 * Created by Salman Saleem on 7/28/16.
 *
 *
 *
 */

public interface AudioCallbacks {
    void onUpdate(int percentage);

    void onPause();

    void onStop();

}
