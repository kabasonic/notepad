package com.kabasonic.notepad.ui.note;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
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
import com.kabasonic.notepad.data.db.NoteWithTasks;
import com.kabasonic.notepad.data.model.Image;
import com.kabasonic.notepad.data.model.Note;
import com.kabasonic.notepad.data.model.Task;
import com.kabasonic.notepad.ui.adapters.ImageNoteFragmentAdapter;
import com.kabasonic.notepad.ui.adapters.TaskAdapter;
import com.kabasonic.notepad.ui.dialogs.ColorPickerDialogFragment;
import com.kabasonic.notepad.ui.dialogs.FilePickerDialogFragment;
import com.kabasonic.notepad.ui.reminder.AlarmManagerBroadcastReceiver;
import com.kabasonic.notepad.ui.reminder.ReminderDialogFragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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

    private RecyclerView mTaskRecyclerView;
    private TaskAdapter mTaskAdapter;
    private EditText fieldTask;
    private ImageButton addTaskToList;
    private List<Task> taskList = new ArrayList<>();
    private LinearLayout taskLayout;

    private NoteFragmentArgs getFragmentArguments;
    private NoteViewModel noteViewModel;
    private Note currentNote;
    private boolean deleteNote = false;

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
        buildAdapterForImages();
        buildTaskListAdapter();
        addNewTask();


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
                share();
                break;
            case R.id.delete_note:
                // Delete note
                this.deleteNote = true;
                NavDirections action = NoteFragmentDirections.actionNoteFragmentToHomeFragment();
                Navigation.findNavController(view).navigate(action);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void share() {
        Intent myIntent = new Intent(Intent.ACTION_SEND);
        myIntent.setType("text/plain");
        String shareBody = getResources().getString(R.string.app_name) + "\nTitle: " + fieldTitle.getText().toString() + "\nText: " + fieldBody.getText().toString();
        myIntent.putExtra(Intent.EXTRA_SUBJECT, shareBody);
        myIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(myIntent, "Share"));
    }

    @Override
    public void onDestroyView() {
        if (this.deleteNote) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyy hh:mm");
            Date date = new Date(System.currentTimeMillis());
            NoteWithImages noteWithImages = new NoteWithImages(getNoteValuesFromView(), mAdapter.getImageList());
            noteWithImages.note.setId(this.currentNote.getId());
            noteWithImages.note.setDeletedAt(sdf.format(date));
            noteViewModel.updateNoteWithImages(noteWithImages);
            Snackbar.make(view, "The note was moved to the trash.", Snackbar.LENGTH_SHORT).show();
        } else if (getFragmentArguments.getNoteId() == -1 && (!getTitleView().isEmpty() || !getBodyView().isEmpty()) || mAdapter.getItemCount() != 0) {
            //Insert Note
            noteViewModel.insertNoteWithImages(new NoteWithImages(getNoteValuesFromView(), mAdapter.getImageList()));

            if (this.currentNote.isList()) {
                noteViewModel.insertNoteWithTask(new NoteWithTasks(getNoteValuesFromView(), mTaskAdapter.getmItemList()));
            }

            Snackbar.make(view, "Note saved.", Snackbar.LENGTH_SHORT).show();
        } else if (getFragmentArguments.getNoteId() >= 0) {
            if (!getTitleView().isEmpty() || !getBodyView().isEmpty()) {
                //Update Note

                Note note = getNoteValuesFromView();
                note.setId(getFragmentArguments.getNoteId());
                noteViewModel.updateNoteWithImages(new NoteWithImages(note, mAdapter.getImageList()));
                if (this.currentNote.isList()) {
                    noteViewModel.updateTask(new NoteWithTasks(note, mTaskAdapter.getmItemList()));
                }
                //Snackbar.make(view, "Note updated.", Snackbar.LENGTH_SHORT).show();
            } else if (getTitleView().isEmpty() && getBodyView().isEmpty()) {
                //Delete note
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyy hh:mm");
                Date date = new Date(System.currentTimeMillis());
                NoteWithImages noteWithImages = new NoteWithImages(getNoteValuesFromView(), mAdapter.getImageList());
                noteWithImages.note.setId(this.currentNote.getId());
                noteWithImages.note.setDeletedAt(sdf.format(date));
                noteViewModel.updateNoteWithImages(noteWithImages);
                Snackbar.make(view, "The note was moved to the trash.", Snackbar.LENGTH_SHORT).show();
            }
        }

        super.onDestroyView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (data != null) {
            if (requestCode == CAMERA_PICK_CODE && resultCode == Activity.RESULT_OK) {
                Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                mAdapter.addImageToList(new Image(saveImage(selectedImage)));
                setVisibilityImages();
                mAdapter.notifyDataSetChanged();
            } else if (requestCode == STORAGE_PICK_CODE && resultCode == Activity.RESULT_OK) {
                Uri imageUri = data.getData();
                // Add to database image to database and update Adapter
                mAdapter.addImageToList(new Image(imageUri.toString()));
                setVisibilityImages();
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    private void setViewElements() {
        mRecyclerView = view.findViewById(R.id.note_image_rv);
        bottomNavigationView = view.findViewById(R.id.bottom_navigation_create_note);
        linearLayout = view.findViewById(R.id.layout_create_note);
        fieldBody = view.findViewById(R.id.edit_body_text_note);
        fieldTitle = view.findViewById(R.id.edit_title_text_note);
        chipAlarm = view.findViewById(R.id.alarm_chip);
        lastChange = view.findViewById(R.id.last_change);

        mTaskRecyclerView = view.findViewById(R.id.rv_task_note);
        fieldTask = view.findViewById(R.id.edit_text_row_task);
        addTaskToList = view.findViewById(R.id.add_row_task);
        taskLayout = view.findViewById(R.id.task_layout);
    }

    @SuppressLint("NonConstantResourceId")
    private void setBottomNavigationListener() {
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_menu_1:
                    DialogFragment dialogColorPicker = new ColorPickerDialogFragment(NoteFragment.this);
                    dialogColorPicker.show(fm, "color_picker");
                    break;
//                case R.id.nav_menu_2:
                    //In development
//                    DialogFragment dialogReminder = new ReminderDialogFragment(NoteFragment.this);
//                    dialogReminder.show(fm, "reminder_dialog_fragment");
//                    break;
                case R.id.nav_menu_3:
                    DialogFragment dialogFilePicker = new FilePickerDialogFragment(NoteFragment.this, getActivity());
                    dialogFilePicker.show(fm, "file_picker");
                    // set reminder!!!
                    break;
                case R.id.nav_menu_4:
                    if (this.currentNote.isList()) {
                        Snackbar.make(view, "Converted to note.", Snackbar.LENGTH_SHORT).show();
                        for (Task items : mTaskAdapter.getmItemList()) {
                            Log.d("List", "items: " + items.getBody());
                        }
                        if (mTaskAdapter.getmItemList().isEmpty()) {
                            fieldBody.setText("");
                            this.currentNote.setBody("");
                        }
                        mTaskRecyclerView.setVisibility(View.GONE);
                        taskLayout.setVisibility(View.GONE);
                        fieldBody.setVisibility(View.VISIBLE);

                        noteViewModel.deleteListTasks(new NoteWithTasks(getNoteValuesFromView(), mTaskAdapter.getmItemList()));

                        this.currentNote.setList(false);
                        this.currentNote.setBody(mTaskAdapter.getListToString());
                        this.fieldBody.setText(this.currentNote.getBody());
                    } else {
                        Snackbar snack = Snackbar.make(view, "Converted to list.", Snackbar.LENGTH_SHORT);

                        mTaskAdapter.setmItemList(findNewLine(fieldBody.getText().toString()));
                        mTaskAdapter.notifyDataSetChanged();
                        mTaskRecyclerView.setVisibility(View.VISIBLE);
                        taskLayout.setVisibility(View.VISIBLE);
                        fieldBody.setVisibility(View.GONE);
                        this.currentNote.setList(true);
                    }

                    break;
                case R.id.nav_menu_5:
                    if (this.currentNote.isFavorite()) {
                        this.currentNote.setFavorite(false);
                        Snackbar.make(view, "Note has been deleted from favorite.", Snackbar.LENGTH_SHORT).show();
                    } else {
                        this.currentNote.setFavorite(true);
                        Snackbar.make(view, "Note has been added to favorite.", Snackbar.LENGTH_SHORT).show();
                    }
                    break;
            }
            return true;
        });
    }

    private void addNewTask() {
        addTaskToList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!fieldTask.getText().toString().isEmpty()) {
                    List<Task> tempList = mTaskAdapter.getmItemList();
                    tempList.add(new Task(fieldTask.getText().toString(), false));
                    mTaskAdapter.setmItemList(tempList);
                    mTaskRecyclerView.scrollToPosition(mTaskRecyclerView.getAdapter().getItemCount() - 1);
                    mTaskAdapter.notifyDataSetChanged();
                    updateBody();
                    fieldTask.setText("");
                }

            }
        });
    }

    private void updateBody() {
        String text = fieldBody.getText().toString() + "\n" + fieldTask.getText().toString();
        fieldBody.setText(text);
    }


    private void buildTaskListAdapter() {
        mTaskRecyclerView.setHasFixedSize(false);
        mTaskAdapter = new TaskAdapter(mContext);
        mTaskRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mTaskRecyclerView.setAdapter(mTaskAdapter);

        mTaskAdapter.setOnItemClickListener(new TaskAdapter.OnClickListener() {
            @Override
            public void removeTask(int position) {
                Task task = mTaskAdapter.getmItemList().get(position);
                noteViewModel.deleteTask(task);
                mTaskAdapter.removeTaskWithList(position);
                mTaskAdapter.notifyItemRemoved(position);
                if (mTaskAdapter.getmItemList().isEmpty()) {
                    fieldBody.setText("");
                }
            }

            @Override
            public void fieldTask(int position, String changedText) {
                List<Task> list = mTaskAdapter.getmItemList();
                list.set(position, new Task(changedText, list.get(position).isCompletedTask()));
                mTaskAdapter.setmItemList(list);
                mTaskAdapter.notifyDataSetChanged();
            }

            @Override
            public void checkBoxTask(int position, boolean isChecked) {
                List<Task> list = mTaskAdapter.getmItemList();
                Task task = list.get(position);
                task.setId(list.get(position).getId());
                task.setCompletedTask(isChecked);
                list.set(position, task);
                mTaskAdapter.setmItemList(list);
                mTaskAdapter.notifyDataSetChanged();
            }
        });
    }

    private void buildAdapterForImages() {
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
                    AlarmManagerBroadcastReceiver alarmManagerBroadcastReceiver = new AlarmManagerBroadcastReceiver();
                    alarmManagerBroadcastReceiver.cancelAlarm(mContext);
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

                            noteViewModel.deleteNoteWithImages(mAdapter.getAtImageItem(position));
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
                LayoutInflater layoutInflater = getLayoutInflater();
                View view = layoutInflater.inflate(R.layout.layout_dialog_image, null);
                ImageView imageDialog = view.findViewById(R.id.dialog_image);
                Uri uriImage = Uri.parse(mAdapter.getAtImageItem(position).getUri());
                imageDialog.setImageURI(uriImage);
                new AlertDialog.Builder(mContext).setView(view).create().show();
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
                this.currentNote.setId(noteWithImages.note.getId());
                this.currentNote.setTitle(noteWithImages.note.getTitle());
                this.currentNote.setBody(noteWithImages.note.getBody());
                this.currentNote.setBackgroundColor(noteWithImages.note.getBackgroundColor());
                this.currentNote.setLastTimeUpdate(noteWithImages.note.getLastTimeUpdate());
                this.currentNote.setFavorite(noteWithImages.note.isFavorite());
                this.currentNote.setDeletedAt(noteWithImages.note.getDeletedAt());
                this.currentNote.setReminderIsSet(noteWithImages.note.getReminderIsSet());
                this.currentNote.setList(noteWithImages.note.isList());

                mAdapter.setImageFromBD(noteWithImages.imageList);
                mAdapter.notifyDataSetChanged();
                setVisibilityImages();
                setValuesToView();

                bottomNavigationView.setBackgroundColor(noteWithImages.note.getBackgroundColor());

                if (this.currentNote.isList()) {
                    noteViewModel.getNoteWithTasks(getFragmentArguments.getNoteId()).observe(getViewLifecycleOwner(), new Observer<NoteWithTasks>() {
                        @Override
                        public void onChanged(NoteWithTasks noteWithTasks) {
                            mTaskAdapter.setmItemList(noteWithTasks.taskList);
                            mTaskAdapter.notifyDataSetChanged();
                        }
                    });
                    mTaskRecyclerView.setVisibility(View.VISIBLE);
                    taskLayout.setVisibility(View.VISIBLE);
                    fieldBody.setVisibility(View.GONE);
                    this.currentNote.setList(true);
                }

            });
        } else {
            this.currentNote = new Note();
            taskLayout.setVisibility(View.GONE);
            mTaskRecyclerView.setVisibility(View.GONE);
        }
    }

    private void setValuesToView() {
        fieldTitle.setText(this.currentNote.getTitle());
        fieldBody.setText(this.currentNote.getBody());
        linearLayout.setBackgroundColor(this.currentNote.getBackgroundColor());
        lastChange.setText(getNoteCurrentDate(this.currentNote.getLastTimeUpdate()));

    }

    private List<Task> findNewLine(String inputString) {
        this.taskList.clear();
        inputString += "\n";
        if (inputString.equals("\n")) {
            return taskList;
        } else {
            char[] chars = inputString.toCharArray();
            String output = "";
            for (int i = 0; i < chars.length; i++) {
                if ((int) chars[i] != 10) {
                    output += chars[i];
                } else if ((int) chars[i] == 10) {
                    if (!output.equals("")) {
                        this.taskList.add(new Task(output, false));
                    }
                    output = "";
                }
            }
        }
        return taskList;
    }

    private Note getNoteValuesFromView() {
        return new Note(
                fieldTitle.getText().toString(),
                fieldBody.getText().toString(),
                new Date().getTime(),
                this.currentNote.isFavorite(),
                this.currentNote.getReminderIsSet(),
                this.currentNote.getDeletedAt(),
                this.currentNote.getBackgroundColor(),
                this.currentNote.isList()
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
        bottomNavigationView.setBackgroundColor(color);
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
        // Create system alarm
        chipAlarm.setText(textAlarmChip);
        chipAlarm.setVisibility(View.VISIBLE);
    }

    private String saveImage(Bitmap bitmap) {

        FileOutputStream outStream = null;
        File sdCard = Environment.getExternalStorageDirectory();
        File dir = new File(sdCard.getAbsolutePath() + "/" + R.string.app_name);
        dir.mkdirs();
        String fileName = String.format("%d.jpg", System.currentTimeMillis());
        File outFile = new File(dir, fileName);
        try {
            outStream = new FileOutputStream(outFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
        try {
            outStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            outStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        File path = new File(dir + "/" + fileName);
        return path.getPath();
    }

}

