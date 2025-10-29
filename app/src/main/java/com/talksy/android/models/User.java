package com.talksy.android.models;

public class User {
    private String id;
    private String fullName;
    private String email;
    private String profilePic;

    public User(String id, String fullName, String email, String profilePic) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.profilePic = profilePic;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }
}
