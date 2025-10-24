package com.talksy.android.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    private String _id;
    private String email;
    private String fullName;
    private String profilePic;
    private String createdAt;
    private String updatedAt;

    public User() {}

    public User(String _id, String email, String fullName, String profilePic) {
        this._id = _id;
        this.email = email;
        this.fullName = fullName;
        this.profilePic = profilePic;
    }

    protected User(Parcel in) {
        _id = in.readString();
        email = in.readString();
        fullName = in.readString();
        profilePic = in.readString();
        createdAt = in.readString();
        updatedAt = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    // Getters and Setters
    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_id);
        dest.writeString(email);
        dest.writeString(fullName);
        dest.writeString(profilePic);
        dest.writeString(createdAt);
        dest.writeString(updatedAt);
    }
}
