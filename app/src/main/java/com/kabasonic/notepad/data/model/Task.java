package com.kabasonic.notepad.data.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "task_table")
public class Task {

    @ColumnInfo(name = "task_id")
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo (name = "task_id_fk_note")
    public long idFkNoteTask;

    @ColumnInfo(name = "text_task")
    private String body;

    @ColumnInfo(name = "status_task")
    private boolean completedTask;

    @Ignore
    public Task(){

    }

    public Task(String body, boolean completedTask){
        this.body = body;
        this.completedTask = completedTask;
    }

    public long getIdFkNoteTask() {
        return idFkNoteTask;
    }

    public void setIdFkNoteTask(long idFkNoteTask) {
        this.idFkNoteTask = idFkNoteTask;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public boolean isCompletedTask() {
        return completedTask;
    }

    public void setCompletedTask(boolean completedTask) {
        this.completedTask = completedTask;
    }

}
