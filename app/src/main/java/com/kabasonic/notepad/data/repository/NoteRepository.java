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

public class NoteRepository {

    private NoteDao noteDao;
    private ImageDao imageDao;

    public NoteRepository(Application application) {
        NoteDatabase database = NoteDatabase.getInstance(application);
        this.noteDao = database.noteDao();
        this.imageDao = database.imageDao();
    }

    public void insert(NoteWithImages noteWithImages) {
        new InsertNoteWithImagesAsyncTask(noteDao).execute(noteWithImages);
    }

    public void deleteImageWithNote(Image image){
        new DeleteImageWithNoteAsyncTask(imageDao).execute(image);
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

    private static class DeleteImageWithNoteAsyncTask extends AsyncTask<Image, Void, Void>{

        private ImageDao imageDao;

        private DeleteImageWithNoteAsyncTask(ImageDao imageDao){
            this.imageDao = imageDao;
        }

        @Override
        protected Void doInBackground(Image... images) {
            imageDao.deleteImageWithNote(images[0]);
            return null;
        }
    }

    private static class InsertNoteWithImagesAsyncTask extends AsyncTask<NoteWithImages, Void, Void> {
        private NoteDao noteDao;

        private InsertNoteWithImagesAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(NoteWithImages... noteWithImages) {

            long identifier = noteDao.insertNote(noteWithImages[0].note);

            for (Image image: noteWithImages[0].imageList) {
                image.setIdFkNote(identifier);
            }
            noteDao.insertImages(noteWithImages[0].imageList);
            return null;
        }
    }

    public LiveData<List<Image>> getAllImages(){
        return imageDao.getAllImages();
    }

    public LiveData<NoteWithImages> getNoteWithImages(int idNote){
        return noteDao.getNoteWithImages(idNote);
    }


}
