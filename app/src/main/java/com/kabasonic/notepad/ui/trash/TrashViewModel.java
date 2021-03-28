package com.kabasonic.notepad.ui.trash;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.kabasonic.notepad.data.db.NoteWithImages;
import com.kabasonic.notepad.data.model.Note;
import com.kabasonic.notepad.data.repository.TrashRepository;

import java.util.List;

public class TrashViewModel extends AndroidViewModel {

    private TrashRepository trashRepository;
    private LiveData<List<NoteWithImages>> allNoteWithImagesTrash;

    public TrashViewModel(@NonNull Application application) {
        super(application);

        this.trashRepository = new TrashRepository(application);
        this.allNoteWithImagesTrash = trashRepository.getAllNotesWithImagesTrash();

    }

    public LiveData<List<NoteWithImages>> getAllNoteWithImagesTrash(){
        return allNoteWithImagesTrash;
    }

    public void restoreNoteWithTrash(NoteWithImages noteWithImages){
        trashRepository.update(noteWithImages);
    }

    public void deleteNoteWithImagesTrash(Note note){
        trashRepository.deleteNoteWithImages(note);
    }

}
