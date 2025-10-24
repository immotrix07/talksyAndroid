package com.talksy.android.data.model;

public class UpdateProfileRequest {
    private String profilePic;

    public UpdateProfileRequest(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }
}
