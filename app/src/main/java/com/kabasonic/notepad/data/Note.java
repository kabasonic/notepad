package com.kabasonic.notepad.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "note_table")

public class Note {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_column")
    private int id;

    @ColumnInfo(name = "title_column")
    private String title;

    @ColumnInfo(name = "body_column")
    private String body;

    @ColumnInfo(name = "lastTimeUpdate_column")
    private long lastTimeUpdate;

    @ColumnInfo(name = "favorite_column")
    private boolean favorite;

    @ColumnInfo(name = "reminderIsSet_column")
    private boolean reminderIsSet;

    @ColumnInfo(name = "backgroundColor")
    private int backgroundColor = 0xffffff;

    @Ignore
    public Note(){
        //empty constructor
    }

    public Note(String title, String body,  int backgroundColor, long lastTimeUpdate) {
        this.title = title;
        this.body = body;
        this.backgroundColor = backgroundColor;
        this.lastTimeUpdate = lastTimeUpdate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public long getLastTimeUpdate() {
        return lastTimeUpdate;
    }

    public void setLastTimeUpdate(long lastTimeUpdate) {
        this.lastTimeUpdate = lastTimeUpdate;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public boolean isReminderIsSet() {
        return reminderIsSet;
    }

    public void setReminderIsSet(boolean reminderIsSet) {
        this.reminderIsSet = reminderIsSet;
    }

    public int getBackgroundColor() {

        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
}
