package com.kabasonic.notepad.data.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.kabasonic.notepad.data.dao.NoteDao;
import com.kabasonic.notepad.data.db.NoteDatabase;
import com.kabasonic.notepad.data.db.NoteWithImages;
import com.kabasonic.notepad.data.model.Note;

import java.util.List;

public class HomeRepository {

    private NoteDao noteDao;

    private LiveData<List<NoteWithImages>> allNotesWithImages;

    public HomeRepository(Application application){
        NoteDatabase database = NoteDatabase.getInstance(application);
        this.noteDao = database.noteDao();

        this.allNotesWithImages = noteDao.getAllNotesWithImages();
    }

    public void deleteNoteWithImages(Note note){
        new DeleteNoteByIdAsyncTask(noteDao,note.getId()).execute(note);
    }

    private class DeleteNoteByIdAsyncTask extends AsyncTask<Note,Void,Void>{

        private NoteDao noteDao;
        private int fkNote;

        private DeleteNoteByIdAsyncTask(NoteDao noteDao,int fkNote){
            this.noteDao = noteDao;
            this.fkNote = fkNote;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.deleteNote(notes[0]);
            noteDao.deleteImages(fkNote);
            return null;
        }
    }



    public LiveData<List<NoteWithImages>> getAllNotesWithImages(){
        return allNotesWithImages;
    }


}
