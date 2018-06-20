/*
 * *
 *  * Created by Salman Saleem on 6/21/18 12:43 AM
 *  * Copyright (c) 2018 . All rights reserved.
 *  * Last modified 6/21/18 12:34 AM
 *
 */

package com.tcv.vassistchat.helpers.Files;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;

import com.tcv.vassistchat.helpers.AppHelper;
import com.tcv.vassistchat.interfaces.DownloadCallbacks;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import io.realm.internal.IOException;
import okhttp3.ResponseBody;

import static com.tcv.vassistchat.helpers.Files.FilesManager.getFileAudio;
import static com.tcv.vassistchat.helpers.Files.FilesManager.getFileDocument;
import static com.tcv.vassistchat.helpers.Files.FilesManager.getFileImage;
import static com.tcv.vassistchat.helpers.Files.FilesManager.getFileVideo;
import static com.tcv.vassistchat.helpers.Files.FilesManager.isFileAudioExists;
import static com.tcv.vassistchat.helpers.Files.FilesManager.isFileDocumentsExists;
import static com.tcv.vassistchat.helpers.Files.FilesManager.isFileImagesExists;
import static com.tcv.vassistchat.helpers.Files.FilesManager.isFileVideosExists;

/**
 * Created by Salman Saleem on 7/28/16.
 *
 *
 *
 */

public class DownloadFilesHelper {

    private DownloadCallbacks mDownloadCallbacks;
    private static final int DEFAULT_BUFFER_SIZE = 4096;
    private String type;
    private String Identifier;
    private ResponseBody mFile;

    public DownloadFilesHelper(final ResponseBody mFile, String Identifier, String type, final DownloadCallbacks mDownloadCallbacks) {
        this.mFile = mFile;
        this.Identifier = Identifier;
        this.type = type;
        this.mDownloadCallbacks = mDownloadCallbacks;

    }


    public boolean writeResponseBodyToDisk(Activity mActivity) {
        try {
            try {
                if (isFileImagesExists(mActivity,Identifier)) {
                    getFileImage(mActivity,Identifier).delete();
                    return false;
                } else if (isFileVideosExists(mActivity,Identifier)) {
                    getFileVideo(mActivity,Identifier).delete();
                    return false;
                } else if (isFileAudioExists(mActivity,Identifier)) {
                    getFileAudio(mActivity,Identifier).delete();
                    return false;
                } else if (isFileDocumentsExists(mActivity,Identifier)) {
                    getFileDocument(mActivity,Identifier).delete();
                    return false;
                }
            } catch (Exception ignored) {
            }

            File downloadedFile = null;
            switch (type) {
                case "image":
                    downloadedFile = new File(FilesManager.getFileImagesPath(mActivity,Identifier));
                    break;
                case "video":
                    downloadedFile = new File(FilesManager.getFileVideoPath(mActivity,Identifier));
                    break;
                case "audio":
                    downloadedFile = new File(FilesManager.getFileAudioPath(mActivity,Identifier));
                    break;
                case "document":
                    downloadedFile = new File(FilesManager.getFileDocumentsPath(mActivity,Identifier));
                    break;
            }

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[DEFAULT_BUFFER_SIZE];

                long fileSize = mFile.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = mFile.byteStream();
                try {
                    outputStream = new FileOutputStream(downloadedFile);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                Handler handler = new Handler(Looper.getMainLooper());
                while (true) {
                    int read = 0;
                    try {
                        read = inputStream.read(fileReader);
                    } catch (java.io.IOException e) {
                        e.printStackTrace();
                    }

                    if (read == -1) {
                        break;
                    }

                    try {
                        outputStream.write(fileReader, 0, read);
                    } catch (java.io.IOException e) {
                        e.printStackTrace();
                    }

                    fileSizeDownloaded += read;
                    // update progress on UI thread
                    handler.post(new Updater(fileSizeDownloaded, fileSize));
                    AppHelper.LogCat("file download: " + fileSizeDownloaded + " of " + fileSize);
                }

                try {
                    outputStream.flush();
                } catch (java.io.IOException e) {
                    e.printStackTrace();
                }

                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (java.io.IOException e) {
                        e.printStackTrace();
                    }
                }

                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (java.io.IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            return false;
        }
    }

    private class Updater implements Runnable {
        private long mUploaded;
        private long mTotal;

        Updater(long uploaded, long total) {
            mUploaded = uploaded;
            mTotal = total;
        }


        @Override
        public void run() {
            mDownloadCallbacks.onUpdate((int) (100 * mUploaded / mTotal), type);
        }
    }

}
