package com.project.aaronkoti.splitus.net;

import com.project.aaronkoti.splitus.beans.Notification;
import com.project.aaronkoti.splitus.beans.ResponseFCM;
import com.project.aaronkoti.splitus.beans.SplitUsNotification;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HEAD;
import retrofit2.http.HeaderMap;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by User on 10/27/2016.
 */

public interface FirebaseMessageInterface {

    @POST("send")
    Call<ResponseFCM> sendNotification(@Body SplitUsNotification notification, @HeaderMap Map<String, String> header);

}
