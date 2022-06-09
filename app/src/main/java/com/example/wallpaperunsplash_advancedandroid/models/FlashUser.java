package com.example.wallpaperunsplash_advancedandroid.models;

import java.io.Serializable;

public class FlashUser implements Serializable {
    public String userID;
    public String email;
    public String avatar;
    public String nameOfUser;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNameOfUser() {
        return nameOfUser;
    }

    public void setNameOfUser(String nameOfUser) {
        this.nameOfUser = nameOfUser;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }


    public FlashUser(String userID, String email, String avatar, String nameOfUser, String tel) {
        this.userID = userID;
        this.email = email;
        this.avatar = avatar;
        this.nameOfUser = nameOfUser;
        this.tel = tel;
    }
    public FlashUser(){}
    public String tel;

}
