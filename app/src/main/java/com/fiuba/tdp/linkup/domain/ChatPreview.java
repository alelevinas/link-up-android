package com.fiuba.tdp.linkup.domain;

/**
 * Created by alejandro on 10/7/17.
 */

public class ChatPreview {
    private String mName;
    private String mLastMessage;
    private String mOtherUserId;
//    private String mMessageId;
//    private Boolean mIsRead;

    public ChatPreview() {
        // Needed for Firebase
    }

    public ChatPreview(String name, String lastMessage, String otherUserId) {
        this.mName = name;
        this.mLastMessage = lastMessage;
        this.mOtherUserId = otherUserId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getLastMessage() {
        return mLastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.mLastMessage = lastMessage;
    }

    public String getOtherUserId() {
        return mOtherUserId;
    }

    public void setOtherUserId(String otherUserId) {
        this.mOtherUserId = otherUserId;
    }

//    public String getmMessageId() {
//        return mMessageId;
//    }
//
//    public void setmMessageId(String mMessageId) {
//        this.mMessageId = mMessageId;
//    }
//
//    public Boolean getmIsRead() {
//        return mIsRead;
//    }
//
//    public void setmIsRead(Boolean mIsRead) {
//        this.mIsRead = mIsRead;
//    }
//
//    public String getmPhotoUrl() {
//        return mPhotoUrl;
//    }
//
//    public void setmPhotoUrl(String mPhotoUrl) {
//        this.mPhotoUrl = mPhotoUrl;
//    }
}
