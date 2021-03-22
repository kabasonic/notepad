package com.kabasonic.notepad.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kabasonic.notepad.R;
import com.kabasonic.notepad.ui.adapters.HomeFragmentAdapter;
import com.kabasonic.notepad.ui.adapters.Note;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    FloatingActionButton mFabMore, mFabCreateNote, mFabCreateReminder;
    private boolean clicked = false;
    Animation fromBottomAnim, rotateCloseAnim, rotateOpenAnim, toBottomAnim;

    HomeFragmentAdapter mAdapter;
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.clicked = false;

        View view = inflater.inflate(R.layout.fragment_home, container, false);
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

        mFabMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Button", "Click");
                Log.d("Boolean", String.valueOf(clicked));
                actionButtonClicked();
            }
        });

        mFabCreateNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections action = HomeFragmentDirections.actionHomeFragmentToNoteFragment(getResources().getString(R.string.note_key));
                Navigation.findNavController(view).navigate(action);
            }
        });

        mFabCreateReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections action = HomeFragmentDirections.actionHomeFragmentToNoteFragment(getResources().getString(R.string.note_key_remembering));
                Navigation.findNavController(view).navigate(action);
            }
        });
    }

    private void setAdapter(View view) {
        ArrayList<Note> exampleList = new ArrayList<>();
        ArrayList<Bitmap> exampleImage = new ArrayList<>();
        String title = getResources().getString(R.string.lorem_ipsum);
        String body = getResources().getString(R.string.lorem_ipsum_body);

        Bitmap bitmapTest = BitmapFactory.decodeResource(getContext().getResources(),
                R.drawable.test_image);

        for(int i=0;i<4;i++){
            exampleImage.add(bitmapTest);
        }

        exampleList.add(new Note(title, body));
        exampleList.add(new Note(title, body));
        exampleList.add(new Note(title, body, exampleImage, exampleImage.size()));
        exampleList.add(new Note(title, body, exampleImage, exampleImage.size()));


        mRecyclerView = view.findViewById(R.id.home_fragment_rv);
        mRecyclerView.setHasFixedSize(true);
        //mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false);
        mAdapter = new HomeFragmentAdapter(getContext(), exampleList);
        //mRecyclerView.setLayoutManager(mLayoutManager);
/*
//         here the adapter view type changes. get type view with SharedPreferences
//        private int getTypeDisplayingView(){
//            SharedPreferences sharedPref = getContext().getSharedPreferences(getResources().getString(R.string.shared_preferences_notepad), MODE_PRIVATE);
//            return (sharedPref.getInt(getResources().getString(R.string.saved_displaying_elements), 0));
//        }
 */
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        mRecyclerView.setAdapter(mAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.RIGHT
                        | ItemTouchHelper.UP
                        | ItemTouchHelper.DOWN
                        | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                /*     DOES NOT WORK

                final int fromPosition = viewHolder.getAdapterPosition();
                final int toPosition = viewHolder.getAdapterPosition();
//                move item in `fromPos` to `toPos` in adapter.
//                return true if moved, false otherwise
                Collections.swap(mAdapter.getmRowItem(),fromPosition,toPosition);
                mRecyclerView.getAdapter().notifyItemMoved(fromPosition,toPosition);
                */
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                mAdapter.notifyItemRemoved(viewHolder.getLayoutPosition());
                // delete with BD
            }
        }).attachToRecyclerView(mRecyclerView);
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
