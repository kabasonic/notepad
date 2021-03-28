package com.kabasonic.notepad.ui.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.kabasonic.notepad.data.db.NoteWithImages;
import com.kabasonic.notepad.data.model.Note;
import com.kabasonic.notepad.data.repository.HomeRepository;

import java.util.List;

public class HomeViewModel extends AndroidViewModel {

    private HomeRepository homeRepository;
    private LiveData<List<NoteWithImages>> allNotesWithImages;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        this.homeRepository = new HomeRepository(application);

        this.allNotesWithImages = homeRepository.getAllNotesWithImages();
    }

    public void deleteNoteWithImages(Note note){
        homeRepository.deleteNoteWithImages(note);

    }

    public LiveData<List<NoteWithImages>> getGetAllNotesWithImages(){
        return allNotesWithImages;
    }




}
