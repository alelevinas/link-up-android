package com.fiuba.tdp.linkup.domain.facebook;

/**
 * Created by alejandro on 9/10/17.
 */

class FacebookPaging {
    String previous;
    String next;

    public FacebookPaging(String previous, String next) {
        this.previous = previous;
        this.next = next;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }
}
