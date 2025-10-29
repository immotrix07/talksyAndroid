package com.talksy.android.models;

import java.util.List;

public class Chat {
    private String id;
    private String chatName;
    private List<User> users;
    private Message lastMessage;

    public Chat(String id, String chatName, List<User> users, Message lastMessage) {
        this.id = id;
        this.chatName = chatName;
        this.users = users;
        this.lastMessage = lastMessage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChatName() {
        return chatName;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public Message getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(Message lastMessage) {
        this.lastMessage = lastMessage;
    }
}
