package com.kabasonic.notepad.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.kabasonic.notepad.data.db.NoteWithImages;
import com.kabasonic.notepad.data.model.Image;
import com.kabasonic.notepad.data.model.Note;

import java.util.List;

@Dao
public interface NoteDao {

    @Transaction
    @Insert
    long insertNote(Note note);

    @Insert
    void insertImages(List<Image> imageList);

    @Transaction
    @Query("SELECT * FROM note_table")
    LiveData<List<NoteWithImages>> getAllNotesWithImages();

    @Query("SELECT * FROM note_table WHERE note_id = :idNote")
    LiveData<NoteWithImages> getNoteWithImage(int idNote);

    @Delete
    void deleteNote(Note note);

    @Query("DELETE FROM image_table WHERE image_id_fk_note = :noteId")
    void deleteImages(int noteId);
}
