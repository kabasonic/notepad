package com.kabasonic.notepad.data.db;


import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.kabasonic.notepad.data.dao.ImageDao;
import com.kabasonic.notepad.data.dao.NoteDao;
import com.kabasonic.notepad.data.model.Image;
import com.kabasonic.notepad.data.model.Note;

@Database(entities = {Note.class, Image.class}, version = 3)
public abstract class NoteDatabase extends RoomDatabase {

    private static NoteDatabase instance;

    public abstract NoteDao noteDao();
    public abstract ImageDao imageDao();

    public static synchronized NoteDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext()
                    ,NoteDatabase.class
                    , "note_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }

        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };


    private static class PopulateDbAsyncTask extends AsyncTask<Void,Void,Void>{

        private NoteDao noteDao;
        private ImageDao imageDao;

        private PopulateDbAsyncTask(NoteDatabase noteDatabase){
            this.noteDao = noteDatabase.noteDao();
            this.imageDao = noteDatabase.imageDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            //init elements
            return null;
        }
    }

}
