package com.kabasonic.notepad.ui.favorite;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.kabasonic.notepad.data.db.NoteWithImages;
import com.kabasonic.notepad.data.repository.FavoriteRepository;

import java.util.List;

public class FavoriteViewModel extends AndroidViewModel {

    private FavoriteRepository favoriteRepository;
    private LiveData<List<NoteWithImages>> allFavoriteNotesWithImages;

    public FavoriteViewModel(@NonNull Application application) {
        super(application);
        this.favoriteRepository = new FavoriteRepository(application);

        this.allFavoriteNotesWithImages = favoriteRepository.getAllFavoriteNotesWithImages();
    }

    public LiveData<List<NoteWithImages>> getAllFavoriteNotesWithImages(){
        return allFavoriteNotesWithImages;
    }

    public void updateNoteWithImages(NoteWithImages noteWithImages){
        favoriteRepository.update(noteWithImages);
    }

}
