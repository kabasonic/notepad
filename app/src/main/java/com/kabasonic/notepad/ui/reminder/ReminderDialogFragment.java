package com.kabasonic.notepad.ui.reminder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.kabasonic.notepad.R;
import com.kabasonic.notepad.ui.NoteFragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ReminderDialogFragment extends DialogFragment implements View.OnClickListener, DataTimeDate {


    Context context;

    boolean stateSwitch = false;
    private int spinnerPosition = -1;
    private String[] dataSpinner;

    TextView dateReminder, timeReminder;
    Spinner spinnerReminder;
    SwitchMaterial switchReminder;
    MaterialButton btOkayReminder, btCancelReminder;

    private FragmentManager fm;

    public interface OnClickReminderListener {
        void dataListener(String date, String time);
    }

    OnClickReminderListener mListener;

    public ReminderDialogFragment() {

    }

    public ReminderDialogFragment(NoteFragment noteFragment){
        this.mListener = (OnClickReminderListener) noteFragment;
    }

    public static ReminderDialogFragment newInstance() {
        return new ReminderDialogFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        this.context = context;
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_remembering_note, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dataSpinner = context.getResources().getStringArray(R.array.data_spinner);

        // Get field from view
        fm = getActivity().getSupportFragmentManager();

        dateReminder = (TextView) view.findViewById(R.id.day_reminder);
        timeReminder = (TextView) view.findViewById(R.id.time_reminder);
        spinnerReminder = (Spinner) view.findViewById(R.id.spinner_reminder);
        switchReminder = (SwitchMaterial) view.findViewById(R.id.set_switch_reminder);
        btOkayReminder = (MaterialButton) view.findViewById(R.id.bt_okay_reminder);
        btCancelReminder = (MaterialButton) view.findViewById(R.id.bt_cancel_reminder);

        //Set current time
        String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
        timeReminder.setText(currentTime);

        dateReminder.setOnClickListener(this);
        timeReminder.setOnClickListener(this);
        switchReminder.setOnClickListener(this);
        btOkayReminder.setOnClickListener(this);
        btCancelReminder.setOnClickListener(this);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, dataSpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerReminder.setAdapter(adapter);
        spinnerReminder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                spinnerPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {}
        });


    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.day_reminder:
                Log.i("onClick", "is Day");
                DialogFragment datePicker = new DatePickerDialogFragment(this);
                datePicker.show(fm, "date_picker");
                break;
            case R.id.time_reminder:
                Log.i("onClick", "is Time");
                DialogFragment timePicker = new TimePickerDialogFragment(this);
                timePicker.show(fm, "time_picker");
                break;
            case R.id.set_switch_reminder:
                Log.i("onClick", "is Switch");
                this.stateSwitch = switchReminder.isChecked();
                break;
            case R.id.bt_okay_reminder:
                Log.i("onClick", "is Okay");
                Log.i("Date:", String.valueOf(dateReminder.getText()));
                Log.i("Time:", String.valueOf(timeReminder.getText()));
                Log.i("Do not repeat:",dataSpinner[spinnerPosition]);
                Log.i("Show text:", String.valueOf(stateSwitch));
                mListener.dataListener(dateReminder.getText().toString(),timeReminder.getText().toString());
                getDialog().dismiss();

                break;
            case R.id.bt_cancel_reminder:
                Log.i("onClick", "is Cancel");
                getDialog().dismiss();
                break;
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void time(int hour, int minute) {
        timeReminder.setText(hour + ":" + minute);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void date(int year, int month, int day) {
        dateReminder.setText(day + "." + month + "." + year);
    }
}
