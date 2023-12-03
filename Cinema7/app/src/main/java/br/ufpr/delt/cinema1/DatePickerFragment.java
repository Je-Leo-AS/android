package br.ufpr.delt.cinema1;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment {
    //DatePickerDialog.OnDateSetListener

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Bundle bDate = this.getArguments();
        long lDate = bDate.getLong("data");

        // e usa como default no picker
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(lDate);
        int year = c.get(c.YEAR);
        int month = c.get(c.MONTH);
        int day = c.get(c.DAY_OF_MONTH);

        // caso fosse usar a data corrente, sem pegar do Bundle
        // final Calendar c = Calendar.getInstance();
        // int year = c.get(Calendar.YEAR);
        // int month = c.get(Calendar.MONTH);
        // int day = c.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(), (DatePickerDialog.OnDateSetListener) getActivity(), year, month, day);
    }



}
