package com.kabasonic.notepad.ui.reminder;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.kabasonic.notepad.R;

public class ReminderDialogFragment extends DialogFragment implements View.OnClickListener, DataTimeDate{



    Context context;

    boolean stateSwitch = false;

    TextView dateReminder, timeReminder;
    Spinner spinnerReminder;
    SwitchMaterial switchReminder;
    MaterialButton btOkayReminder, btCancelReminder;
    private FragmentManager fm;

    public ReminderDialogFragment() {

    }

    public static ReminderDialogFragment newInstance() {
        ReminderDialogFragment reminderDialogFragment = new ReminderDialogFragment();
        return reminderDialogFragment;
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

        //String[] dataSpinner = context.getResources().getStringArray(R.array.data_spinner);
        String[] dataSpinner = {"Show", "Don't show"};

        // Get field from view
        fm = getActivity().getSupportFragmentManager();

        dateReminder = (TextView) view.findViewById(R.id.day_reminder);
        timeReminder = (TextView) view.findViewById(R.id.time_reminder);
        spinnerReminder = (Spinner) view.findViewById(R.id.spinner_reminder);
        switchReminder = (SwitchMaterial) view.findViewById(R.id.set_switch_reminder);
        btOkayReminder = (MaterialButton) view.findViewById(R.id.bt_okay_reminder);
        btCancelReminder = (MaterialButton) view.findViewById(R.id.bt_cancel_reminder);

        dateReminder.setOnClickListener(this);
        timeReminder.setOnClickListener(this);
        switchReminder.setOnClickListener(this);
        btOkayReminder.setOnClickListener(this);
        btCancelReminder.setOnClickListener(this);

//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),R.layout.dialog_remembering_note,R.id.spinner_reminder,dataSpinner);
//        adapter.setDropDownViewResource(R.layout.dialog_remembering_note);
//        spinnerReminder.setAdapter(adapter);
//        spinnerReminder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Log.i("Spinner","Click on element in spinner");
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                Log.i("Spinner", "Spinner nothing selected");
//            }
//        });
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.day_reminder:
                Log.i("onClick", "is Day");
                DialogFragment datePicker = new DatePickerDialogFragment();
                datePicker.show(fm, "date_picker");
                break;
            case R.id.time_reminder:
                Log.i("onClick", "is Time");
                DialogFragment timePicker = new TimePickerDialogFragment();
                timePicker.show(fm, "time_picker");
                break;
            case R.id.set_switch_reminder:
                Log.i("onClick", "is Switch");
                this.stateSwitch = switchReminder.isChecked();
                break;
            case R.id.bt_okay_reminder:
                Log.i("onClick", "is Okay");
                getDialog().dismiss();

                break;
            case R.id.bt_cancel_reminder:
                Log.i("onClick", "is Cancel");
                getDialog().dismiss();
                break;
        }
    }


    @Override
    public void time(int hour, int minute) {
        Log.i("onTimeSet", "Hour is: " + String.valueOf(hour));
        Log.i("onTimeSet", "Minute is: " + String.valueOf(minute));
        timeReminder.setText(hour + ":" + minute);
    }

    @Override
    public void date(int year, int month, int day) {
        Log.i("onDateSet","Day is: " + String.valueOf(day));
        Log.i("onDateSet","Month is: " + String.valueOf(month));
        Log.i("onDateSet","Year is: " + String.valueOf(year));
        dateReminder.setText(day + "." + month + "." + year);
    }
}
