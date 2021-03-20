package com.kabasonic.notepad.ui.dialogs;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.kabasonic.notepad.R;
import com.kabasonic.notepad.ui.NoteFragment;

import java.util.Objects;

public class FilePickerDialogFragment extends DialogFragment implements View.OnClickListener {

    public static final int STORAGE_PERMISSION_CODE = 1;
    public static final int CAMERA_PERMISSION_CODE = 2;

    Activity mActivity;
    Context mContext;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    public interface FilePickerListener {
        void loadImage(int key);
    }

    FilePickerListener mListener;

    public FilePickerDialogFragment(NoteFragment noteFragment, Activity activity) {
        this.mListener = noteFragment;
        this.mActivity = activity;

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_fragment_file_picker, null, false);
        LinearLayout row1 = view.findViewById(R.id.linear_1);
        LinearLayout row2 = view.findViewById(R.id.linear_2);
        row1.setOnClickListener(this);
        row2.setOnClickListener(this);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity())
                .setView(view);
        return alertDialog.create();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.linear_1) {
            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                Log.d(getClass().getSimpleName(), "Permission granted");
                mListener.loadImage(0);
            } else {
                Log.d(getClass().getSimpleName(), "Permission denied");
                requestCameraPermission();
            }
        } else if (v.getId() == R.id.linear_2) {

            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.d(getClass().getSimpleName(), "Permission granted");
                mListener.loadImage(1);
            } else {
                Log.d(getClass().getSimpleName(), "Permission denied");
                requestStoragePermission();
            }
        }
        Objects.requireNonNull(getDialog()).dismiss();
    }

    private void requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this.mActivity,
                Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(mActivity,
                    new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        } else {
            ActivityCompat.requestPermissions(this.mActivity,
                    new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        }
    }

    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this.mActivity,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(mActivity,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        } else {
            ActivityCompat.requestPermissions(this.mActivity,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(getClass().getSimpleName(), "Permission granted STORAGE");
            } else {
                Log.d(getClass().getSimpleName(), "Permission denied STORAGE");
            }
        } else if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(getClass().getSimpleName(), "Permission granted CAMERA");
            } else {
                Log.d(getClass().getSimpleName(), "Permission denied CAMERA");
            }
        }
    }
}

