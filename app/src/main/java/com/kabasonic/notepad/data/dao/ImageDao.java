package com.kabasonic.notepad.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.kabasonic.notepad.data.model.Image;

import java.util.List;

@Dao
public interface ImageDao {

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void insert(Image image);

    @Delete
    void deleteImageWithNote(Image image);

    @Query("SELECT * FROM image_table")
    LiveData<List<Image>> getAllImages();

}
