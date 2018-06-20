/*
 * *
 *  * Created by Salman Saleem on 6/21/18 12:43 AM
 *  * Copyright (c) 2018 . All rights reserved.
 *  * Last modified 6/21/18 12:34 AM
 *
 */

package com.tcv.vassistchat.api;

import com.tcv.vassistchat.app.EndPoints;
import com.tcv.vassistchat.models.BackupModel;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by Salman Saleem on 6/13/16.
 *
 *
 *
 */

public interface FilesDownloadService {

    /**
     * method to download a small file size
     *
     * @param fileName this is  parameter for  downloadSmallFileSizeUrlSync method
     * @return this is return value
     */
    @GET
    Call<ResponseBody> downloadSmallFileSizeUrlSync(@Url String fileName);

    /**
     * method to download a large file size
     *
     * @param fileName this is   parameter for  downloadLargeFileSizeUrlSync method
     * @return this is return value
     */
    @Streaming
    @GET
    Call<ResponseBody> downloadLargeFileSizeUrlSync(@Url String fileName);


}
