/*
 * *
 *  * Created by Salman Saleem on 6/21/18 12:43 AM
 *  * Copyright (c) 2018 . All rights reserved.
 *  * Last modified 6/21/18 12:34 AM
 *
 */

package com.tcv.vassistchat.interfaces;

/**
 * Created by Salman Saleem on 9/20/16.
 *
 *
 *
 */


public interface NetworkListener {
    void onNetworkConnectionChanged(boolean isConnecting, boolean isConnected);
}
