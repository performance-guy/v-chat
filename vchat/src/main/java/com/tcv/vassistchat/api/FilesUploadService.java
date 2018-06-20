/*
 * *
 *  * Created by Salman Saleem on 6/21/18 12:43 AM
 *  * Copyright (c) 2018 . All rights reserved.
 *  * Last modified 6/21/18 12:34 AM
 *
 */

package com.tcv.vassistchat.api;

import com.tcv.vassistchat.app.EndPoints;
import com.tcv.vassistchat.helpers.Files.UploadFilesHelper;
import com.tcv.vassistchat.models.BackupModel;
import com.tcv.vassistchat.models.messages.FilesResponse;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by Salman Saleem on 7/26/16.
 *
 *
 *
 */

public interface FilesUploadService {

    /**
     * method to upload images
     *
     * @param image this is  the second parameter for  uploadMessageImage method
     * @return this is return value
     */
    @Multipart
    @POST(EndPoints.UPLOAD_MESSAGES_IMAGE)
    Call<FilesResponse> uploadMessageImage(@Part("image\"; filename=\"messageImage.jpg\" ") UploadFilesHelper image);

    /**
     * method to upload videos
     *
     * @param video     this is  the first parameter for  uploadMessageVideo method
     * @param thumbnail this is  the second parameter for  uploadMessageVideo method
     * @return this is return value
     */
    @Multipart
    @POST(EndPoints.UPLOAD_MESSAGES_VIDEO)
    Call<FilesResponse> uploadMessageVideo(@Part("video\"; filename=\"messageVideo.mp4\" ") UploadFilesHelper video,
                                           @Part("thumbnail\"; filename=\"messageVideoThumbnail.jpg\" ") RequestBody thumbnail);

    /**
     * method to upload audio
     *
     * @param audio this is   parameter for  uploadMessageAudio method
     * @return this is return value
     */
    @Multipart
    @POST(EndPoints.UPLOAD_MESSAGES_AUDIO)
    Call<FilesResponse> uploadMessageAudio(@Part("audio\"; filename=\"messageAudio.mp3\" ") UploadFilesHelper audio);

    /**
     * method to upload document
     *
     * @param document this is  parameter for  uploadMessageDocument method
     * @return this is return value
     */
    @Multipart
    @POST(EndPoints.UPLOAD_MESSAGES_DOCUMENT)
    Call<FilesResponse> uploadMessageDocument(@Part("document\"; filename=\"messageDocument.pdf\" ") UploadFilesHelper document);



}
