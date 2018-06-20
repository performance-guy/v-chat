/*
 * *
 *  * Created by Salman Saleem on 6/21/18 12:43 AM
 *  * Copyright (c) 2018 . All rights reserved.
 *  * Last modified 6/21/18 12:33 AM
 *
 */

package com.tcv.vassistchat.activities.images;

import android.app.Activity;
import android.content.Intent;
import android.provider.MediaStore;

/**
 * Created by Salman Saleem on 1/11/17.
 *
 *
 * @Author : https://twitter.com/Ben__Cherif
 * @Skype :
 */

public class CameraPickerManager extends PickerManager {

    public CameraPickerManager(Activity activity) {
        super(activity);
    }

    protected void sendToExternalApp() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        mProcessingPhotoUri = getImageFile();
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mProcessingPhotoUri);
        activity.startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE);
    }
}