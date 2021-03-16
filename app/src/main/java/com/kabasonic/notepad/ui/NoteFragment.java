package com.kabasonic.notepad.ui;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.kabasonic.notepad.R;
import com.kabasonic.notepad.ui.dialogs.ColorPickerDialogFragment;
import com.kabasonic.notepad.ui.reminder.ReminderDialogFragment;

public class NoteFragment extends Fragment implements ColorPickerDialogFragment.ColorPickerListener{
    View view;
    BottomNavigationView bottomNavigationView;
    LinearLayout linearLayout;
    FragmentManager fm;
    private int i;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_note, container, false);
        bottomNavigationView = view.findViewById(R.id.bottom_navigation_create_note);
        linearLayout = (LinearLayout) view.findViewById(R.id.layout_create_note);
        fm = getActivity().getSupportFragmentManager();

        setHasOptionsMenu(true);
        actionArgument();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navBottomListener();
    }

    private void actionArgument() {
        String key = getResources().getString(R.string.note_key_remembering);
        NoteFragmentArgs noteFragmentArgs = NoteFragmentArgs.fromBundle(getArguments());
        if (noteFragmentArgs.getTypeNote().equals(key)){
            //Create remembering window
            ReminderDialogFragment reminderDialogFragment = ReminderDialogFragment.newInstance();
            reminderDialogFragment.show(fm, "reminderDialogFragment");
        }

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.note_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }



    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.change_color_bg:
                linearLayout.setBackgroundColor(Color.BLACK);
                break;
            case R.id.share_note:
                break;
            case R.id.delete_note:
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void navBottomListener() {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_menu_1:
                        Log.d("Navigation", "Menu 1");
                        //openColorDialog();
                        DialogFragment dialogColorPicker = new ColorPickerDialogFragment(NoteFragment.this);
                        dialogColorPicker.show(fm,"color_picker");
                        break;
                    case R.id.nav_menu_2:
                        Log.d("Navigation", "Menu 2");
                        break;
                    case R.id.nav_menu_3:
                        Log.d("Navigation", "Menu 3");
                        break;
                    case R.id.nav_menu_4:
                        Log.d("Navigation", "Menu 4");
                        break;
                    case R.id.nav_menu_5:
                        Log.d("Navigation", "Menu 5");
                        break;
                }
                return true;
            }
        });
    }


    @Override
    public void selectedColor(int color) {
        linearLayout.setBackgroundColor(color);
    }


}

