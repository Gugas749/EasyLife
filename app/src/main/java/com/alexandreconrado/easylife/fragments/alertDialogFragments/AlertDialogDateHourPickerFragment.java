package com.alexandreconrado.easylife.fragments.alertDialogFragments;

import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.Toast;

import com.alexandreconrado.easylife.databinding.FragmentAlertDialogDateHourPickerBinding;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.Calendar;

public class AlertDialogDateHourPickerFragment extends Fragment {
    private FragmentAlertDialogDateHourPickerBinding binding;
    private ExitAlertDialogDateHourPicker listenner;
    public interface ExitAlertDialogDateHourPicker{
        void onExitAlertDialogDateHourPicker(boolean save, Date date);
    }
    public void setExitAlertDialogDateHourPickerListenner(ExitAlertDialogDateHourPicker listenner){
        this.listenner = listenner;
    }
    public AlertDialogDateHourPickerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAlertDialogDateHourPickerBinding.inflate(inflater);

        DatePicker datePicker = binding.datePickerAlertDialogDateHourPicker;
        Calendar currentDate = Calendar.getInstance();
        datePicker.setMaxDate(currentDate.getTimeInMillis());
        Calendar startOfYear = Calendar.getInstance();
        startOfYear.set(Calendar.MONTH, Calendar.JANUARY);
        startOfYear.set(Calendar.DAY_OF_MONTH, 1);
        startOfYear.set(Calendar.HOUR_OF_DAY, 0);
        startOfYear.set(Calendar.MINUTE, 0);
        startOfYear.set(Calendar.SECOND, 0);
        startOfYear.set(Calendar.MILLISECOND, 0);

        datePicker.setMinDate(startOfYear.getTimeInMillis());

        setupNextFinishButton();
        setupPreviousCancelButton();

        return binding.getRoot();
    }
    private void setupNextFinishButton(){
        binding.buttonConfirmAlertDialogDateHourPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date selectedDate = getSelectedDate();
                listenner.onExitAlertDialogDateHourPicker(true, selectedDate);
            }
        });
    }
    private void setupPreviousCancelButton(){
        binding.buttonCancelAlertDialogDateHourPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date = null;
                listenner.onExitAlertDialogDateHourPicker(false, date);
            }
        });
    }
    private Date getSelectedDate(){
        DatePicker datePicker = binding.datePickerAlertDialogDateHourPicker;
        int year = datePicker.getYear();
        int month = datePicker.getMonth();
        int day = datePicker.getDayOfMonth();
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        Date utilDate = calendar.getTime();
        return utilDate;
    }
}