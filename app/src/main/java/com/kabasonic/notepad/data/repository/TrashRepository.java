package com.kabasonic.notepad.data.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.kabasonic.notepad.data.dao.ImageDao;
import com.kabasonic.notepad.data.dao.NoteDao;
import com.kabasonic.notepad.data.db.NoteDatabase;
import com.kabasonic.notepad.data.db.NoteWithImages;
import com.kabasonic.notepad.data.model.Image;
import com.kabasonic.notepad.data.model.Note;

import java.util.List;

public class TrashRepository {

    private NoteDao noteDao;
    private ImageDao imageDao;

    private LiveData<List<NoteWithImages>> allNotesWithImagesTrash;


    public TrashRepository(Application application){
        NoteDatabase database = NoteDatabase.getInstance(application);
        this.noteDao = database.noteDao();
        this.imageDao = database.imageDao();

        this.allNotesWithImagesTrash = noteDao.getAllNotesWithImagesTrash();

    }

    public LiveData<List<NoteWithImages>> getAllNotesWithImagesTrash(){
        return allNotesWithImagesTrash;
    }

    public void update(NoteWithImages noteWithImages){
        new UpdateNoteWithImagesAsyncTask(noteDao, imageDao).execute(noteWithImages);
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

    private static class UpdateNoteWithImagesAsyncTask extends AsyncTask<NoteWithImages,Void,Void> {
        private NoteDao noteDao;
        private ImageDao imageDao;

        private UpdateNoteWithImagesAsyncTask(NoteDao noteDao, ImageDao imageDao){
            this.noteDao = noteDao;
            this.imageDao = imageDao;
        }

        @Override
        protected Void doInBackground(NoteWithImages... noteWithImages) {

            noteDao.updateNote(noteWithImages[0].note);

            for(Image image: noteWithImages[0].imageList){
                if(image.getId() == 0 && image.getIdFkNote() == 0){
                    image.setIdFkNote(noteWithImages[0].note.getId());
                    imageDao.insert(image);
                }
            }
            return null;
        }
    }


}
