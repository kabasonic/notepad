package com.kabasonic.notepad.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.kabasonic.notepad.data.db.NoteWithImages;
import com.kabasonic.notepad.data.model.Image;
import com.kabasonic.notepad.data.model.Note;

import java.util.List;

@Dao
public interface NoteDao {

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertNote(Note note);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertImages(List<Image> imageList);

    @Transaction
    @Update(onConflict = OnConflictStrategy.REPLACE)
    int updateNote(Note note);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateImages(List<Image> imageList);

    //home fragment
    @Transaction
    @Query("SELECT * FROM note_table")
    LiveData<List<NoteWithImages>> getAllNotesWithImages();

    //note fragment
    @Query("SELECT * FROM note_table WHERE note_id = :idNote")
    LiveData<NoteWithImages> getNoteWithImages(int idNote);

    //home fragment
    @Delete
    void deleteNote(Note note);

    @Query("DELETE FROM image_table WHERE image_id_fk_note = :noteId")
    void deleteImages(int noteId);
}
