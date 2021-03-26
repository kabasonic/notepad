package com.kabasonic.notepad.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.kabasonic.notepad.R;
import com.kabasonic.notepad.data.db.NoteWithImages;
import com.kabasonic.notepad.data.model.Image;
import com.kabasonic.notepad.data.model.Note;
import com.kabasonic.notepad.ui.adapters.HomeFragmentAdapter;
import com.kabasonic.notepad.ui.note.NoteViewModel;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    private View view;
    private FloatingActionButton mFabMore, mFabCreateNote, mFabCreateReminder;
    private boolean clicked = false;
    private Animation fromBottomAnim, rotateCloseAnim, rotateOpenAnim, toBottomAnim;

    private HomeFragmentAdapter mAdapter;
    private RecyclerView mRecyclerView;

    private HomeViewModel homeViewModel;
    private Context mContext;

    private NoteViewModel noteViewModel;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.clicked = false;
        view = inflater.inflate(R.layout.fragment_home, container, false);
        //Initialization animations for FABs
        initAnimElements();
        //Initializations view elements
        initViewElements(view);
        //set adapter
        setAdapter(view);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        noteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);
        noteViewModel.getAllImages().observe(getViewLifecycleOwner(), new Observer<List<Image>>() {
            @Override
            public void onChanged(List<Image> images) {
                for(Image item: images){
                    Log.d("Image List","id " + item.getId());
                    Log.d("Image List","fkNote " + item.getIdFkNote());
                    Log.d("Image List","Uri " + item.getUri());

                }
            }
        });
        fabListeners();
        getAllNotes();
    }

    private void getAllNotes() {

        homeViewModel.getGetAllNotesWithImages().observe(getViewLifecycleOwner(), noteWithImages -> {
            List<Note> noteList = new ArrayList<>();
            List<List<Image>> imagesList = new ArrayList<List<Image>>();
            Log.d("getNotes", "All notes: " + noteWithImages.size());
            for (NoteWithImages itemObject : noteWithImages) {
                noteList.add(itemObject.note);
                imagesList.add(itemObject.imageList);
                Log.d("getNotes", "Note id: " + itemObject.note.getId());
                Log.d("getNotes", "Note title: " + itemObject.note.getTitle());
                Log.d("getNotes", "Note body: " + itemObject.note.getBody());
                Log.d("getNotes","###################################################");
                Log.d("getNotes","Image size: " + itemObject.imageList.size());
                for(Image item: itemObject.imageList){
                    Log.d("getNotes", "# Image id: " + item.getId());
                    Log.d("getNotes", "# Image idFhNote: " + item.getIdFkNote());
                    Log.d("getNotes", "# Image Uri: " + item.getUri());
                }
            }

            mAdapter.setDataAdapter(noteList, imagesList);
            mAdapter.notifyDataSetChanged();
        });
    }

    private void setAdapter(View view) {
        mRecyclerView = view.findViewById(R.id.home_fragment_rv);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new HomeFragmentAdapter(getContext());
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new HomeFragmentAdapter.OnItemClickListener() {
            @Override
            public void onClickItemView(Note note) {
                // Navigation!!!
                Log.d("Id with adapter:", String.valueOf(note.getId()));
                NavDirections action = HomeFragmentDirections.actionHomeFragmentToNoteFragment("", note.getId());
                Navigation.findNavController(view).navigate(action);
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
                homeViewModel.deleteNoteWithImages(mAdapter.getNoteAt(viewHolder.getAdapterPosition()));
                mAdapter.notifyItemRemoved(viewHolder.getLayoutPosition());
                Snackbar.make(view, "Note has been deleted", Snackbar.LENGTH_SHORT).show();

            }
        }).attachToRecyclerView(mRecyclerView);
    }

    private void fabListeners() {
        mFabMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionButtonClicked();
            }
        });

        mFabCreateNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections action = HomeFragmentDirections.actionHomeFragmentToNoteFragment(getResources().getString(R.string.note_key), -1);
                Navigation.findNavController(view).navigate(action);
            }
        });

        mFabCreateReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections action = HomeFragmentDirections.actionHomeFragmentToNoteFragment(getResources().getString(R.string.note_key_remembering), -1);
                Navigation.findNavController(view).navigate(action);
            }
        });
    }

    private void initViewElements(View view) {
        mFabMore = view.findViewById(R.id.fab_more);
        mFabCreateNote = view.findViewById(R.id.fab_two);
        mFabCreateReminder = view.findViewById(R.id.fab_one);
    }

    private void initAnimElements() {
        fromBottomAnim = AnimationUtils.loadAnimation(getContext(), R.anim.from_bottom_anim);
        rotateCloseAnim = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_close_anim);
        rotateOpenAnim = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_open_anim);
        toBottomAnim = AnimationUtils.loadAnimation(getContext(), R.anim.to_bottom_anim);
    }

    private void actionButtonClicked() {
        playAnimation();
        setVisibilityFab();
        setClicked();
        clicked = !clicked;
    }

    private void playAnimation() {
        if (!this.clicked) {
            mFabMore.setAnimation(rotateOpenAnim);
            mFabCreateNote.setAnimation(fromBottomAnim);
            mFabCreateReminder.setAnimation(fromBottomAnim);
        } else {
            mFabMore.setAnimation(rotateCloseAnim);
            mFabCreateNote.setAnimation(toBottomAnim);
            mFabCreateReminder.setAnimation(toBottomAnim);
        }

    }

    private void setVisibilityFab() {
        if (!this.clicked) {
            mFabMore.setVisibility(View.VISIBLE);
            mFabCreateNote.setVisibility(View.VISIBLE);
            mFabCreateReminder.setVisibility(View.VISIBLE);
        } else {
            mFabMore.setVisibility(View.VISIBLE);
            mFabCreateNote.setVisibility(View.INVISIBLE);
            mFabCreateReminder.setVisibility(View.INVISIBLE);
        }


    }

    private void setClicked() {
        if (!this.clicked) {
            mFabCreateNote.setClickable(true);
            mFabCreateReminder.setClickable(true);
        } else {
            mFabCreateNote.setClickable(false);
            mFabCreateReminder.setClickable(false);
        }
    }

}
