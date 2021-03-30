package com.kabasonic.notepad.data.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.kabasonic.notepad.data.dao.ImageDao;
import com.kabasonic.notepad.data.dao.NoteDao;
import com.kabasonic.notepad.data.db.NoteDatabase;
import com.kabasonic.notepad.data.db.NoteWithImages;
import com.kabasonic.notepad.data.model.Image;

import java.util.List;

public class FavoriteRepository {

    private NoteDao noteDao;
    private ImageDao imageDao;

    LiveData<List<NoteWithImages>> allFavoriteNotesWithImages;

    public FavoriteRepository(Application application){

        NoteDatabase database = NoteDatabase.getInstance(application);

        this.noteDao = database.noteDao();
        this.imageDao = database.imageDao();

        allFavoriteNotesWithImages = noteDao.getAllNotesWithImagesFavorite();
    }

    public LiveData<List<NoteWithImages>> getAllFavoriteNotesWithImages(){
        return allFavoriteNotesWithImages;
    }

    public void update(NoteWithImages noteWithImages){
        new UpdateNoteWithImagesAsyncTask(noteDao, imageDao).execute(noteWithImages);
    }

    private static class UpdateNoteWithImagesAsyncTask extends AsyncTask<NoteWithImages,Void,Void>{
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
