package mad.focuson.views;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import mad.focuson.Model;
import mad.focuson.R;
import mad.focuson.Task;
import mad.focuson.interfaces.Views;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class TasksActivity extends AppCompatActivity implements Views.TasksView {

    private Model model;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tasks);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.tasks), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("dd/MM/yy", Locale.getDefault());
        model = new Model(this);

        // Enable back button in action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Tasks");
        }

        FloatingActionButton fabAddTask = findViewById(R.id.fabAddTask);
        fabAddTask.setOnClickListener(v -> showAddTaskDialog());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public void showTasks(List<Task> tasks) {
        // Display the list of tasks
    }

    @Override
    public void showAddTaskDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_add_task);

        // Initialize views
        EditText editTaskName = dialog.findViewById(R.id.editTaskName);
        NumberPicker workDurationPicker = dialog.findViewById(R.id.workDurationPicker);
        NumberPicker breakDurationPicker = dialog.findViewById(R.id.breakDurationPicker);
        NumberPicker sessionCountPicker = dialog.findViewById(R.id.sessionCountPicker);
        EditText editDeadline = dialog.findViewById(R.id.editDeadline);
        ImageButton btnCalendar = dialog.findViewById(R.id.btnCalendar);
        Switch switchDeadline = dialog.findViewById(R.id.switchDeadline);
        SeekBar seekBarReminder = dialog.findViewById(R.id.seekBarReminder);
        TextView txtReminderDays = dialog.findViewById(R.id.txtReminderDays);
        Button btnSave = dialog.findViewById(R.id.btnSave);

        // Setup NumberPickers
        workDurationPicker.setMinValue(1);
        workDurationPicker.setMaxValue(60);
        workDurationPicker.setValue(25);

        breakDurationPicker.setMinValue(1);
        breakDurationPicker.setMaxValue(30);
        breakDurationPicker.setValue(5);

        sessionCountPicker.setMinValue(1);
        sessionCountPicker.setMaxValue(10);
        sessionCountPicker.setValue(1);

        // Setup deadline picker
        btnCalendar.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(year, month, dayOfMonth);
                    editDeadline.setText(dateFormat.format(calendar.getTime()));
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            );
            datePickerDialog.show();
        });

        // Setup reminder seekbar
        seekBarReminder.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                txtReminderDays.setText(progress + " days");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // Handle save button
        btnSave.setOnClickListener(v -> {
            String taskName = editTaskName.getText().toString();
            if (taskName.isEmpty()) {
                editTaskName.setError("Task name is required");
                return;
            }

            Task newTask = new Task(
                taskName,
                workDurationPicker.getValue(),
                breakDurationPicker.getValue(),
                sessionCountPicker.getValue(),
                switchDeadline.isChecked() ? calendar.getTimeInMillis() : 0,
                seekBarReminder.getProgress()
            );

            model.addTask(newTask);
            model.saveTasks();
            showTasks(model.getTasks());
            dialog.dismiss();
        });

        dialog.show();
    }

    @Override
    public void showTaskDetails(Task task) {
        // Show details of the selected task
    }

    @Override
    public void showTaskUpdated() {
        // Show confirmation that task was updated
    }

    @Override
    public void showTaskDeleted() {
        // Show confirmation that task was deleted
    }
}