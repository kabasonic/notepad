package com.kabasonic.notepad.data.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.kabasonic.notepad.data.dao.ImageDao;
import com.kabasonic.notepad.data.dao.NoteDao;
import com.kabasonic.notepad.data.db.NoteDatabase;
import com.kabasonic.notepad.data.db.NoteWithImages;
import com.kabasonic.notepad.data.db.NoteWithTasks;
import com.kabasonic.notepad.data.model.Image;
import com.kabasonic.notepad.data.model.Task;

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



    public LiveData<NoteWithTasks> getNoteWithTasks(int noteId){
        return noteDao.getNoteWithTasks(noteId);
    }

    public void insertTasks(NoteWithTasks noteWithTasks) {
        new InsertNoteWithTasksAsyncTask(noteDao).execute(noteWithTasks);
    }

    private static class InsertNoteWithTasksAsyncTask extends AsyncTask<NoteWithTasks, Void, Void> {
        private NoteDao noteDao;

        private InsertNoteWithTasksAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(NoteWithTasks... noteWithTasks) {

            for (Task task: noteWithTasks[0].taskList) {
                if(task.getId() == 0 && task.getIdFkNoteTask() == 0){
                    task.setIdFkNoteTask(noteDao.getLastId());
                    noteDao.insertTaskToNote(task);
                }

            }
            return null;
        }
    }

    public void updateTasks(NoteWithTasks noteWithTasks){
        new UpdateNoteWithTasksAsyncTask(noteDao).execute(noteWithTasks);
    }

    private static class UpdateNoteWithTasksAsyncTask extends AsyncTask<NoteWithTasks,Void,Void>{
        private NoteDao noteDao;


        private UpdateNoteWithTasksAsyncTask(NoteDao noteDao){
            this.noteDao = noteDao;

        }

        @Override
        protected Void doInBackground(NoteWithTasks... noteWithTasks) {

            noteDao.updateNote(noteWithTasks[0].note);
            Task taskObject = new Task();
            for(Task task: noteWithTasks[0].taskList){
                if(task.getIdFkNoteTask() == 0){
                    task.setIdFkNoteTask(noteWithTasks[0].note.getId());
                    noteDao.insertTaskToNote(task);
                }else{
                    noteDao.updateTask(task);
                }
            }
            return null;
        }
    }

    public void deleteListTasks(NoteWithTasks noteWithTasks) {
        new DeleteTaskAsyncTask(noteDao).execute(noteWithTasks);
    }

    private static class DeleteTaskAsyncTask extends AsyncTask< NoteWithTasks,Void,Void>{
        private NoteDao noteDao;

        private DeleteTaskAsyncTask(NoteDao noteDao){
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(NoteWithTasks... noteWithTasks) {
            for (Task task: noteWithTasks[0].taskList){
                if(task.getId() != 0 && task.getIdFkNoteTask() != 0)
                noteDao.deleteTask(task);
            }

            return null;
        }
    }

    public void deleteTask(Task task){
       new DeleteTask(noteDao).execute(task);
    }

    private static class DeleteTask extends AsyncTask<Task,Void,Void>{
        private NoteDao noteDao;

        private DeleteTask(NoteDao noteDao){
            this.noteDao = noteDao;
        }


        @Override
        protected Void doInBackground(Task... tasks) {
            if(tasks[0].getId() != 0){
                noteDao.deleteTask(tasks[0]);
            }
            return null;
        }
    }

}
