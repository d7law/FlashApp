package com.example.wallpaperunsplash_advancedandroid.adapter.roomDatabase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PathDAO {
    @Query("SELECT * FROM PathDownload")
    List<PathDownload> getAll();

    @Insert
    void insertAll(PathDownload... pathDownloads);

    @Insert
    void insertPath(PathDownload pathDownload);

    @Delete
    void deletePath(PathDownload pathDownload);

    @Update
    void updatePath(PathDownload pathDownload);

    @Delete
    void deleteMultiPath(PathDownload... pathDownloads);

    @Query("DELETE FROM PathDownload")
    void delete();
}
