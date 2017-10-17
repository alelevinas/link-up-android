package com.fiuba.tdp.linkup.services;

import android.content.Context;
import android.util.Log;

import com.fiuba.tdp.linkup.domain.LinkUpBlockedUser;
import com.fiuba.tdp.linkup.domain.LinkUpPicture;
import com.fiuba.tdp.linkup.domain.LinkUpUser;
import com.fiuba.tdp.linkup.domain.ServerResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserManager {
    public static final int PICTURE_COUNT = 5;
    private static final UserManager ourInstance = new UserManager();
    private LinkUpUser myUser, userSelected;

    private HashMap<String, LinkUpBlockedUser> myBlockedUsers;

    private UserManager() {
    }

    public static UserManager getInstance() {
        return ourInstance;
    }

    public final LinkUpUser getMyUser() {
//        if (myUser != null) {
//            new UserService()
//        }
        return myUser;
    }

    public void setMyUser(LinkUpUser myUser) {
        this.myUser = myUser;
    }

    public final LinkUpUser getUserSelected() {
        return userSelected;
    }

    public void setUserSelected(LinkUpUser otherUser) {
        this.userSelected = otherUser;
    }

    public void updatePicture(int i, String url, Context context) {
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

        new UserService(context).updateUser(myUser.getId(), myUser, new Callback<ServerResponse<String>>() {
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


    public void updateMyBlockedUsers(Context context) {
        new UserService(context).getBlockedUsersByMe(myUser.getId(), new Callback<ServerResponse<LinkUpBlockedUser[]>>() {
            @Override
            public void onResponse(Call<ServerResponse<LinkUpBlockedUser[]>> call, Response<ServerResponse<LinkUpBlockedUser[]>> response) {
                if (response.isSuccessful() && response.body().data != null) {
                    myBlockedUsers = new HashMap<String, LinkUpBlockedUser>();
                    for (LinkUpBlockedUser u: response.body().data) {
                        myBlockedUsers.put(u.getUserId(), u);
                    }
                }
            }

            @Override
            public void onFailure(Call<ServerResponse<LinkUpBlockedUser[]>> call, Throwable t) {
                Log.e("GET BLOCKED USERS", "ERROR");
            }
        });
    }

    public boolean isBlocked(String otherUserId) {
        return myBlockedUsers.containsKey(otherUserId);
    }

}