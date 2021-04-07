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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kabasonic.notepad.R;
import com.kabasonic.notepad.ui.note.NoteFragment;

import java.util.Objects;

public class ColorPickerDialogFragment extends DialogFragment implements DialogInterface.OnClickListener, View.OnClickListener {

    private View view;

    public interface OnClickColorPickerListener {
        void setSelectedColor(int color);
    }

    OnClickColorPickerListener mListener;

    public ColorPickerDialogFragment(NoteFragment noteFragment){
        this.mListener = (OnClickColorPickerListener) noteFragment;
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
        FloatingActionButton color1 = (FloatingActionButton) view.findViewById(R.id.color_1);
        FloatingActionButton color2 = (FloatingActionButton) view.findViewById(R.id.color_2);
        FloatingActionButton color3 = (FloatingActionButton) view.findViewById(R.id.color_3);
        FloatingActionButton color4 = (FloatingActionButton) view.findViewById(R.id.color_4);
        FloatingActionButton color5 = (FloatingActionButton) view.findViewById(R.id.color_5);
        FloatingActionButton color6 = (FloatingActionButton) view.findViewById(R.id.color_6);
        FloatingActionButton color7 = (FloatingActionButton) view.findViewById(R.id.color_7);
        FloatingActionButton color8 = (FloatingActionButton) view.findViewById(R.id.color_8);
        FloatingActionButton color9 = (FloatingActionButton) view.findViewById(R.id.color_9);
        FloatingActionButton color10 = (FloatingActionButton) view.findViewById(R.id.color_10);

        color1.setOnClickListener(this);
        color2.setOnClickListener(this);
        color3.setOnClickListener(this);
        color4.setOnClickListener(this);
        color5.setOnClickListener(this);
        color6.setOnClickListener(this);
        color7.setOnClickListener(this);
        color8.setOnClickListener(this);
        color9.setOnClickListener(this);
        color10.setOnClickListener(this);
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which == Dialog.BUTTON_NEGATIVE) {
            dialog.dismiss();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.color_1:
                mListener.setSelectedColor(ContextCompat.getColor(getContext(), R.color.color_1));
                break;
            case R.id.color_2:
                mListener.setSelectedColor(ContextCompat.getColor(getContext(), R.color.color_2));
                break;
            case R.id.color_3:
                mListener.setSelectedColor(ContextCompat.getColor(getContext(), R.color.color_3));
                break;
            case R.id.color_4:
                mListener.setSelectedColor(ContextCompat.getColor(getContext(), R.color.color_4));
                break;
            case R.id.color_5:
                mListener.setSelectedColor(ContextCompat.getColor(getContext(), R.color.color_5));
                break;
            case R.id.color_6:
                mListener.setSelectedColor(ContextCompat.getColor(getContext(), R.color.color_6));
                break;
            case R.id.color_7:
                mListener.setSelectedColor(ContextCompat.getColor(getContext(), R.color.color_7));
                break;
            case R.id.color_8:
                mListener.setSelectedColor(ContextCompat.getColor(getContext(), R.color.color_8));
                break;
            case R.id.color_9:
                mListener.setSelectedColor(ContextCompat.getColor(getContext(), R.color.color_9));
                break;
            case R.id.color_10:
                mListener.setSelectedColor(ContextCompat.getColor(getContext(), R.color.color_10));
                break;
        }

        Objects.requireNonNull(getDialog()).dismiss();
    }

}
