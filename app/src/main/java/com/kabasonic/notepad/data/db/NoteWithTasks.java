package com.kabasonic.notepad.data.db;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.kabasonic.notepad.data.model.Note;
import com.kabasonic.notepad.data.model.Task;

import java.util.List;

public class NoteWithTasks {
    @Embedded
    public Note note;
    @Relation(
            parentColumn = "note_id",
            entityColumn = "task_id_fk_note"
    )
    public List<Task> taskList;

    public NoteWithTasks(Note note, List<Task> taskList) {
        this.note = note;
        this.taskList = taskList;
    }
}
