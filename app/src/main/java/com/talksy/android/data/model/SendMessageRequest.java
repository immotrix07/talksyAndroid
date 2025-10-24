package com.talksy.android.data.model;

public class SendMessageRequest {
    private String text;
    private String image;

    public SendMessageRequest() {}

    public SendMessageRequest(String text, String image) {
        this.text = text;
        this.image = image;
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
}
