package com.example.wallpaperunsplash_advancedandroid.models;

import java.io.Serializable;

public class CollectionPreFiles implements Serializable {
    public String img1, img2, img3;
    public String idCollection;
    public String nameCollection, ownerName, totalPhotos;

    public String getIdCollection() {
        return idCollection;
    }

    public void setIdCollection(String idCollection) {
        this.idCollection = idCollection;
    }



    public String getImg1() {
        return img1;
    }

    public void setImg1(String img1) {
        this.img1 = img1;
    }

    public String getImg2() {
        return img2;
    }

    public void setImg2(String img2) {
        this.img2 = img2;
    }

    public String getImg3() {
        return img3;
    }

    public void setImg3(String img3) {
        this.img3 = img3;
    }

    public String getNameCollection() {
        return nameCollection;
    }

    public void setNameCollection(String nameCollection) {
        this.nameCollection = nameCollection;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getTotalPhotos() {
        return totalPhotos;
    }

    public void setTotalPhotos(String totalPhotos) {
        this.totalPhotos = totalPhotos;
    }

    public CollectionPreFiles() {
    }

    public CollectionPreFiles(String idCollection, String img1, String img2, String img3, String nameCollection, String ownerName, String totalPhotos) {
        this.idCollection = idCollection;
        this.img1 = img1;
        this.img2 = img2;
        this.img3 = img3;
        this.nameCollection = nameCollection;
        this.ownerName = ownerName;
        this.totalPhotos = totalPhotos;
    }
}
