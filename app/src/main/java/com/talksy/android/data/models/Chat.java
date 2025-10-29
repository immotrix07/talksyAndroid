package com.talksy.android.data.models;

import java.util.List;

public class Chat {
    private String id;
    private String chatName;
    private List<User> users;
    private Message lastMessage;

    public String getId() {
        return id;
    }

    public String getChatName() {
        return chatName;
    }

    public List<User> getUsers() {
        return users;
    }

    public Message getLastMessage() {
        return lastMessage;
    }
}
