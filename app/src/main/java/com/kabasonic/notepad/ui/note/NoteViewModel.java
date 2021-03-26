package com.kabasonic.notepad.ui.note;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.kabasonic.notepad.data.db.NoteWithImages;
import com.kabasonic.notepad.data.repository.NoteRepository;

public class NoteViewModel extends AndroidViewModel {

    private NoteRepository noteRepository;
    private LiveData<NoteWithImages> noteWithImages;

    public NoteViewModel(@NonNull Application application) {
        super(application);

        this.noteRepository = new NoteRepository(application);

    }

    public void insertNoteWithImages(NoteWithImages noteWithImages){
        noteRepository.insert(noteWithImages);
    }

    public LiveData<NoteWithImages> getNoteWithImages(int noteId){
        return noteRepository.getNoteWithImages(noteId);
    }



}
