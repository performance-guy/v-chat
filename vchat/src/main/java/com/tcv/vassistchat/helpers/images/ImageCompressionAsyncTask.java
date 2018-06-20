/*
 * *
 *  * Created by Salman Saleem on 6/21/18 12:43 AM
 *  * Copyright (c) 2018 . All rights reserved.
 *  * Last modified 6/21/18 12:34 AM
 *
 */

package com.tcv.vassistchat.helpers.images;

import android.os.AsyncTask;

/**
 * Created by Salman Saleem on 6/13/16.
 *
 *
 *
 */

public abstract class ImageCompressionAsyncTask extends AsyncTask<String, Void, byte[]> {
    @Override
    protected byte[] doInBackground(String... strings) {
        if (strings.length == 0 || strings[0] == null)
            return null;
        return ImageUtils.compressImage(strings[0]);
    }

    protected abstract void onPostExecute(byte[] imageBytes);

}
