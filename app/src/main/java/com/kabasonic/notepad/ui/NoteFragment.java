package com.kabasonic.notepad.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.kabasonic.notepad.R;
import com.kabasonic.notepad.ui.reminder.ReminderDialogFragment;

public class NoteFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note, container, false);
        setHasOptionsMenu(true);
        actionArgument();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    private void actionArgument() {
        String key = getResources().getString(R.string.note_key_remembering);
        NoteFragmentArgs noteFragmentArgs = NoteFragmentArgs.fromBundle(getArguments());
        if(noteFragmentArgs.getTypeNote()== key){
            createRememberingWindow();
        }

    }

    private void createRememberingWindow() {

        FragmentManager fm = getActivity().getSupportFragmentManager();
        ReminderDialogFragment reminderDialogFragment = ReminderDialogFragment.newInstance();
        reminderDialogFragment.show(fm,"reminderDialogFragment");

//        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
//        LayoutInflater inflater = this.getLayoutInflater();
//        View dialogView = inflater.inflate(R.layout.dialog_remembering_note, null);
//        dialogBuilder.setView(dialogView);
//
//        TextView dateReminder = (TextView) dialogView.findViewById(R.id.day_reminder);
//        TextView timeReminder = (TextView) dialogView.findViewById(R.id.time_reminder);
//        Spinner spinnerReminder = (Spinner) dialogView.findViewById(R.id.spinner_reminder);
//        SwitchMaterial switchReminder = (SwitchMaterial) dialogView.findViewById(R.id.set_switch_reminder);
//
//        dateReminder.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.i("Window Reminder", "Set date:" );
//            }
//        });
//
//        timeReminder.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.i("Window Reminder", "Set time:" );
//            }
//        });
//        /*
//            ERROR Don't call setOnClickListener for an AdapterView. You probably want setOnItemClickListener instead
//            ERROR setOnItemClickListener cannot be used with a spinner.
//            :(
//         */
////        spinnerReminder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
////            @Override
////            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
////                Log.i("Window Reminder", "Do not repeat:" );
////            }
////        });
//
//        dialogBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {}
//        });
//        dialogBuilder.setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                //Write date Window
//                Log.i("Window Reminder", "Set date:" );
//                Log.i("Window Reminder", "Set time:" );
//                Log.i("Window Reminder", "Do not repeat:" );
//                Log.i("Window Reminder", "Show text: " + String.valueOf(switchReminder.isChecked()));
//            }
//        });
//
//        AlertDialog alertDialog = dialogBuilder.create();
//        alertDialog.show();
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
}
