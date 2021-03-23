package com.kabasonic.notepad.ui;

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
import com.kabasonic.notepad.R;
import com.kabasonic.notepad.data.Note;
import com.kabasonic.notepad.data.NoteViewModel;
import com.kabasonic.notepad.ui.adapters.HomeFragmentAdapter;

import java.util.List;

public class HomeFragment extends Fragment {

    private View view;
    private FloatingActionButton mFabMore, mFabCreateNote, mFabCreateReminder;
    private boolean clicked = false;
    private Animation fromBottomAnim, rotateCloseAnim, rotateOpenAnim, toBottomAnim;

    private HomeFragmentAdapter mAdapter;
    private RecyclerView mRecyclerView;

    private NoteViewModel noteViewModel;

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
        fabListeners();
        getAllNotes();

        noteViewModel.getAllId().observe(getViewLifecycleOwner(), listId -> {

            for (Integer currentNoteId : listId){
                Log.d("Home fragment ID:", String.valueOf(listId));
                }

        });
    }

    private void getAllNotes() {
        noteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);
        noteViewModel.getAllNotes().observe(getViewLifecycleOwner(), new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                mAdapter.setDataAdapter(notes);
            }
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
                noteViewModel.delete(mAdapter.getNoteAt(viewHolder.getAdapterPosition()));
                mAdapter.notifyItemRemoved(viewHolder.getLayoutPosition());
                // delete with BD
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
