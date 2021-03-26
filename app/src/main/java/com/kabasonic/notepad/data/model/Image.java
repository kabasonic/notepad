package com.kabasonic.notepad.data.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "image_table")
public class Image {

    @ColumnInfo(name = "image_id")
    @PrimaryKey(autoGenerate = true)
    int id;

    @ColumnInfo (name = "image_id_fk_note")
    long idFkNote;

    @ColumnInfo (name = "image_uri")
    String uri;

    @Ignore
    public Image(){
        //empty constructor
    }

    public Image( String uri) {
        this.uri = uri;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getIdFkNote() {
        return idFkNote;
    }

    public void setIdFkNote(long idFkNote) {
        this.idFkNote = idFkNote;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
