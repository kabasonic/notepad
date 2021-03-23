package com.kabasonic.notepad.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.chip.Chip;
import com.google.android.material.snackbar.Snackbar;
import com.kabasonic.notepad.R;
import com.kabasonic.notepad.data.Note;
import com.kabasonic.notepad.data.NoteViewModel;
import com.kabasonic.notepad.ui.adapters.ImageItem;
import com.kabasonic.notepad.ui.adapters.ImageNoteFragmentAdapter;
import com.kabasonic.notepad.ui.dialogs.ColorPickerDialogFragment;
import com.kabasonic.notepad.ui.dialogs.FilePickerDialogFragment;
import com.kabasonic.notepad.ui.reminder.ReminderDialogFragment;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class NoteFragment extends Fragment implements ColorPickerDialogFragment.ColorPickerListener, FilePickerDialogFragment.FilePickerListener, ReminderDialogFragment.DataReminderListener {

    public static final int CAMERA_PICK_CODE = 0;
    public static final int STORAGE_PICK_CODE = 1;

    private Context mContext;
    private View view;
    private BottomNavigationView bottomNavigationView;
    private LinearLayout linearLayout;
    private FragmentManager fm;
    private EditText editBodyText, editTitleText;
    private RecyclerView mRecyclerView;
    private ImageNoteFragmentAdapter mAdapter;
    private Chip chipAlarm;
    private TextView lastChange;

    private NoteFragmentArgs noteFragmentArgs;
    private NoteViewModel noteViewModel;
    private Note currentNote;
    private int color = 0xffffffff;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        noteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);
        view = inflater.inflate(R.layout.fragment_note, container, false);
        bottomNavigationView = view.findViewById(R.id.bottom_navigation_create_note);
        linearLayout = view.findViewById(R.id.layout_create_note);
        fm = getActivity().getSupportFragmentManager();
        editBodyText = view.findViewById(R.id.edit_body_text_note);
        editTitleText = view.findViewById(R.id.edit_title_text_note);
        chipAlarm = view.findViewById(R.id.alarm_chip);
        lastChange = view.findViewById(R.id.last_change);
        setHasOptionsMenu(true);
        getNavigationFragmentArguments();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setChipAlarmListener();
        setBottomNavigationListener();
        buildImageAdapter(view);
    }

    @Override
    public void onResume() {
        super.onResume();
        setVisibilityImages();
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
            case R.id.share_note:
                break;
            case R.id.delete_note:
                Snackbar.make(view, "The note was moved to the trash.", Snackbar.LENGTH_LONG).show();
                deleteNote();
                NavDirections action = NoteFragmentDirections.actionNoteFragmentToHomeFragment();
                Navigation.findNavController(view).navigate(action);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        if(noteFragmentArgs.getNoteId() == -1 && (!getTitle().isEmpty() || !getBody().isEmpty())){
            noteViewModel.insert(new Note(getTitle(),getBody(),getColor()));
            Snackbar.make(view, "Note saved.", Snackbar.LENGTH_SHORT).show();
        }
        if(noteFragmentArgs.getNoteId() >= 0){
                Note noteUpdate = new Note(getTitle(),getBody(),getColor());
                if( !getTitle().isEmpty() || !getBody().isEmpty() ){
                    noteUpdate.setId(noteFragmentArgs.getNoteId());
                    noteViewModel.update(noteUpdate);
                    Snackbar.make(view, "Note updated.", Snackbar.LENGTH_SHORT).show();
                }else if(getTitle().isEmpty() && getBody().isEmpty()){
                    noteViewModel.delete(currentNote);
                    Snackbar.make(view, "Empty note deleted.", Snackbar.LENGTH_SHORT).show();
                }
        }
        super.onDestroyView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CAMERA_PICK_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                mAdapter.addImage(selectedImage);
                mAdapter.notifyDataSetChanged();
            }
        }
        if (requestCode == STORAGE_PICK_CODE && resultCode == Activity.RESULT_OK) {
            try {
                assert data != null;
                final Uri imageUri = data.getData();
                final InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                mAdapter.addImage(selectedImage);
                mAdapter.notifyDataSetChanged();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void selectedColor(int color) {
        linearLayout.setBackgroundColor(color);
        setColor(color);
    }

    @Override
    public void loadImage(int key) {
        Intent intent;
        if (key == 0) {
            intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, CAMERA_PICK_CODE);
        } else if (key == 1) {
            intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, STORAGE_PICK_CODE);
        }
    }

    @Override
    public void dataListener(String date, String time) {
        String textAlarmChip = date + " " + time;
        chipAlarm.setText(textAlarmChip);
        chipAlarm.setVisibility(View.VISIBLE);
    }


    private void getNavigationFragmentArguments() {
        String key = getResources().getString(R.string.note_key_remembering);

        assert getArguments() != null;
        noteFragmentArgs = NoteFragmentArgs.fromBundle(getArguments());

        if (noteFragmentArgs.getTypeNote().equals(key)) {
            //Create remembering window
            DialogFragment dialogReminder = new ReminderDialogFragment(NoteFragment.this);
            dialogReminder.show(fm, "reminder_dialog_fragment");
        }

        if (noteFragmentArgs.getNoteId() >= 0) {
            getNote(noteFragmentArgs.getNoteId());
            setCurrentNote();
        }
    }

    @SuppressLint("NonConstantResourceId")
    private void setBottomNavigationListener() {
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_menu_1:
                    Log.d("Navigation", "Menu 1");
                    //openColorDialog();
                    DialogFragment dialogColorPicker = new ColorPickerDialogFragment(NoteFragment.this);
                    dialogColorPicker.show(fm, "color_picker");
                    break;
                case R.id.nav_menu_2:
                    Log.d("Navigation", "Menu 2");
                    DialogFragment dialogReminder = new ReminderDialogFragment(NoteFragment.this);
                    dialogReminder.show(fm, "reminder_dialog_fragment");
                    break;
                case R.id.nav_menu_3:
                    Log.d("Navigation", "Menu 3");
                    DialogFragment dialogFilePicker = new FilePickerDialogFragment(NoteFragment.this, getActivity());
                    dialogFilePicker.show(fm, "file_picker");
                    break;
                case R.id.nav_menu_4:
                    Log.d("Navigation", "Menu 4");
                    Snackbar.make(view, "Currently under development.", Snackbar.LENGTH_LONG).show();
                    break;
                case R.id.nav_menu_5:
                    Log.d("Navigation", "Menu 5");
                    Snackbar.make(view, "Note has been added to favorite.", Snackbar.LENGTH_LONG).show();
                    break;
            }
            return true;
        });
    }

    private void buildImageAdapter(View view) {
        ArrayList<ImageItem> exampleList = new ArrayList<>();
        mRecyclerView = view.findViewById(R.id.note_image_rv);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        mAdapter = new ImageNoteFragmentAdapter(mContext, exampleList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        setImageAdapterOnClickListener();
    }

    private void setImageAdapterOnClickListener() {
        mAdapter.setOnItemClickListener(new ImageNoteFragmentAdapter.OnItemClickListener() {
            @Override
            public void onItemClickDeleteImage(int position) {
                Log.d(getClass().getSimpleName(), "DELETE POSITION: " + position);
                new AlertDialog.Builder(mContext)
                        .setTitle("Delete image")
                        .setMessage("Are you sure you want to delete the selected image?")
                        .setPositiveButton("YES", (dialog, which) -> {
                            mAdapter.deleteImageItem(position);
                            mAdapter.notifyDataSetChanged();
                        })
                        .setNegativeButton("CANCEL", (dialog, which) -> {/*body*/})
                        .create()
                        .show();
            }

            @Override
            public void onItemClickPickImage(int position) {
                // Open image in new window
            }
        });
    }

    private void deleteNote() {
        noteViewModel.getNoteById(noteFragmentArgs.getNoteId()).observe(getViewLifecycleOwner(), note -> {
            noteViewModel.delete(note);
        });
    }

    private void getNote(int noteId) {
        noteViewModel.getNoteById(noteId).observe(getViewLifecycleOwner(), note -> {
            editTitleText.setText(note.getTitle());
            editBodyText.setText(note.getBody());
            linearLayout.setBackgroundColor(note.getBackgroundColor());
        });

    }

    private void setVisibilityImages() {
        if (mAdapter.getItemCount() != 0) {
            mRecyclerView.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setVisibility(View.GONE);
        }
    }

    private void setChipAlarmListener() {
        chipAlarm.setOnCloseIconClickListener(v -> new AlertDialog.Builder(mContext)
                .setTitle("Reminder")
                .setMessage("Are you sure you want to delete the reminder time?")
                .setPositiveButton("YES", (dialog, which) -> {
                    chipAlarm.setVisibility(View.GONE);
                    Snackbar.make(view, "Alarm reminder has been deleted.", Snackbar.LENGTH_LONG).show();
                })
                .setNegativeButton("NO", (dialog, which) -> {
                })
                .create()
                .show());
    }

    private String getTitle(){
        return editTitleText.getText().toString();
    }

    private String getBody(){
        return editBodyText.getText().toString();
    }

    private int getColor(){
        return this.color;
    }

    private void setColor(int color){
        this.color = color;
    }

    private void setCurrentNote(){
        this.currentNote = new Note();
        noteViewModel.getNoteById(noteFragmentArgs.getNoteId()).observe(getViewLifecycleOwner(), note -> {
            this.currentNote.setId(note.getId());
            this.currentNote.setTitle(note.getTitle());
            this.currentNote.setBody(note.getBody());
            this.currentNote.setBackgroundColor(note.getBackgroundColor());
        });
    }


}

