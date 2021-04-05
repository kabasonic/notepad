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
import com.kabasonic.notepad.data.db.NoteWithTasks;
import com.kabasonic.notepad.data.model.Image;
import com.kabasonic.notepad.data.model.Note;
import com.kabasonic.notepad.data.model.Task;

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

    //favorite fragment
    @Transaction
    @Query("SELECT * FROM note_table WHERE note_favorite = 1 AND deleted_at IS NULL")
    LiveData<List<NoteWithImages>> getAllNotesWithImagesFavorite();

    // trash fragment
    @Transaction
    @Query("SELECT * FROM note_table WHERE deleted_at IS NOT NULL")
    LiveData<List<NoteWithImages>> getAllNotesWithImagesTrash();


    //home fragment
    @Transaction
    @Query("SELECT * FROM note_table WHERE deleted_at IS NULL  ")
    LiveData<List<NoteWithImages>> getAllNotesWithImages();

    //note fragment
    @Transaction
    @Query("SELECT * FROM note_table WHERE note_id = :idNote")
    LiveData<NoteWithImages> getNoteWithImages(int idNote);

    //home fragment
    @Delete
    void deleteNote(Note note);

    @Query("DELETE FROM image_table WHERE image_id_fk_note = :noteId")
    void deleteImages(int noteId);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTaskToNote(Task task);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateTask(Task task);

    @Transaction
    @Query("SELECT * FROM note_table WHERE note_id = :noteId")
    LiveData<NoteWithTasks> getNoteWithTasks(int noteId);




    @Query("SELECT MAX(note_id) FROM note_table")
    int getLastId();

    @Delete
    void deleteTask(Task task);

    @Query("DELETE FROM note_table WHERE deleted_at IS NOT NULL")
    void deleteAllNotesInsideTrash();

}
