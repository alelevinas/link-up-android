package com.fiuba.tdp.linkup.domain;

import java.util.ArrayList;

public class UsersAround {
    private ArrayList<UserAround> usersAround;

    public UsersAround(ArrayList<UserAround> usersAround) {
        this.usersAround = usersAround;
    }

    public ArrayList<String> getIds() {
        ArrayList<String> ids = new ArrayList<>(this.usersAround.size());
        for(int i = 0; i < this.usersAround.size(); i++) {
            ids.add(this.usersAround.get(i).getId());
        }
        return ids;
    }

    public ArrayList<String> getPictures() {
        ArrayList<String> ids = new ArrayList<>(this.usersAround.size());
        for(int i = 0; i < this.usersAround.size(); i++) {
            ids.add(this.usersAround.get(i).getPicture());
        }
        return ids;
    }

    public ArrayList<String> getDescriptions() {
        ArrayList<String> ids = new ArrayList<>(this.usersAround.size());
        for(int i = 0; i < this.usersAround.size(); i++) {
            ids.add(this.usersAround.get(i).getDescription());
        }
        return ids;
    }

    public ArrayList<String> getNames() {
        ArrayList<String> ids = new ArrayList<>(this.usersAround.size());
        for(int i = 0; i < this.usersAround.size(); i++) {
            ids.add(this.usersAround.get(i).getUserName());
        }
        return ids;
    }

}
