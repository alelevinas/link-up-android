package com.fiuba.tdp.linkup.services;

import android.util.Log;

import com.fiuba.tdp.linkup.domain.ServerResponse;
import com.fiuba.tdp.linkup.domain.User;
import com.fiuba.tdp.linkup.util.Globals;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by alejandro on 9/9/17.
 */

public class UserService {

    private UserAPIService api;

    public UserService() {
        this.api = getApi();
    }

    private UserAPIService getApi() {
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .build();
        return new Retrofit.Builder()
                .baseUrl(Globals.getServerAddress())
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(UserAPIService.class);
    }

    public void getUser(String userId, final Callback<ServerResponse<User>> callback) {
        api.getUser(userId).enqueue(new Callback<ServerResponse<User>>() {
            @Override
            public void onResponse(Call<ServerResponse<User>> call, Response<ServerResponse<User>> response) {
                User serverResponse = response.body().data;
                if (serverResponse != null) {
                    Log.i("SERVER RESPONSE", serverResponse.toString());
                    callback.onResponse(call, Response.success(response.body()));
                }
            }

            @Override
            public void onFailure(Call<ServerResponse<User>> call, Throwable t) {
                callback.onFailure(call, t);
            }
        });
    }


}
