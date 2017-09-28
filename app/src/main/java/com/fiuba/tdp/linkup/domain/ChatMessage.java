package com.fiuba.tdp.linkup.domain;

/**
 * Created by alejandro on 9/27/17.
 */

public class ChatMessage {
    private String id;
    private String text;
    private String name;

    public ChatMessage() {
    }

    public ChatMessage(String text, String name) {
        this.text = text;
        this.name = name;
    }


    public ChatMessage(String id, String text, String name) {
        this.id = id;
        this.text = text;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}

