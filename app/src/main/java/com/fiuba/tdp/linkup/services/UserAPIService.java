package com.fiuba.tdp.linkup.services;

import com.fiuba.tdp.linkup.domain.ServerResponse;
import com.fiuba.tdp.linkup.domain.User;
import com.fiuba.tdp.linkup.domain.UserAround;
import com.fiuba.tdp.linkup.domain.UserPreferences;
import com.fiuba.tdp.linkup.domain.UsersAround;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserAPIService {

    @GET("/users/{userId}")
    Call<ServerResponse<User>> getUser(@Path("userId") String userId);

    @GET("/api/linkup/users/{userId}/around")
    Call<ServerResponse<ArrayList<UserAround>>> getUsersCompatible(@Path("userId") String userId);

    @POST("/api/linkup/users/{userId}/preferences")
    Call<ServerResponse> postPreferences(@Path("userId") String userId, @Body HashMap<String, String> parameters);

    @GET("/api/linkup/users/{userId}/preferences")
    Call<ServerResponse<UserPreferences>> getUserPreferences(@Path("userId") String userId);
}
