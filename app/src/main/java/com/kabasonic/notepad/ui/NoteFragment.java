package com.kabasonic.notepad.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.kabasonic.notepad.R;
import com.kabasonic.notepad.ui.adapters.ImageItem;
import com.kabasonic.notepad.ui.adapters.ImageNoteFragmentAdapter;
import com.kabasonic.notepad.ui.dialogs.ColorPickerDialogFragment;
import com.kabasonic.notepad.ui.dialogs.FilePickerDialogFragment;
import com.kabasonic.notepad.ui.reminder.ReminderDialogFragment;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class NoteFragment extends Fragment implements ColorPickerDialogFragment.ColorPickerListener, FilePickerDialogFragment.FilePickerListener {

    public static final int CAMERA_PICK_CODE = 0;
    public static final int STORAGE_PICK_CODE = 1;
    Context mContext;
    View view;
    BottomNavigationView bottomNavigationView;
    LinearLayout linearLayout;
    FragmentManager fm;
    ImageView imageView;
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    ImageNoteFragmentAdapter mAdapter;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_note, container, false);
        bottomNavigationView = view.findViewById(R.id.bottom_navigation_create_note);
        linearLayout = view.findViewById(R.id.layout_create_note);
        fm = getActivity().getSupportFragmentManager();
        imageView = view.findViewById(R.id.testImage);


        setHasOptionsMenu(true);
        actionArgument();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navBottomListener();
        setImageAdapter(view);
    }

    private void actionArgument() {
        String key = getResources().getString(R.string.note_key_remembering);
        NoteFragmentArgs noteFragmentArgs = NoteFragmentArgs.fromBundle(getArguments());
        if (noteFragmentArgs.getTypeNote().equals(key)) {
            //Create remembering window
            ReminderDialogFragment reminderDialogFragment = ReminderDialogFragment.newInstance();
            reminderDialogFragment.show(fm, "reminder_dialog_fragment");
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
                        dialogColorPicker.show(fm, "color_picker");
                        break;
                    case R.id.nav_menu_2:
                        Log.d("Navigation", "Menu 2");
                        DialogFragment dialogReminder = new ReminderDialogFragment();
                        dialogReminder.show(fm, "reminder_dialog_fragment");
                        break;
                    case R.id.nav_menu_3:
                        Log.d("Navigation", "Menu 3");
                        DialogFragment dialogFilePicker = new FilePickerDialogFragment(NoteFragment.this, getActivity());
                        dialogFilePicker.show(fm, "file_picker");
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

    private void setImageAdapter(View view) {
        ArrayList<ImageItem> exampleList = new ArrayList<>();

        mRecyclerView = view.findViewById(R.id.note_image_rv);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        mAdapter = new ImageNoteFragmentAdapter(mContext, exampleList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new ImageNoteFragmentAdapter.OnItemClickListener() {
            @Override
            public void onItemClickDeleteImage(int position) {
                Log.d(getClass().getSimpleName(), "DELETE POSITION: " + position);
                new AlertDialog.Builder(mContext)
                        .setTitle("Delete image")
                        .setMessage("Are you sure you want to delete the selected image?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mAdapter.deleteImageItem(position);
                                mAdapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {}
                        })
                        .create()
                        .show();
            }
            @Override
            public void onItemClickPickImage(int position) {
                // Open image in new window
            }
        });
    }

    @Override
    public void selectedColor(int color) {
        linearLayout.setBackgroundColor(color);
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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.d(getClass().getSimpleName(), "onActivityResult");
        if (requestCode == CAMERA_PICK_CODE && resultCode == Activity.RESULT_OK) {
            Log.d(getClass().getSimpleName(), "Image with camera");
            if (data != null) {
                Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                mAdapter.addImage(selectedImage);
                mAdapter.notifyDataSetChanged();
            }
        }
        if (requestCode == STORAGE_PICK_CODE && resultCode == Activity.RESULT_OK) {
            Log.d(getClass().getSimpleName(), "Image with storage");
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


}

