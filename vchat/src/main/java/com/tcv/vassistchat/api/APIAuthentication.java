/*
 * *
 *  * Created by Salman Saleem on 6/21/18 12:43 AM
 *  * Copyright (c) 2018 . All rights reserved.
 *  * Last modified 6/21/18 12:34 AM
 *
 */

package com.tcv.vassistchat.api;


import com.tcv.vassistchat.app.EndPoints;
import com.tcv.vassistchat.models.auth.JoinModelResponse;
import com.tcv.vassistchat.models.auth.LoginModel;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Salman Saleem on 01/11/2015.
 *
 *
 *
 */
public interface APIAuthentication {
    /**
     * method to join
     *
     * @param loginModel this is parameter for join method
     */

    @POST(EndPoints.JOIN)
    Observable<JoinModelResponse> join(@Body LoginModel loginModel);

    /**
     * method to resend SMS request
     *
     * @param phone this is parameter for resend method
     */

    @FormUrlEncoded
    @POST(EndPoints.RESEND_REQUEST_SMS)
    Observable<JoinModelResponse> resend(@Field("phone") String phone);

    /**
     * method to verify the user code
     *
     * @param code this is parameter for verifyUser method
     * @return this is what method will return
     */
    @FormUrlEncoded
    @POST(EndPoints.VERIFY_USER)
    Observable<JoinModelResponse> verifyUser(@Field("code") String code);


}
