package com.fiuba.tdp.linkup.domain;

/**
 * Created by alejandro on 9/27/17.
 */

public class ChatMessage {
    private String mName;
    private String mMessage;
    private String mUid;
    private String mMessageId;
    private Boolean mLiked;

    public ChatMessage() {
        // Needed for Firebase
    }

    public ChatMessage(String uid, String name, String message, Boolean liked) {
        mName = name;
        mMessage = message;
        mUid = "";
        mLiked = liked;
    }

    public ChatMessage(String messageId, String uid, String name, String message, Boolean liked) {
        mName = name;
        mMessage = message;
        mUid = uid;
        mLiked = liked;
        mMessageId = messageId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public String getUid() {
        return mUid;
    }

    public void setUid(String uid) {
        mUid = uid;
    }

    public Boolean getLiked() {
        return mLiked;
    }

    public void setLiked(Boolean liked) {
        this.mLiked = liked;
    }

    public String getMessageId() {
        return mMessageId;
    }

    public void setMessageId(String mMessageId) {
        this.mMessageId = mMessageId;
    }
}


