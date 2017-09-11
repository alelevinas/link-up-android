package com.fiuba.tdp.linkup.util;

import android.util.AndroidRuntimeException;

/**
 * Created by alejandro on 9/11/17.
 */

public class UserIsNotOldEnoughException extends AndroidRuntimeException {

    public UserIsNotOldEnoughException() {
    }

    public UserIsNotOldEnoughException(String name) {
        super(name);
    }

    public UserIsNotOldEnoughException(String name, Throwable cause) {
        super(name, cause);
    }

    public UserIsNotOldEnoughException(Exception cause) {
        super(cause);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
