package com.fiuba.tdp.linkup.util;

import android.util.AndroidRuntimeException;

/**
 * Created by alejandro on 9/11/17.
 */

public class UserDoesNotHaveFacebookPicture extends AndroidRuntimeException {
    public UserDoesNotHaveFacebookPicture(String s) {
    }

    public UserDoesNotHaveFacebookPicture(String name, Throwable cause) {
        super(name, cause);
    }

    public UserDoesNotHaveFacebookPicture(Exception cause) {
        super(cause);
    }

    public UserDoesNotHaveFacebookPicture() {
    }

}
