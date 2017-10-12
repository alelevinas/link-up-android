package com.fiuba.tdp.linkup.domain;

/**
 * Created by alejandro on 10/7/17.
 */

public class ChatPreview {
    private String mName;
    private String mLastMessage;
    private String mOtherUserId;
    private String mMessageId;
    private Boolean mIsRead;
    private Boolean blocked_by_me;
    private Boolean blocked_by_other;

    public ChatPreview() {
        // Needed for Firebase
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getLastMessage() {
        return mLastMessage;
    }

    public void setLastMessage(String mLastMessage) {
        this.mLastMessage = mLastMessage;
    }

    public String getOtherUserId() {
        return mOtherUserId;
    }

    public void setOtherUserId(String mOtherUserId) {
        this.mOtherUserId = mOtherUserId;
    }

    public String getMessageId() {
        return mMessageId;
    }

    public void setMessageId(String mMessageId) {
        this.mMessageId = mMessageId;
    }

    public Boolean getIsRead() {
        return mIsRead;
    }

    public void setIsRead(Boolean mIsRead) {
        this.mIsRead = mIsRead;
    }

    public Boolean getBlocked_by_me() {
        return blocked_by_me != null ? blocked_by_me : false;
    }

    public void setBlocked_by_me(Boolean blocked_by_me) {
        this.blocked_by_me = blocked_by_me;
    }

    public Boolean getBlocked_by_other() {
        return blocked_by_other != null ? blocked_by_other : false;
    }

    public void setBlocked_by_other(Boolean blocked_by_other) {
        this.blocked_by_other = blocked_by_other;
    }
}
