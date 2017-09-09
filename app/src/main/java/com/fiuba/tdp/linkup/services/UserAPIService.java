package com.fiuba.tdp.linkup.services;

import com.fiuba.tdp.linkup.domain.ServerResponse;
import com.fiuba.tdp.linkup.domain.User;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by alejandro on 9/9/17.
 */

public interface UserAPIService {

    @GET("/users/{userId}")
    Call<ServerResponse<User>> getUser(@Path("userId") String userId);
}
