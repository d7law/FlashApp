package com.example.wallpaperunsplash_advancedandroid.models;

import java.io.Serializable;

public class ImageFiles implements Serializable {
    public String getId_img() {
        return id_img;
    }

    public void setId_img(String id_img) {
        this.id_img = id_img;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public ImageFiles(){

    }

    public ImageFiles(String id_img, String image, String ownerName) {
        this.id_img = id_img;
        this.image = image;
        this.ownerName = ownerName;
    }
    public ImageFiles(String image){
        this.image = image;
    }
    public String id_img;
    public String image;
    public String ownerName;



}
