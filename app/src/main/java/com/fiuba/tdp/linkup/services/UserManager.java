package com.fiuba.tdp.linkup.services;

import android.util.Log;

import com.fiuba.tdp.linkup.domain.LinkUpPicture;
import com.fiuba.tdp.linkup.domain.LinkUpUser;
import com.fiuba.tdp.linkup.domain.ServerResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by alejandro on 9/10/17.
 */

public class UserManager {
    public static final int PICTURE_COUNT = 5;
    private static final UserManager ourInstance = new UserManager();
    private LinkUpUser myUser;

    private UserManager() {
    }

    public static UserManager getInstance() {
        return ourInstance;
    }

    public final LinkUpUser getMyUser() {
        return myUser;
    }

    public void setMyUser(LinkUpUser myUser) {
        this.myUser = myUser;
    }

    public void updatePicture(int i, String url) {
        LinkUpPicture[] arrayPictures = myUser.getPictures();
        if (arrayPictures.length < PICTURE_COUNT) {
            List<LinkUpPicture> pictures = new ArrayList<>(PICTURE_COUNT);
            Collections.addAll(pictures, arrayPictures);
            for (int p = pictures.size(); p < PICTURE_COUNT; p++)
                pictures.add(new LinkUpPicture(""));
            arrayPictures = pictures.toArray(new LinkUpPicture[PICTURE_COUNT]);
        }

        arrayPictures[i].setUrl(url);
        myUser.setPictures(arrayPictures);

        new UserService().updateUser(myUser.getId(), myUser, new Callback<ServerResponse<String>>() {
            @Override
            public void onResponse(Call<ServerResponse<String>> call, Response<ServerResponse<String>> response) {
                if (response.isSuccessful()) {
                    Log.d("UPDATE PICTURES", "-----isSuccess----");
                    return;
                }
                Log.d("UPDATE PICTURES", "-----MAL AHI----");
            }

            @Override
            public void onFailure(Call<ServerResponse<String>> call, Throwable t) {
                Log.d("UPDATE PICTURES", "-----MAL AHI----");
            }
        });

    }
}
