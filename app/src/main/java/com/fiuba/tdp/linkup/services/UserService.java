package com.fiuba.tdp.linkup.services;

import android.util.Log;

import com.fiuba.tdp.linkup.domain.FacebookUserItem;
import com.fiuba.tdp.linkup.domain.LinkUpUser;
import com.fiuba.tdp.linkup.domain.ServerResponse;
import com.fiuba.tdp.linkup.util.Globals;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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

    public void getUser(String userId, final Callback<ServerResponse<LinkUpUser>> callback) {
        api.getUser(userId).enqueue(new Callback<ServerResponse<LinkUpUser>>() {
            @Override
            public void onResponse(Call<ServerResponse<LinkUpUser>> call, Response<ServerResponse<LinkUpUser>> response) {
                Log.i("SERVER RESPONSE", response.toString());
                callback.onResponse(call, response);
            }

            @Override
            public void onFailure(Call<ServerResponse<LinkUpUser>> call, Throwable t) {
                Log.e("USER SERVICE GET USER", call.toString());
                callback.onFailure(call, t);
            }
        });
    }

    public void postActualFacebookUser(final Callback<ServerResponse<LinkUpUser>> callback) {
        final String LOG_TAG = "POST USER";

        new FacebookService().getUserData(new Callback<FacebookUserItem>() {
            @Override
            public void onResponse(Call<FacebookUserItem> call, Response<FacebookUserItem> response) {
                FacebookUserItem body = response.body();

                api.postUser(body).enqueue(new Callback<ServerResponse<LinkUpUser>>() {
                    @Override
                    public void onResponse(Call<ServerResponse<LinkUpUser>> call, Response<ServerResponse<LinkUpUser>> response) {
                        Log.d(LOG_TAG, "message = " + response.message());
                        if (response.isSuccessful()) {
                            Log.d(LOG_TAG, "-----isSuccess----");
                            callback.onResponse(call, response);
                        } else {
                            Log.d(LOG_TAG, "-----isFalse-----");
                        }
                    }

                    @Override
                    public void onFailure(Call<ServerResponse<LinkUpUser>> call, Throwable t) {
                        Log.d(LOG_TAG, "----onFailure------");
                        Log.e(LOG_TAG, t.getMessage());
                        Log.d(LOG_TAG, "----onFailure------");
                        callback.onFailure(call, t);
                    }
                });
            }

            @Override
            public void onFailure(Call<FacebookUserItem> call, Throwable t) {
                Log.e("GET USER FACEBOOK", "COULDN'T GET USER FROM FACEBOOK");
                callback.onFailure(null, t);
            }
        });
    }

    public void postPreferences(String userId, String gender, String distance, String minAge, String maxAge,
                                String mode, String searchMode) {
        final String LOG_TAG = "POST PREFERENCES";

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("gender", gender);
        parameters.put("distance", distance);
        parameters.put("minAge", minAge);
        parameters.put("maxAge", maxAge);
        parameters.put("mode", mode);
        parameters.put("searchMode", searchMode);

        Log.d(LOG_TAG, "Valores: " + gender + ", " + distance + ", " + minAge + ", " + maxAge + ", " + mode + ", " + searchMode);

        api.postPreferences(userId, parameters).enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                Log.d(LOG_TAG, "message = " + response.message());
                if (response.isSuccessful()) {
                    Log.d(LOG_TAG, "-----isSuccess----");
                } else {
                    Log.d(LOG_TAG, "-----isFalse-----");
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Log.d(LOG_TAG, "----onFailure------");
                Log.e(LOG_TAG, t.getMessage());
                Log.d(LOG_TAG, "----onFailure------");
            }
        });
    }
}
