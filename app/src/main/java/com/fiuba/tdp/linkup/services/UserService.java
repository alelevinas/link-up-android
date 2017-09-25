package com.fiuba.tdp.linkup.services;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.fiuba.tdp.linkup.R;
import com.fiuba.tdp.linkup.domain.LinkUpMatch;
import com.fiuba.tdp.linkup.domain.LinkUpUser;
import com.fiuba.tdp.linkup.domain.LocationUser;
import com.fiuba.tdp.linkup.domain.Match;
import com.fiuba.tdp.linkup.domain.ServerResponse;
import com.fiuba.tdp.linkup.domain.UserAround;
import com.fiuba.tdp.linkup.domain.UserPreferences;
import com.fiuba.tdp.linkup.domain.facebook.FacebookUserItem;
import com.fiuba.tdp.linkup.util.Globals;
import com.fiuba.tdp.linkup.util.UserDoesNotHaveFacebookPicture;
import com.fiuba.tdp.linkup.util.UserIsNotOldEnoughException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserService {

    private final Context context;
    Boolean endItNow;
    private UserAPIService api;

    public UserService(Context context) {
        this.context = context;
        if (isNetworkAvailable()) {
            Log.e("NETWOR", "AVAILABLE");
            this.api = getApi();
            endItNow = false;
        } else {
            Log.e("NETWOR", "UUUUUNAVAILABLE");
            endItNow = true;
        }
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
        if (endItNow) {
//            showNoConnectionAlert();
            callback.onFailure(null, null);
            return;
        }
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

    public void postActualFacebookUser(final String name, final String profilePicture, final Callback<ServerResponse<LinkUpUser>> callback) {
        if (endItNow) {
            showNoConnectionAlert();
            callback.onFailure(null, null);
            return;
        }
        final String LOG_TAG = "POST USER";

        new FacebookService(context).getUserData(new Callback<FacebookUserItem>() {
            @Override
            public void onResponse(Call<FacebookUserItem> call, Response<FacebookUserItem> response) {
                FacebookUserItem body = response.body();

                body.setName(name);

                body.setProfilePicture(profilePicture);

                body.setAge(body.getAge());
                if (Integer.parseInt(body.getAge()) < 18) {
                    callback.onFailure(null, new UserIsNotOldEnoughException("User is under 18 years old"));
                    return;
                }

                if (body.getPicture() == null || body.getPicture().getData().getUrl().equals("https://scontent.xx.fbcdn.net/v/t1.0-1/c15.0.50.50/p50x50/1379841_10150004552801901_469209496895221757_n.jpg?oh=75a552f602cfcd0f95dfc5f86743ca25&oe=5A18F933")) {
                    callback.onFailure(null, new UserDoesNotHaveFacebookPicture("User does not have a profile picture"));
                    return;
                }

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

    public void getUsersCompatible(final String userId, final Callback<ServerResponse<ArrayList<UserAround>>> callback) {
        if (endItNow) {
            showNoConnectionAlert();
            callback.onFailure(null, null);
            return;
        }
        final String LOG_TAG = "GET AROUNDS";

        api.getUsersCompatible(userId).enqueue(new Callback<ServerResponse<ArrayList<UserAround>>>() {
            @Override
            public void onResponse(Call<ServerResponse<ArrayList<UserAround>>> call, Response<ServerResponse<ArrayList<UserAround>>> response) {
                Log.d(LOG_TAG, "Id usuario: " + userId);

                try {
                    ArrayList<UserAround> serverResponse = response.body().data;
                    if (serverResponse != null) {
                        Log.i("SERVER RESPONSE", serverResponse.toString());
                        callback.onResponse(call, Response.success(response.body()));
                    }
                } catch (NullPointerException e) {
                    // response.body().data; could throw this exception id data not exists
                }
            }

            @Override
            public void onFailure(Call<ServerResponse<ArrayList<UserAround>>> call, Throwable t) {
                callback.onFailure(call, t);
            }
        });
    }

    public void postPreferences(String userId, String gender, String distance, String minAge, String maxAge,
                                String mode, String searchMode) {
        if (endItNow) {
            showNoConnectionAlert();
            return;
        }
        final String LOG_TAG = "POST PREFERENCES";

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("gender", gender);
        parameters.put("distance", distance);
        parameters.put("minAge", minAge);
        parameters.put("maxAge", maxAge);
        parameters.put("mode", mode);
        parameters.put("searchMode", searchMode);

        Log.d(LOG_TAG, "Valores: " + userId + ", " + gender + ", " + distance + ", " + minAge + ", " + maxAge + ", " + mode + ", " + searchMode);

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


    public void getUserPreferences(String userId, final Callback<ServerResponse<UserPreferences>> callback) {
        if (endItNow) {
            showNoConnectionAlert();
            callback.onFailure(null, null);
            return;
        }
        api.getUserPreferences(userId).enqueue(new Callback<ServerResponse<UserPreferences>>() {
            @Override
            public void onResponse(Call<ServerResponse<UserPreferences>> call, Response<ServerResponse<UserPreferences>> response) {
                if (response.isSuccessful()) {
                    Log.i("SERVER RESPONSE", response.toString());
                    callback.onResponse(call, response);
                }
            }

            @Override
            public void onFailure(Call<ServerResponse<UserPreferences>> call, Throwable t) {
                callback.onFailure(call, t);
            }
        });
    }

    public void putLocation(String userId, LocationUser location) {
        if (endItNow) {
            showNoConnectionAlert();
            return;
        }
        final String LOG_TAG = "POST LOCATION";

        HashMap<String, Double> parameters = new HashMap<>();
        parameters.put("lat", location.getLat());
        parameters.put("lon", location.getLon());

        Log.d(LOG_TAG, "Valores: " + userId + ", " + location.getLat() + ", " + location.getLon());

        api.putLocation(userId, parameters).enqueue(new Callback<ServerResponse>() {
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


    public void updateUser(String userId, LinkUpUser user, final Callback<ServerResponse<String>> callback) {
        if (endItNow) {
            showNoConnectionAlert();
            callback.onFailure(null, null);
            return;
        }
        api.updateUser(userId, user).enqueue(callback);
    }

    public void postLikeToUser(String myUserId, String otherUserId, final Callback<ServerResponse<Match>> callback) {
        if (endItNow) {
            showNoConnectionAlert();
            callback.onFailure(null, null);
            return;
        }

        HashMap<String, String> info = new HashMap<>();
        info.put("userId", otherUserId);
        api.postLikeToUser(myUserId, info).enqueue(callback);
    }

    public void deleteLikeToUser(String myUserId, String otherUserId, final Callback<ServerResponse<String>> callback) {
        if (endItNow) {
            showNoConnectionAlert();
            callback.onFailure(null, null);
            return;
        }
        api.deleteLikeToUser(myUserId, otherUserId).enqueue(callback);
    }

    public void getMatchesWithoutChat(String myUserId, final Callback<ServerResponse<LinkUpMatch[]>> callback) {
        if (endItNow) {
            showNoConnectionAlert();
            callback.onFailure(null, null);
            return;
        }
        api.getMatchesWithoutChat(myUserId).enqueue(callback);
    }

    public boolean isNetworkAvailable() {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    private void showNoConnectionAlert() {
        showAlert("Atención", "No hay conexión. Por favor intenta luego");
    }

    private void showAlert(String title, String message) {
        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage(message)
                .setTitle(title);

        // 3. Add the buttons
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                dialog.dismiss();
            }
        });

        // 4. Get the AlertDialog from create()
        AlertDialog dialog = builder.create();

        dialog.show();

    }
}
