/*
 * *
 *  * Created by Salman Saleem on 6/21/18 12:43 AM
 *  * Copyright (c) 2018 . All rights reserved.
 *  * Last modified 6/21/18 12:32 AM
 *
 */

package com.tcv.vassistchat.activities.images;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.yalantis.ucrop.UCrop;

/**
 * Created by Salman Saleem on 1/11/17.
 *
 *
 * @Author : https://twitter.com/Ben__Cherif
 * @Skype :
 */

public class PickerBuilder {
    public static final int SELECT_FROM_GALLERY = 0;
    public static final int SELECT_FROM_CAMERA = 1;
    private Activity activity;
    private onPermissionRefusedListener permissionRefusedListener;
    protected onImageReceivedListener imageReceivedListener;
    private PickerManager pickerManager;
    public PickerBuilder(Activity activity, int type)
    {
        this.activity = activity;
        pickerManager = (type == PickerBuilder.SELECT_FROM_GALLERY) ? new ImagePickerManager(activity) : new CameraPickerManager(activity);

    }

    public interface onPermissionRefusedListener {
        void onPermissionRefused();
    }

    public interface onImageReceivedListener
    {
        void onImageReceived(Uri imageUri);
    }


    public void start()
    {
        Intent intent = new Intent(activity, TempActivity.class);
        activity.startActivity(intent);

        GlobalHolder.getInstance().setPickerManager(pickerManager);

    }

    public PickerBuilder setOnImageReceivedListener(PickerBuilder.onImageReceivedListener listener) {
        pickerManager.setOnImageReceivedListener(listener);
        return this;
    }

    public PickerBuilder setOnPermissionRefusedListener(PickerBuilder.onPermissionRefusedListener listener) {
        pickerManager.setOnPermissionRefusedListener(listener);
        return this;
    }

    public PickerBuilder setCropScreenColor(int cropScreenColor) {
        pickerManager.setCropActivityColor(cropScreenColor);
        return this;
    }

    public PickerBuilder setImageName(String imageName) {
        pickerManager.setImageName(imageName);
        return this;
    }

    public PickerBuilder withTimeStamp(boolean withTimeStamp) {
        pickerManager.withTimeStamp(withTimeStamp);
        return this;
    }

    public PickerBuilder setImageFolderName(String folderName) {
        pickerManager.setImageFolderName(folderName);
        return this;
    }

    public PickerBuilder setCustomizedUcrop(UCrop ucrop) {
        pickerManager.setCustomizedUcrop(ucrop);
        return this;
    }

}