package com.kabasonic.notepad.ui.reminder;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class TimePickerDialogFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    DataTimeDate mListener;

    public TimePickerDialogFragment(ReminderDialogFragment reminderDialogFragment){
        this.mListener = (DataTimeDate) reminderDialogFragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        //return new TimePickerDialog(getActivity(), (TimePickerDialog.OnTimeSetListener) getActivity(), hour, minute, DateFormat.is24HourFormat(getActivity()));
        return new TimePickerDialog(getActivity(), this, hour, minute, true);
    }


    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        this.mListener.time(hourOfDay,minute);
    }
}
