package com.kabasonic.notepad.ui.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.kabasonic.notepad.data.db.NoteWithImages;
import com.kabasonic.notepad.data.model.Note;
import com.kabasonic.notepad.data.repository.HomeRepository;

import java.util.List;

public class HomeViewModel extends AndroidViewModel {

    private HomeRepository homeRepository;
    private LiveData<List<NoteWithImages>> allNotesWithImages;

    private MutableLiveData<Integer> displayElements;
    private MutableLiveData<Integer> displayContent;


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

    public void updateNoteWithImages(NoteWithImages noteWithImages){
        homeRepository.update(noteWithImages);
    }

    public MutableLiveData<Integer> getDisplayElements() {
        if(displayElements == null){
            displayElements = new MutableLiveData<>();
        }
        return displayElements;
    }

    public MutableLiveData<Integer> getDisplayContent(){
        if(displayContent == null){
            displayContent = new MutableLiveData<>();
        }
        return displayContent;
    }

}
