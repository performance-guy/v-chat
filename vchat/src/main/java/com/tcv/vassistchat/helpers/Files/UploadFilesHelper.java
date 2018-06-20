/*
 * *
 *  * Created by Salman Saleem on 6/21/18 12:43 AM
 *  * Copyright (c) 2018 . All rights reserved.
 *  * Last modified 6/21/18 12:34 AM
 *
 */

package com.tcv.vassistchat.helpers.Files;

import android.os.Handler;
import android.os.Looper;

import com.tcv.vassistchat.interfaces.UploadCallbacks;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;

/**
 * Created by Salman Saleem on 7/26/16.
 *
 *
 *
 */

public class UploadFilesHelper extends RequestBody {


    private File mFile;
    private byte[] arrBytes;
    private UploadCallbacks mUploadCallbacks;
    private String mimeType;
    private String mType;

    private static final int DEFAULT_BUFFER_SIZE = 2048;


    public UploadFilesHelper(final File mFile, final UploadCallbacks mUploadCallbacks, String mimeType, byte[] arrBytes, String mType) {
        this.mFile = mFile;
        this.mUploadCallbacks = mUploadCallbacks;
        this.mimeType = mimeType;
        this.mType = mType;
        this.arrBytes = arrBytes;
    }

    @Override
    public MediaType contentType() {
        return MediaType.parse(mimeType);

    }


    @Override
    public long contentLength() throws IOException {
        return mFile.length();
    }


    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        long fileLength;
        FileInputStream fileInputStream;
        ByteArrayInputStream byteArrayInputStream;


        if (mFile != null && arrBytes == null) {
            fileLength = mFile.length();
            fileInputStream = new FileInputStream(mFile);
            byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
            long uploaded = 0;

            try {
                int read;
                Handler handler = new Handler(Looper.getMainLooper());
                while ((read = fileInputStream.read(buffer)) != -1) {

                    // update progress on UI thread
                    handler.post(new Updater(uploaded, fileLength));

                    uploaded += read;
                    sink.write(buffer, 0, read);
                }
            } finally {
                fileInputStream.close();
            }
        } else {
            byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
            fileLength = arrBytes.length;
            byteArrayInputStream = new ByteArrayInputStream(arrBytes);
            long uploaded = 0;

            try {
                int read;
                Handler handler = new Handler(Looper.getMainLooper());
                while ((read = byteArrayInputStream.read(buffer)) != -1) {

                    // update progress on UI thread
                    handler.post(new Updater(uploaded, fileLength));

                    uploaded += read;
                    sink.write(buffer, 0, read);
                }
            } finally {
                byteArrayInputStream.close();
            }
        }


    }

    private class Updater implements Runnable {
        private long mUploaded;
        private long mTotal;

        public Updater(long uploaded, long total) {
            mUploaded = uploaded;
            mTotal = total;
        }

        @Override
        public void run() {
            mUploadCallbacks.onUpdate((int) (100 * mUploaded / mTotal), mType);
        }
    }

}
