package com.fiuba.tdp.linkup.services;

import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.fiuba.tdp.linkup.domain.FacebookAlbumItem;
import com.fiuba.tdp.linkup.domain.FacebookPhotoItem;
import com.fiuba.tdp.linkup.domain.FacebookUserItem;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by alejandro on 9/10/17.
 */

public class FacebookService {


    public void getUserData(final Callback<FacebookUserItem> callback) {
        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        if (response.getError() != null) {
                            Log.e("FACEBOOK ERROR", response.getError().getErrorMessage());
                            callback.onFailure(null, null);
                        }
                        Log.i("FACEBOOK RESPONSE", object.toString());

                        FacebookUserItem data = new Gson().fromJson(object.toString(), FacebookUserItem.class);
                        callback.onResponse(null, Response.success(data));
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "education,about,birthday,likes,gender,picture");
        request.setParameters(parameters);
        request.executeAsync();
    }

    public void getUserRawData(final Callback<JSONObject> callback) {
        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        if (response.getError() != null) {
                            Log.e("FACEBOOK ERROR", response.getError().getErrorMessage());
                            callback.onFailure(null, null);
                        }
                        Log.i("FACEBOOK RESPONSE", object.toString());
                        callback.onResponse(null, Response.success(object));
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "education,about,birthday,likes,gender,picture");
        request.setParameters(parameters);
        request.executeAsync();
    }

    public void getUserLikes(final Callback<FacebookUserItem.FacebookLikesItem> callback) {
        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        if (response.getError() != null) {
                            Log.e("FACEBOOK ERROR", response.getError().getErrorMessage());
                            callback.onFailure(null, null);
                        }

                        try {
                            Log.i("FACEBOOK RESPONSE", object.toString());

                            FacebookUserItem.FacebookLikesItem data = new Gson().fromJson(object.getJSONObject("likes").toString(), FacebookUserItem.FacebookLikesItem.class);
                            callback.onResponse(null, Response.success(data));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "likes");
        request.setParameters(parameters);
        request.executeAsync();
    }

    public void getAlbums(final Callback<FacebookAlbumItem[]> callback) {
        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        if (response.getError() != null) {
                            Log.e("FACEBOOK ERROR", response.getError().getErrorMessage());
                            callback.onFailure(null, null);
                        }
                        Log.i("FACEBOOK RESPONSE", object.toString());

                        try {
                            JSONArray jsonAlbums = object.getJSONObject("albums").getJSONArray("data");
                            Log.i("ALBUMS ARRAY", jsonAlbums.toString());
                            FacebookAlbumItem[] albums = new Gson().fromJson(jsonAlbums.toString(), FacebookAlbumItem[].class);
                            callback.onResponse(null, Response.success(albums));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "albums{id,name,cover_photo{picture}}");
        request.setParameters(parameters);
        request.executeAsync();
    }

    public void getPhotosFromAlbum(String albumId, final Callback<FacebookPhotoItem[]> callback) {
        GraphRequest request = new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/" + albumId + "/photos",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        if (response.getError() != null) {
                            Log.e("FACEBOOK ERROR", response.getError().getErrorMessage());
                            callback.onFailure(null, null);
                        }
                        Log.i("FACEBOOK RESPONSE", response.getJSONObject().toString());

                        try {
                            JSONArray jsonPhotos = response.getJSONObject().getJSONArray("data");
                            Log.i("PHOTOS ARRAY", jsonPhotos.toString());
                            FacebookPhotoItem[] photos = new Gson().fromJson(jsonPhotos.toString(), FacebookPhotoItem[].class);
                            callback.onResponse(null, Response.success(photos));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,picture, source");
        request.setParameters(parameters);
        request.executeAsync();
    }
}
