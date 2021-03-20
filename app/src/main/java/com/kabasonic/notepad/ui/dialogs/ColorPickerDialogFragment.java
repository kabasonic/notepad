package com.kabasonic.notepad.ui.dialogs;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.kabasonic.notepad.R;
import com.kabasonic.notepad.ui.NoteFragment;

import java.util.Objects;

public class ColorPickerDialogFragment extends DialogFragment implements DialogInterface.OnClickListener, View.OnClickListener {

    private View view;

    public interface ColorPickerListener {
        void selectedColor(int color);

    }

    ColorPickerListener mListener;

    public ColorPickerDialogFragment(NoteFragment noteFragment){
        this.mListener = (ColorPickerListener) noteFragment;
    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater layoutInflater = getLayoutInflater();
        view = layoutInflater.inflate(R.layout.dialog_fragment_color_picker, null);
        initViewElements();
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle(R.string.color_picker)
                .setNegativeButton(R.string.cancel, this);
        return alertBuilder.create();
    }

    private void initViewElements() {
        View color1 = (View) view.findViewById(R.id.color_1);
        View color2 = (View) view.findViewById(R.id.color_2);
        View color3 = (View) view.findViewById(R.id.color_3);
        View color4 = (View) view.findViewById(R.id.color_4);
        View color5 = (View) view.findViewById(R.id.color_5);
        View color6 = (View) view.findViewById(R.id.color_6);

        color1.setOnClickListener(this);
        color2.setOnClickListener(this);
        color3.setOnClickListener(this);
        color4.setOnClickListener(this);
        color5.setOnClickListener(this);
        color6.setOnClickListener(this);
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which == Dialog.BUTTON_NEGATIVE) {
            Log.d("Color View: ", "BUTTON_NEGATIVE");
            dialog.dismiss();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.color_1:
                mListener.selectedColor(ContextCompat.getColor(getContext(), R.color.colorLightBlue));
                break;
            case R.id.color_2:
                mListener.selectedColor(ContextCompat.getColor(getContext(), R.color.colorLightGreen));
                break;
            case R.id.color_3:
                mListener.selectedColor(ContextCompat.getColor(getContext(), R.color.colorOrange));
                break;
            case R.id.color_4:
                mListener.selectedColor(ContextCompat.getColor(getContext(), R.color.colorRed));
                break;
            case R.id.color_5:
                mListener.selectedColor(ContextCompat.getColor(getContext(), R.color.colorYellow));
                break;
            case R.id.color_6:
                mListener.selectedColor(ContextCompat.getColor(getContext(), R.color.colorPurple));
                break;
        }
        Objects.requireNonNull(getDialog()).dismiss();
    }

}
