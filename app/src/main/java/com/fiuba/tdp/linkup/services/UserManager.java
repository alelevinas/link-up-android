package com.fiuba.tdp.linkup.services;

import com.fiuba.tdp.linkup.domain.LinkUpUser;

/**
 * Created by alejandro on 9/10/17.
 */

public class UserManager {
    private static final UserManager ourInstance = new UserManager();

    private LinkUpUser myUser;

    private UserManager() {
    }

    public static UserManager getInstance() {
        return ourInstance;
    }

    public LinkUpUser getMyUser() {
        return myUser;
    }

    public void setMyUser(LinkUpUser myUser) {
        this.myUser = myUser;
    }
}
