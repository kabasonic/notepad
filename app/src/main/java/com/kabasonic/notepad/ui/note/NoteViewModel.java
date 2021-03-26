package com.kabasonic.notepad.ui.note;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.kabasonic.notepad.data.db.NoteWithImages;
import com.kabasonic.notepad.data.model.Image;
import com.kabasonic.notepad.data.repository.NoteRepository;

import java.util.List;

public class NoteViewModel extends AndroidViewModel {

    private NoteRepository noteRepository;
    private LiveData<List<Image>> allImages;


    public NoteViewModel(@NonNull Application application) {
        super(application);

        this.noteRepository = new NoteRepository(application);
        this.allImages = noteRepository.getAllImages();
    }

    public void insertNoteWithImages(NoteWithImages noteWithImages){
        noteRepository.insert(noteWithImages);
    }

    public void deleteNoteWithImages(Image image){
        noteRepository.deleteImageWithNote(image);
    }

    public LiveData<List<Image>> getAllImages(){
        return allImages;
    }


    public LiveData<NoteWithImages> getNoteWithImages(int noteId){
        return noteRepository.getNoteWithImages(noteId);
    }



}
