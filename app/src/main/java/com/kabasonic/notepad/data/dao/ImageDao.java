package com.kabasonic.notepad.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.kabasonic.notepad.data.model.Image;

import java.util.List;

@Dao
public interface ImageDao {

    @Query("SELECT last_insert_rowid() FROM image_table")
    int getLastId();

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void insert(Image image);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(Image image);

    @Delete
    void delete(Image image);

    @Query("DELETE FROM image_table")
    void deleteAllImages();

    @Delete
    void deleteImageWithNote(Image image);

    @Query("SELECT * FROM image_table")
    LiveData<List<Image>> getAllImages();

}
