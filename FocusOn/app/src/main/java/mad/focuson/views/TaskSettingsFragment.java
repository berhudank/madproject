package mad.focuson.views;

import android.app.DatePickerDialog;
import android.icu.util.DateInterval;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import mad.focuson.R;
import mad.focuson.Task;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TaskSettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TaskSettingsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_TASK = "task";

    // TODO: Rename and change types of parameters
    private Task task;

    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private EditText editTaskName;
    private NumberPicker workDurationPicker;
    private NumberPicker breakDurationPicker;
    private NumberPicker sessionCountPicker;
    private EditText editDeadline;
    private ImageButton btnCalendar;
    private SwitchCompat switchDeadline;
    private SeekBar seekBarReminder;
    private TextView txtReminderDays;

    public TaskSettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param task Parameter 1.
     *
     * @return A new instance of fragment TaskSettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TaskSettingsFragment newInstance(Task task) {
        TaskSettingsFragment fragment = new TaskSettingsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_TASK, task);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            task = (Task) getArguments().getSerializable(ARG_TASK);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_task_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Initialize views
        editTaskName = view.findViewById(R.id.txtEditTaskName);
        workDurationPicker = view.findViewById(R.id.workDurationPicker);
        breakDurationPicker = view.findViewById(R.id.breakTimePicker);
        sessionCountPicker = view.findViewById(R.id.sessionNumberPicker);
        editDeadline = view.findViewById(R.id.txtSetDeadline);
        btnCalendar = view.findViewById(R.id.btnDeadlineCalendar);
        switchDeadline = view.findViewById(R.id.deadlineSwitch);
        seekBarReminder = view.findViewById(R.id.reminderSeekBar);
        txtReminderDays = view.findViewById(R.id.reminderDaysTextView);

        // Setup NumberPickers
        workDurationPicker.setMinValue(1);
        workDurationPicker.setMaxValue(60);

        breakDurationPicker.setMinValue(1);
        breakDurationPicker.setMaxValue(30);

        sessionCountPicker.setMinValue(1);
        sessionCountPicker.setMaxValue(10);


        // TO-DO: Prevent user from entering invalid dates
        // in the EditText
        // Require a Task Name

        dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        calendar = Calendar.getInstance();
        // cannot choose any date before tomorrow
        calendar.add(Calendar.DAY_OF_MONTH, 1);

        if(task != null){
            editTaskName.setText(task.getTaskName());
            workDurationPicker.setValue(task.getWorkDurationInMinutes());
            breakDurationPicker.setValue(task.getBreakTimeInMinutes());
            sessionCountPicker.setValue(task.getNumberOfSessions());
            if(task.getDeadline() != 0){
                calendar.setTimeInMillis(task.getDeadline());
                editDeadline.setText(dateFormat.format(calendar.getTime()));
            }
            switchDeadline.setChecked(task.getDeadline() != 0);
            seekBarReminder.setProgress(task.getRemind());
        }
        else{
            workDurationPicker.setValue(1);
            breakDurationPicker.setValue(1);
            sessionCountPicker.setValue(1);
        }

        // Setup deadline picker
        // TO-DO: Dialog can be created only once,
        // after that it could be updated using dialog.updateDate(...);
        btnCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        TaskSettingsFragment.this.requireContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                calendar.set(year, month, dayOfMonth);
                                editDeadline.setText(dateFormat.format(calendar.getTime()));
                                long diff = TimeUnit.MILLISECONDS.toDays(calendar.getTimeInMillis() - Calendar.getInstance().getTimeInMillis());
                                seekBarReminder.setMax(diff < 5 ? (int) diff : 5); // limit seekBar based on the date chosen
                            }
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                );
                Calendar currentTime = Calendar.getInstance();
                currentTime.add(Calendar.DAY_OF_MONTH , 1);
                datePickerDialog.getDatePicker().setMinDate(currentTime.getTimeInMillis());
                datePickerDialog.show();
            }
        });

        // Setup reminder seekbar
        seekBarReminder.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                txtReminderDays.setText(progress != 0 ? progress + (progress != 1 ? " days" : " day") : "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }


        });



    }

    // onAttach onDetach onDestroy

    public void sendTask(){
        Task newTask = new Task(
                editTaskName.getText().toString(),
                workDurationPicker.getValue()*60000,
                breakDurationPicker.getValue()*60000,
                sessionCountPicker.getValue(),
                switchDeadline.isChecked() ? seekBarReminder.getProgress() : 0,
                switchDeadline.isChecked() ? calendar.getTimeInMillis() : 0
        );

        Bundle result = new Bundle();
        result.putSerializable("newTask", newTask);
        // The child fragment needs to still set the result on its parent fragment manager.
        getParentFragmentManager().setFragmentResult("task", result);
    }


}