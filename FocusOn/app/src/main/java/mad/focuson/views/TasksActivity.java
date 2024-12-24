package mad.focuson.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import mad.focuson.Model;
import mad.focuson.R;
import mad.focuson.Task;
import mad.focuson.interfaces.Views;
import mad.focuson.presenters.TasksActivityPresenter;
import mad.focuson.views.adapters.TaskRecyclerViewAdapter;

public class TasksActivity extends AppCompatActivity implements Views.TasksActivityView {
    Button btnAddNewTask;
    ImageButton imgBtnBack, imgBtnDelete;
    RecyclerView tasksListView;
    TasksActivityPresenter presenter;
    Task taskToEdit;
    Model model;

    ArrayList<Task> tasks;

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

        model = Model.getInstance();
        tasks = model.getTasks();
        presenter = new TasksActivityPresenter(this);

        btnAddNewTask = findViewById(R.id.btnAddNewTask);
        imgBtnBack = findViewById(R.id.imgBtnBack);
        imgBtnDelete = findViewById(R.id.imgBtnDelete);
        tasksListView = findViewById(R.id.tasksListView);

//        tasks.add(new Task("FinishedTask", 20000, 20000, 3, 0, 0));
//        for (int i = 0; i < 20; i++) {
//            tasks.add(new Task("FinishedTask", 20000, 20000, 3, 0, 0));
//        }
//        tasks.add(new Task("FinishedTask", 20000, 20000, 3, 0, 0));

        tasksListView.setLayoutManager(new LinearLayoutManager(this));
        tasksListView.setAdapter(new TaskRecyclerViewAdapter(tasks, this));

        FragmentManager fm = getSupportFragmentManager();

        fm.setFragmentResultListener("task", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                // We use a String here, but any type that can be put in a Bundle is supported.

                // Do something with the result.
                if(taskToEdit == null){
                    tasks.add((Task) bundle.getSerializable("newTask"));
                    tasksListView.getAdapter().notifyDataSetChanged(); // TO-DO: Think about other methods ... notifyiteminserted()...
                }
                else {
                    Task editedTask = (Task) bundle.getSerializable("newTask");
                    taskToEdit.setDeadline(editedTask.getDeadline());
                    taskToEdit.setRemind(editedTask.getRemind());
                    taskToEdit.setTaskName(editedTask.getTaskName());
                    taskToEdit.setNumberOfSessions(editedTask.getNumberOfSessions());
                    taskToEdit.setBreakTime(editedTask.getBreakTime());
                    taskToEdit.setWorkDuration(editedTask.getWorkDuration());
                    tasksListView.getAdapter().notifyDataSetChanged();
                    taskToEdit = null;
                }
            }
        });

        fm.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if(fm.findFragmentByTag("settings") == null){
                    tasksListView.setVisibility(View.VISIBLE);
                    btnAddNewTask.setText("Add New Task");
                }
                else {
                    btnAddNewTask.setText("Save");
                }
            }
        });

        btnAddNewTask.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Fragment fragment = fm.findFragmentByTag("settings");
                if(fragment == null) {
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.add(R.id.taskFragmentContainerView, TaskSettingsFragment.newInstance(null), "settings");
                    ft.addToBackStack(null);
                    tasksListView.setVisibility(View.INVISIBLE);
                    ft.commit();
                }
                else {
                    TaskSettingsFragment tsf = (TaskSettingsFragment) fragment;
                    if (tsf.isAdded() && tsf.getView() != null) {
                        tsf.sendTask();
                        fm.popBackStack();
                        tasksListView.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

    }

    @Override
    public void returnResult(int resultCode, Intent intent) {
        setResult(resultCode, intent);
        finish();
    }

    @Override
    public void sendToSettings(Task selectedTask) {
        taskToEdit = selectedTask;
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.taskFragmentContainerView, TaskSettingsFragment.newInstance(selectedTask), "settings");
        ft.addToBackStack(null);
        tasksListView.setVisibility(View.INVISIBLE);
        ft.commit();
    }

}