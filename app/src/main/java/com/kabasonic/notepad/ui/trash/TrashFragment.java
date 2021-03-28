package com.kabasonic.notepad.ui.trash;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.kabasonic.notepad.R;
import com.kabasonic.notepad.data.db.NoteWithImages;
import com.kabasonic.notepad.data.model.Image;
import com.kabasonic.notepad.data.model.Note;
import com.kabasonic.notepad.ui.adapters.HomeFragmentAdapter;

import java.util.ArrayList;
import java.util.List;

public class TrashFragment extends Fragment {

    private Context mContext;
    private RecyclerView recyclerView;
    private HomeFragmentAdapter mAdapter;
    private TrashViewModel trashViewModel;
    private View view;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        trashViewModel = new ViewModelProvider(this).get(TrashViewModel.class);
        view = inflater.inflate(R.layout.fragment_trash,container,false);
        recyclerView = view.findViewById(R.id.rv_trash_fragment);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        buildRecyclerView();
        getAllNotes();
    }

    private void getAllNotes(){
        trashViewModel.getAllNoteWithImagesTrash().observe(getViewLifecycleOwner(), new Observer<List<NoteWithImages>>() {
            @Override
            public void onChanged(List<NoteWithImages> noteWithImages) {
                List<Note> noteList = new ArrayList<>();
                List<List<Image>> imagesList = new ArrayList<List<Image>>();
                for (NoteWithImages itemObject : noteWithImages) {
                    noteList.add(itemObject.note);
                    imagesList.add(itemObject.imageList);
                }
                mAdapter.setDataAdapter(noteList, imagesList);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    private void buildRecyclerView(){
        recyclerView.setHasFixedSize(true);
        mAdapter = new HomeFragmentAdapter(mContext);
        recyclerView.setLayoutManager(new GridLayoutManager(mContext,1));
        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new HomeFragmentAdapter.OnItemClickListener() {
            @Override
            public void onClickItemView(Note note) {
                Snackbar.make(view,"To view the note, you need to restore it. Swipe left to restore, right to delete.",Snackbar.LENGTH_LONG).show();
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT|ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                // remove with adapter and delete with BD
                if(direction == ItemTouchHelper.RIGHT){
                    trashViewModel.deleteNoteWithImagesTrash(mAdapter.getNoteAt(viewHolder.getAdapterPosition()));
                    Snackbar.make(view,"The note was deleted",Snackbar.LENGTH_SHORT).show();
                }else if(direction == ItemTouchHelper.LEFT){
                    NoteWithImages noteWithImages = mAdapter.getNoteWithImagesAt(viewHolder.getAdapterPosition());
                    noteWithImages.note.setDeletedAt(null);
                    trashViewModel.restoreNoteWithTrash(noteWithImages);
                    mAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                    Snackbar.make(view,"The note was restored",Snackbar.LENGTH_SHORT).show();
                }

            }
        }).attachToRecyclerView(recyclerView);
    }
}
