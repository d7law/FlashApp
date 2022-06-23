package com.example.wallpaperunsplash_advancedandroid.adapter.roomDatabase;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class PathDownload {
    @PrimaryKey(autoGenerate = true)
    int id;
    @ColumnInfo(name = "path")
    String path;
    @Ignore
    public PathDownload(){}

    public int getId() {
        return id;
    }

    public PathDownload(String path) {
        this.path = path;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
