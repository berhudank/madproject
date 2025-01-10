package mad.focuson.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import mad.focuson.R;
import mad.focuson.Task;
import mad.focuson.interfaces.Views;
import mad.focuson.presenters.MainActivityPresenter;


public class MainActivity extends AppCompatActivity implements Views.MainActivityView {
    TextView taskName;
    TextView timer;
    ProgressBar progressBar;

    MainActivityPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        taskName = findViewById(R.id.txtTaskName);
        timer = findViewById(R.id.txtTimer);
        progressBar = findViewById(R.id.progressBar);

        presenter = new MainActivityPresenter(this);

        Listener listener = new Listener();

        LinearLayout bottomNavigation = findViewById(R.id.bottomNavigation);
        for (int i = 0; i < bottomNavigation.getChildCount(); i++) {
            bottomNavigation.getChildAt(i).setOnClickListener(listener);
        }
        findViewById(R.id.btnProfile).setOnClickListener(listener);
        findViewById(R.id.btnSettings).setOnClickListener(listener);

        findViewById(R.id.btnStartStop).setOnClickListener(presenter);
    }

    @Override
    public void updateTaskName(String taskName) {
        this.taskName.setText(taskName);
    }

    @Override
    public void updateProgress(int progress) {
        progressBar.setProgress(progress);
    }

    @Override
    public void updateTimer(String time) {
        timer.setText(time);
    }

    ActivityResultLauncher<Intent> launcher = registerForActivityResult(
        new ActivityResultContracts.StartActivityForResult(),
        new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    if(result.getData() != null) {
                        String response = result.getData().getStringExtra("response");
                        if(response.equals("select")) {
                            presenter.handleTask((Task) result.getData().getSerializableExtra("selectedTask"));
                        } else if (response.equals("delete")) {
                            presenter.detachCurrentTask();
                        }
                    }
                }
            }
        }
    );


    class Listener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            if(v instanceof Button){
                int id = v.getId();
                if (id == R.id.btnTasks) {
                    launcher.launch(new Intent(MainActivity.this, TasksActivity.class));
                } else if (id == R.id.btnLeaderboard) {
                    startActivity(new Intent(MainActivity.this, LeaderboardActivity.class));
                }
                // for other activities
            }
        }
    }


}