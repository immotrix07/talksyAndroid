package com.talksy.android.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Message implements Parcelable {
    private String _id;
    private String senderId;
    private String receiverId;
    private String text;
    private String image;
    private String createdAt;
    private String updatedAt;

    public Message() {}

    public Message(String _id, String senderId, String receiverId, String text, String image, String createdAt) {
        this._id = _id;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.text = text;
        this.image = image;
        this.createdAt = createdAt;
    }

    protected Message(Parcel in) {
        _id = in.readString();
        senderId = in.readString();
        receiverId = in.readString();
        text = in.readString();
        image = in.readString();
        createdAt = in.readString();
        updatedAt = in.readString();
    }

    public static final Creator<Message> CREATOR = new Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };

    // Getters and Setters
    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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
        dest.writeString(senderId);
        dest.writeString(receiverId);
        dest.writeString(text);
        dest.writeString(image);
        dest.writeString(createdAt);
        dest.writeString(updatedAt);
    }
}
