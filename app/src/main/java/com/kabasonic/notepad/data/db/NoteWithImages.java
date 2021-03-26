package com.kabasonic.notepad.data.db;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.kabasonic.notepad.data.model.Image;
import com.kabasonic.notepad.data.model.Note;

import java.util.List;

public class NoteWithImages {
    @Embedded
    public Note note;
    @Relation(
            parentColumn = "note_id",
            entityColumn = "image_id_fk_note"
    )
    public List<Image> imageList;

    public NoteWithImages(Note note, List<Image> imageList) {
        this.note = note;
        this.imageList = imageList;
    }

}
