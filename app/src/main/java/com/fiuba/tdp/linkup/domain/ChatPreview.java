package com.fiuba.tdp.linkup.domain;

/**
 * Created by alejandro on 10/7/17.
 */

public class ChatPreview {
    private String mName;
    private String mLastMessage;
    private String mOtherUserId;
    private String mMessageId;
    private String mPhotoUrl;
    private Boolean mIsRead;

    public ChatPreview() {
        // Needed for Firebase
    }

    public ChatPreview(String mName, String mLastMessage, String mOtherUserId) {
        this.mName = mName;
        this.mLastMessage = mLastMessage;
        this.mOtherUserId = mOtherUserId;
    }

    public ChatPreview(String uid, String name, String message, Boolean liked, String photoUrl) {
        mName = name;
        mLastMessage = message;
        mOtherUserId = "";
        mIsRead = liked;
        mPhotoUrl = photoUrl;
    }

    public ChatPreview(String messageId, String uid, String name, String message, Boolean liked, String photoUrl) {
        mName = name;
        mLastMessage = message;
        mOtherUserId = uid;
        mIsRead = liked;
        mMessageId = messageId;
        mPhotoUrl = photoUrl;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmLastMessage() {
        return mLastMessage;
    }

    public void setmLastMessage(String mLastMessage) {
        this.mLastMessage = mLastMessage;
    }

    public String getmOtherUserId() {
        return mOtherUserId;
    }

    public void setmOtherUserId(String mOtherUserId) {
        this.mOtherUserId = mOtherUserId;
    }

    public String getmMessageId() {
        return mMessageId;
    }

    public void setmMessageId(String mMessageId) {
        this.mMessageId = mMessageId;
    }

    public Boolean getmIsRead() {
        return mIsRead;
    }

    public void setmIsRead(Boolean mIsRead) {
        this.mIsRead = mIsRead;
    }
}
