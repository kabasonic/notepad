package com.kabasonic.notepad.ui.note;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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
import com.kabasonic.notepad.data.db.NoteWithImages;
import com.kabasonic.notepad.data.model.Image;
import com.kabasonic.notepad.data.model.Note;
import com.kabasonic.notepad.ui.adapters.ImageNoteFragmentAdapter;
import com.kabasonic.notepad.ui.dialogs.ColorPickerDialogFragment;
import com.kabasonic.notepad.ui.dialogs.FilePickerDialogFragment;
import com.kabasonic.notepad.ui.reminder.ReminderDialogFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NoteFragment extends Fragment implements ColorPickerDialogFragment.OnClickColorPickerListener, FilePickerDialogFragment.OnClickFilePickerListener, ReminderDialogFragment.OnClickReminderListener {

    public static final int CAMERA_PICK_CODE = 0;
    public static final int STORAGE_PICK_CODE = 1;

    private Context mContext;
    private View view;
    private BottomNavigationView bottomNavigationView;
    private LinearLayout linearLayout;
    private FragmentManager fm;
    private EditText fieldBody, fieldTitle;
    private RecyclerView mRecyclerView;
    private ImageNoteFragmentAdapter mAdapter;
    private Chip chipAlarm;
    private TextView lastChange;

    private NoteFragmentArgs getFragmentArguments;
    private NoteViewModel noteViewModel;
    private Note currentNote;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_note, container, false);
        fm = getActivity().getSupportFragmentManager();
        noteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);
        //associate view elements with NoteFragment.class
        setViewElements();
        setHasOptionsMenu(true);
        setIncomingArguments();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setChipAlarmListener();
        setBottomNavigationListener();
        buildAdapterForImages(view);
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
                // Delete note
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
        if (getFragmentArguments.getNoteId() == -1 && (!getTitleView().isEmpty() || !getBodyView().isEmpty())) {
            //Insert Note
            noteViewModel.insertNoteWithImages(new NoteWithImages(getNoteValuesFromView(), getImageValuesFromView()));
            Snackbar.make(view, "Note saved.", Snackbar.LENGTH_SHORT).show();
        } else if (getFragmentArguments.getNoteId() >= 0) {
            if (!getTitleView().isEmpty() || !getBodyView().isEmpty()) {
                //Update Note
                Snackbar.make(view, "Note updated.", Snackbar.LENGTH_SHORT).show();
            } else if (getTitleView().isEmpty() && getBodyView().isEmpty()) {
                //Delete note
                Snackbar.make(view, "Empty note deleted.", Snackbar.LENGTH_SHORT).show();
            }
        }

        super.onDestroyView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (data != null) {
            if (requestCode == CAMERA_PICK_CODE && resultCode == Activity.RESULT_OK) {
                Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                //mAdapter.addImageToList(selectedImage);
                //Add image to database and update Adapter
                //Need save image because we not have uri with camera
            } else if (requestCode == STORAGE_PICK_CODE && resultCode == Activity.RESULT_OK) {
                Uri imageUri = data.getData();
                // Add to database image to database and update Adapter
                mAdapter.addImageToList(imageUri.toString());
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    private void setViewElements() {
        bottomNavigationView = view.findViewById(R.id.bottom_navigation_create_note);
        linearLayout = view.findViewById(R.id.layout_create_note);
        fieldBody = view.findViewById(R.id.edit_body_text_note);
        fieldTitle = view.findViewById(R.id.edit_title_text_note);
        chipAlarm = view.findViewById(R.id.alarm_chip);
        lastChange = view.findViewById(R.id.last_change);
    }

    @SuppressLint("NonConstantResourceId")
    private void setBottomNavigationListener() {
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_menu_1:
                    DialogFragment dialogColorPicker = new ColorPickerDialogFragment(NoteFragment.this);
                    dialogColorPicker.show(fm, "color_picker");
                    break;
                case R.id.nav_menu_2:
                    DialogFragment dialogReminder = new ReminderDialogFragment(NoteFragment.this);
                    dialogReminder.show(fm, "reminder_dialog_fragment");
                    break;
                case R.id.nav_menu_3:
                    DialogFragment dialogFilePicker = new FilePickerDialogFragment(NoteFragment.this, getActivity());
                    dialogFilePicker.show(fm, "file_picker");
                    break;
                case R.id.nav_menu_4:
                    Snackbar.make(view, "Currently under development.", Snackbar.LENGTH_LONG).show();
                    break;
                case R.id.nav_menu_5:
                    Snackbar.make(view, "Note has been added to favorite.", Snackbar.LENGTH_LONG).show();
                    break;
            }
            return true;
        });
    }

    private void buildAdapterForImages(View view) {
        mRecyclerView = view.findViewById(R.id.note_image_rv);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        mAdapter = new ImageNoteFragmentAdapter(mContext);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        setImageAdapterOnClickListener();
    }

    private void setVisibilityImages() {
        if (mAdapter.getItemCount() != 0) {
            mRecyclerView.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setVisibility(View.GONE);
        }
    }

    //#listeners start
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

    private void setImageAdapterOnClickListener() {
        mAdapter.setOnItemClickListener(new ImageNoteFragmentAdapter.OnItemClickListener() {
            @Override
            public void onItemClickDeleteImage(int position) {
                Log.d(getClass().getSimpleName(), "DELETE POSITION: " + position);
                new AlertDialog.Builder(mContext)
                        .setTitle("Delete image")
                        .setMessage("Are you sure you want to delete the selected image?")
                        .setPositiveButton("YES", (dialog, which) -> {
                            //Delete image with database

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
    //#listeners end

    private void setIncomingArguments() {
        String key = getResources().getString(R.string.note_key_remembering);
        assert getArguments() != null;
        getFragmentArguments = NoteFragmentArgs.fromBundle(getArguments());
        if (getFragmentArguments.getTypeNote().equals(key) && getFragmentArguments.getNoteId() == -1) {
            //Create remembering window
            DialogFragment dialogReminder = new ReminderDialogFragment(NoteFragment.this);
            dialogReminder.show(fm, "reminder_dialog_fragment");
        } else {
            createObjectCurrentNote();
        }
    }

    private void createObjectCurrentNote() {
        if (getFragmentArguments.getNoteId() != -1 && getFragmentArguments.getNoteId() >= 0) {
            this.currentNote = new Note();
            noteViewModel.getNoteWithImages(getFragmentArguments.getNoteId()).observe(getViewLifecycleOwner(), noteWithImages -> {
               List<String> imageList = new ArrayList<>();
                this.currentNote.setId(noteWithImages.note.getId());
                this.currentNote.setTitle(noteWithImages.note.getTitle());
                this.currentNote.setBody(noteWithImages.note.getBody());
                this.currentNote.setBackgroundColor(noteWithImages.note.getBackgroundColor());
                this.currentNote.setLastTimeUpdate(noteWithImages.note.getLastTimeUpdate());
                for (int i=0;i<noteWithImages.imageList.size();i++) {
                    imageList.add(noteWithImages.imageList.get(i).getUri());
                    Log.d("ImageList","item: " + imageList.get(i));
                }
                mAdapter.setImagesToList(imageList);
                mAdapter.notifyDataSetChanged();
                setVisibilityImages();
                setValuesToView();
            });
        } else {
            this.currentNote = new Note();
        }
    }

    private void setValuesToView() {
        fieldTitle.setText(this.currentNote.getTitle());
        fieldBody.setText(this.currentNote.getBody());
        linearLayout.setBackgroundColor(this.currentNote.getBackgroundColor());
        lastChange.setText(getNoteCurrentDate(this.currentNote.getLastTimeUpdate()));
    }

    private List<Image> getImageValuesFromView() {
        List<Image> imageList = new ArrayList<>();
        for (int i = 0; i < mAdapter.getAllImageList().size(); i++) {
            imageList.add(new Image(mAdapter.getAllImageList().get(i)));
        }
        return imageList;
    }

    private Note getNoteValuesFromView() {
        return new Note(
                fieldTitle.getText().toString(),
                fieldBody.getText().toString(),
                this.currentNote.getBackgroundColor(),
                new Date().getTime()
        );
    }

    private String getTitleView() {
        return fieldTitle.getText().toString();
    }

    private String getBodyView() {
        return fieldBody.getText().toString();
    }

    private String getNoteCurrentDate(long value) {
        Date date = new Date(value);
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm dd/MM/yyyy");
        return "Last time update: " + dateFormat.format(date);

    }

    // interface methods
    @Override
    public void setSelectedColor(int color) {
        linearLayout.setBackgroundColor(color);
        this.currentNote.setBackgroundColor(color);
    }

    @Override
    public void imageLoader(int key) {
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
}

