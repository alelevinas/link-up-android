package com.fiuba.tdp.linkup.services;

import com.fiuba.tdp.linkup.domain.FacebookUserItem;
import com.fiuba.tdp.linkup.domain.LinkUpUser;
import com.fiuba.tdp.linkup.domain.ServerResponse;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserAPIService {

    @GET("/api/linkup/users/{userId}")
    Call<ServerResponse<LinkUpUser>> getUser(@Path("userId") String userId);

    @POST("/api/linkup/users")
    Call<ServerResponse<LinkUpUser>> postUser(@Body FacebookUserItem body);


    @POST("/api/linkup/users/{userId}/preferences")
    Call<ServerResponse> postPreferences(@Path("userId") String userId, @Body HashMap<String, String> parameters);


}
