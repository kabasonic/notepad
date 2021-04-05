package com.kabasonic.notepad.ui.favorite;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.kabasonic.notepad.R;
import com.kabasonic.notepad.data.db.NoteWithImages;
import com.kabasonic.notepad.data.model.Image;
import com.kabasonic.notepad.data.model.Note;
import com.kabasonic.notepad.ui.adapters.HomeFragmentAdapter;
import com.kabasonic.notepad.ui.home.HomeViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FavoriteFragment extends Fragment {

    private Context mContext;
    private View view;
    private RecyclerView recyclerView;
    private HomeFragmentAdapter mAdapter;
    private FavoriteViewModel favoriteViewModel;
    private HomeViewModel homeViewModel;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        favoriteViewModel = new ViewModelProvider(this).get(FavoriteViewModel.class);
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        view = inflater.inflate(R.layout.fragment_favorite,container,false);
        recyclerView = view.findViewById(R.id.rv_favorite_fragment);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        buildRecyclerView();
        getAllFavoriteNotesWithImages();

        getDisplayElement();
        getDisplayContent();
    }

    private void getDisplayElement(){
        homeViewModel.getDisplayElements().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                Log.d("HomeViewModel", "onChanged");
                if(recyclerView != null && mAdapter != null && integer != null){
                    Log.d("DISPLAY ELEMNTS","HERE");
                    recyclerView.setLayoutManager(new GridLayoutManager(mContext, integer));
                    mAdapter.displayingView(integer);
                    mAdapter.notifyDataSetChanged();
                }

            }
        });
    }

    private void getDisplayContent(){
        homeViewModel.getDisplayContent().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if(mAdapter!=null){
                    mAdapter.displayingBody(integer);
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void getAllFavoriteNotesWithImages(){
        favoriteViewModel.getAllFavoriteNotesWithImages().observe(getViewLifecycleOwner(), new Observer<List<NoteWithImages>>() {
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
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 1));
        mAdapter = new HomeFragmentAdapter(mContext);
        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new HomeFragmentAdapter.OnItemClickListener() {
            @Override
            public void onClickItemView(Note note) {
                NavDirections action = FavoriteFragmentDirections.actionFavoriteFragmentToNoteFragment("",note.getId());
                Navigation.findNavController(view).navigate(action);
            }

            @Override
            public void onClickFavorite(boolean action, int position) {
                NoteWithImages noteWithImages = mAdapter.getNoteWithImagesAt(position);
                noteWithImages.note.setFavorite(action);
                favoriteViewModel.updateNoteWithImages(noteWithImages);
                mAdapter.notifyDataSetChanged();
            }

        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                // remove with adapter and delete with BD
                if(direction == ItemTouchHelper.RIGHT){
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyy hh:mm");
                    Date date = new Date(System.currentTimeMillis());
                    NoteWithImages noteWithImages = mAdapter.getNoteWithImagesAt(viewHolder.getAdapterPosition());
                    noteWithImages.note.setDeletedAt(sdf.format(date));
                    favoriteViewModel.updateNoteWithImages(noteWithImages);
                    Snackbar.make(view,"The note was moved to the trash.",Snackbar.LENGTH_SHORT).show();
                }
            }
        }).attachToRecyclerView(recyclerView);
    }

}
