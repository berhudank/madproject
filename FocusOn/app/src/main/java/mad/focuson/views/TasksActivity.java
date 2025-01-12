package mad.focuson.views;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import mad.focuson.R;
import mad.focuson.Task;
import mad.focuson.interfaces.Views;
import mad.focuson.views.adapters.TaskRecyclerViewAdapter;

public class TasksActivity extends AppCompatActivity implements Views.TasksActivityView {
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    Button btnAddNewTask;
    ImageButton imgBtnBack;
    RecyclerView tasksListView;
    Task taskToEdit;

    ArrayList<Task> tasks = new ArrayList<>();

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

        btnAddNewTask = findViewById(R.id.btnAddNewTask);
        imgBtnBack = findViewById(R.id.imgBtnBack);
        tasksListView = findViewById(R.id.tasksListView);

        imgBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });




        tasksListView.setLayoutManager(new LinearLayoutManager(TasksActivity.this));
        tasksListView.setAdapter(new TaskRecyclerViewAdapter(tasks, this));

        FirebaseFirestore db = FirebaseFirestore.getInstance();


        CollectionReference ref = db.collection("users").document(user.getUid()).collection("tasks");
        new DatabaseAcess().execute(ref);


        FragmentManager fm = getSupportFragmentManager();

        fm.setFragmentResultListener("task", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                // We use a String here, but any type that can be put in a Bundle is supported.

                // Do something with the result.
                if(taskToEdit == null){
                    Task newTask = (Task) bundle.getSerializable("newTask");
                    DocumentReference ref = db.collection("users").document(user.getUid()).collection("tasks").document();

                    newTask.setTaskId(ref.getId());
                    tasks.add(newTask);
                    tasksListView.getAdapter().notifyDataSetChanged(); // TO-DO: Think about other methods ... notifyiteminserted()...

                    ref.set(newTask, SetOptions.merge())
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d("success", "Task updated successfully!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("error", "Error updating task: " + e.getMessage());
                                }
                            });


                }
                else {
                    Task editedTask = (Task) bundle.getSerializable("newTask");
                    taskToEdit.setDeadline(editedTask.getDeadline());
                    taskToEdit.setRemind(editedTask.getRemind());
                    taskToEdit.setTaskName(editedTask.getTaskName());
                    taskToEdit.setNumberOfSessions(editedTask.getNumberOfSessions());
                    taskToEdit.setRemainingSessions(editedTask.getRemainingSessions());
                    taskToEdit.setBreakTime(editedTask.getBreakTime());
                    taskToEdit.setRemainingBreakTime(editedTask.getRemainingBreakTime());
                    taskToEdit.setWorkDuration(editedTask.getWorkDuration());
                    taskToEdit.setRemainingWorkDuration(editedTask.getRemainingWorkDuration());
                    tasksListView.getAdapter().notifyDataSetChanged();


                    Map<String, Object> taskMap = new HashMap<>();
                    taskMap.put("taskName", taskToEdit.getTaskName());
                    taskMap.put("workDuration", taskToEdit.getWorkDuration());
                    taskMap.put("breakTime", taskToEdit.getBreakTime());
                    taskMap.put("numberOfSessions", taskToEdit.getNumberOfSessions());
                    taskMap.put("remainingWorkDuration", taskToEdit.getRemainingWorkDuration());
                    taskMap.put("remainingBreakTime", taskToEdit.getRemainingBreakTime());
                    taskMap.put("remainingSessions", taskToEdit.getRemainingSessions());
                    taskMap.put("remind", taskToEdit.getRemind());
                    taskMap.put("deadline", taskToEdit.getDeadline());


                    db.collection("users").document(user.getUid()).collection("tasks").document(taskToEdit.getTaskId())
                            .update(taskMap)
                            .addOnSuccessListener(aVoid -> {
                                // Handle success
                                Log.d("success", "Task updated successfully!");
                            })
                            .addOnFailureListener(e -> {
                                // Handle failure
                                Log.w("error", "Error updating task: " + e.getMessage());
                            });

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
                        tsf.sendTask(); // make the fragment call getParentFragmentManager().setFragmentResult("task", result);
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
    public void detachTask(){
        setResult(RESULT_OK, new Intent().putExtra("response", "delete"));
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

    private class DatabaseAcess extends AsyncTask<CollectionReference, Void, ArrayList<Task>>{

        protected ArrayList<Task> doInBackground(CollectionReference... refs) {
            ArrayList<Task> fetchedTasks = new ArrayList<>();
            CountDownLatch latch = new CountDownLatch(1); // Initialize the latch with a count of 1

            refs[0].get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull com.google.android.gms.tasks.Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d("success", document.getId() + " => " + document.getData());
                                    Task userTask = document.toObject(Task.class);
                                    fetchedTasks.add(userTask);
                                }
                            } else {
                                Log.w("error", "Error getting documents", task.getException());
                            }
                            latch.countDown(); // Decrease the latch count when done
                        }
                    });

            try {
                latch.await(); // Wait until the latch count reaches 0
            } catch (InterruptedException e) {
                Log.e("error", "Latch interrupted", e);
            }

            return fetchedTasks;
        }

        protected void onPostExecute(ArrayList<Task> tasks) {
            TasksActivity.this.tasks.addAll(tasks);
            tasksListView.getAdapter().notifyDataSetChanged();
        }
    }
}