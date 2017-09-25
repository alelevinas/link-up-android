package com.fiuba.tdp.linkup.services;

import com.fiuba.tdp.linkup.domain.LinkUpMatch;
import com.fiuba.tdp.linkup.domain.LinkUpUser;
import com.fiuba.tdp.linkup.domain.ServerResponse;
import com.fiuba.tdp.linkup.domain.UserAround;
import com.fiuba.tdp.linkup.domain.UserPreferences;
import com.fiuba.tdp.linkup.domain.facebook.FacebookUserItem;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserAPIService {

    @GET("/api/linkup/users/{userId}")
    Call<ServerResponse<LinkUpUser>> getUser(@Path("userId") String userId);

    @PUT("/api/linkup/users/{userId}")
    Call<ServerResponse<String>> updateUser(@Path("userId") String userId, @Body LinkUpUser user);

    @POST("/api/linkup/users")
    Call<ServerResponse<LinkUpUser>> postUser(@Body FacebookUserItem body);


    @GET("/api/linkup/users/{userId}/around")
    Call<ServerResponse<ArrayList<UserAround>>> getUsersCompatible(@Path("userId") String userId);

    @POST("/api/linkup/users/{userId}/preferences")
    Call<ServerResponse> postPreferences(@Path("userId") String userId, @Body HashMap<String, String> parameters);

    @GET("/api/linkup/users/{userId}/preferences")
    Call<ServerResponse<UserPreferences>> getUserPreferences(@Path("userId") String userId);

    @PUT("/api/linkup/users/{userId}/location")
    Call<ServerResponse> putLocation(@Path("userId") String userId, @Body HashMap<String, Double> parameters);

    @POST("/api/linkup/users/{myUserId}/likes")
    Call<ServerResponse<String>> postLikeToUser(@Path("myUserId") String myUserId, @Body HashMap<String, String> info);

    @DELETE("/api/linkup/users/{myUserId}/likes/{otherUserId}")
    Call<ServerResponse<String>> deleteLikeToUser(@Path("myUserId") String myUserId, @Path("otherUserId") String otherUserId);

    @GET("/api/linkup/users/{myUserId}/links")
    Call<ServerResponse<LinkUpMatch[]>> getMatchesWithoutChat(@Path("myUserId") String myUserId);
}
