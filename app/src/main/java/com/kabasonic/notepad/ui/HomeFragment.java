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
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kabasonic.notepad.R;

public class HomeFragment extends Fragment {

    FloatingActionButton mFabMore, mFabCreateNote, mFabCreateReminder;

    private boolean clicked = false;
    Animation fromBottomAnim, rotateCloseAnim, rotateOpenAnim, toBottomAnim;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.clicked = false;

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        //Initialization animations for FABs
        initAnimElements();
        //Initializations view elements
        initViewElements(view);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mFabMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Button","Click");
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

    private void initViewElements(View view){
        mFabMore = view.findViewById(R.id.fab_more);
        mFabCreateNote = view.findViewById(R.id.fab_two);
        mFabCreateReminder = view.findViewById(R.id.fab_one);
    }

    private void initAnimElements(){
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
