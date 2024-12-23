package mad.focuson.views;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.annotation.NonNull;

import mad.focuson.R;
import mad.focuson.Task;
import mad.focuson.interfaces.Views;
import mad.focuson.presenters.MainActivityPresenter;
import mad.focuson.Model;
import mad.focuson.Music;
import mad.focuson.views.TasksActivity;
import mad.focuson.views.LeaderboardActivity;
import mad.focuson.views.ProfileActivity;
import mad.focuson.views.SettingsActivity;


public class MainActivity extends AppCompatActivity implements Views.MainView {
    private TextView txtTaskName;
    private TextView txtTimer;
    private TextView txtPomodoroCount;
    private ProgressBar progressBar;
    private Task currentTask;
    private Button btnStartStop;
    private Button btnStop;
    private Model model;
    private Music music;
    private Handler handler;
    private boolean isBgMusicPlaying = false;
    private boolean isDarkTheme = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            isBgMusicPlaying = savedInstanceState.getBoolean("isBgMusicPlaying", false);
            isDarkTheme = savedInstanceState.getBoolean("isDarkTheme", false);
        }
        applyTheme();
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeViews();

        model = new Model(this);
        music = new Music(this);
        handler = new Handler();
        MainActivityPresenter presenter = new MainActivityPresenter(this, model, music);

        setClickListeners(presenter);
    }

    private void initializeViews() {
        txtTaskName = findViewById(R.id.txtTaskName);
        txtTimer = findViewById(R.id.txtTimer);
        txtPomodoroCount = findViewById(R.id.txtPomodoroCount);
        btnStartStop = findViewById(R.id.btnStartStop);
        btnStop = findViewById(R.id.btnStop);
        progressBar = findViewById(R.id.progressBar);

        if (btnStartStop == null) Log.e("MainActivity", "btnStartStop not found");
        if (btnStop == null) Log.e("MainActivity", "btnStop not found");
        if (txtTaskName == null) Log.e("MainActivity", "txtTaskName not found");
        if (txtTimer == null) Log.e("MainActivity", "txtTimer not found");
    }

    private void setClickListeners(MainActivityPresenter presenter) {
        if (btnStartStop != null) {
            btnStartStop.setOnClickListener(presenter);
        }
        if (btnStop != null) {
            btnStop.setOnClickListener(presenter);
        }

        LinearLayout bottomNavigation = findViewById(R.id.bottomNavigation);
        if (bottomNavigation != null) {
            for (int i = 0; i < bottomNavigation.getChildCount(); i++) {
                View child = bottomNavigation.getChildAt(i);
                if (child != null) {
                    child.setOnClickListener(presenter);
                }
            }
        }

        View btnProfile = findViewById(R.id.btnProfile);
        View btnSettings = findViewById(R.id.btnSettings);
        
        if (btnProfile != null) {
            btnProfile.setOnClickListener(presenter);
        }
        if (btnSettings != null) {
            btnSettings.setOnClickListener(presenter);
        }

        Button btnBgMusic = findViewById(R.id.btnBgMusic);
        Button btnTheme = findViewById(R.id.btnTheme);

        if (btnBgMusic != null) {
            btnBgMusic.setOnClickListener(v -> toggleBackgroundMusic());
        }

        if (btnTheme != null) {
            btnTheme.setOnClickListener(v -> toggleTheme());
            btnTheme.setText(isDarkTheme ? "Light Theme" : "Dark Theme");
        }
    }

    private void toggleBackgroundMusic() {
        isBgMusicPlaying = !isBgMusicPlaying;
        Button btnBgMusic = findViewById(R.id.btnBgMusic);
        if (btnBgMusic != null) {
            btnBgMusic.setText(isBgMusicPlaying ? "Music Off" : "Music On");
        }
        
        if (isBgMusicPlaying) {
            // Start background music
            music.playBackgroundMusic();
        } else {
            // Stop background music
            music.stopBackgroundMusic();
        }
    }

    private void applyTheme() {
        if (isDarkTheme) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    private void toggleTheme() {
        isDarkTheme = !isDarkTheme;
        Button btnTheme = findViewById(R.id.btnTheme);
        if (btnTheme != null) {
            btnTheme.setText(isDarkTheme ? "Light Theme" : "Dark Theme");
        }
        applyTheme();
        recreate();  // Recreate activity to apply theme changes
    }

    @Override
    public void setTask(Task task) {
        this.currentTask = task;
        if (task != null) {
            txtTaskName.setText(task.getTitle());
        }
    }

    @Override
    public Task getTask() {
        return currentTask;
    }

    @Override
    public void updateTimer(String time) {
        txtTimer.setText(time);
        updateProgressBar(time);
    }

    @Override
    public void updateTaskName(String taskName) {
        if (txtTaskName != null) {
            txtTaskName.setText(taskName);
        }
    }

    @Override
    public void showBreakMessage(boolean isLongBreak) {
        String message = isLongBreak ? "Time for a long break!" : "Time for a short break!";
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updatePomodoroCount(int count) {
        if (txtPomodoroCount != null) {
            txtPomodoroCount.setText(String.format("Pomodoros: %d", count));
        }
    }

    @Override
    public void playTimerEndSound() {
        // This should be handled by the Music class
    }

    @Override
    public void enableStartButton(boolean enable) {
        if (btnStartStop != null) {
            btnStartStop.setText(enable ? "Start" : "Pause");
        }
    }

    @Override
    public void enablePauseButton(boolean enable) {
        if (btnStartStop != null) {
            btnStartStop.setText(enable ? "Pause" : "Start");
        }
    }

    @Override
    public void enableStopButton(boolean enable) {
        if (btnStop != null) {
        }
    }

    @Override
    public void navigateToTasks() {
        Intent intent = new Intent(this, TasksActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void navigateToLeaderboard() {
        Intent intent = new Intent(this, LeaderboardActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void navigateToProfile() {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void navigateToSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Save state when app goes to background
        model.savePomodoroCount();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Clean up resources
        handler.removeCallbacksAndMessages(null);
        music.release();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateProgress();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("isBgMusicPlaying", isBgMusicPlaying);
        outState.putBoolean("isDarkTheme", isDarkTheme);
    }

    private void updateProgress() {
        // Update initial progress when resuming
        if (txtTimer != null) {
            updateProgressBar(txtTimer.getText().toString());
        }
    }

    private void updateProgressBar(String timeString) {
        if (progressBar != null) {
            // Parse time string (format: "mm:ss")
            String[] parts = timeString.split(":");
            int minutes = Integer.parseInt(parts[0]);
            int seconds = Integer.parseInt(parts[1]);
            int totalSeconds = minutes * 60 + seconds;
            
            // Calculate progress percentage
            int maxSeconds = model.isBreakTime() ? 
                (model.isLongBreak() ? Model.LONG_BREAK_LENGTH : Model.SHORT_BREAK_LENGTH) : 
                Model.POMODORO_LENGTH;
            
            int progress = 100 - ((totalSeconds * 100) / maxSeconds);
            progressBar.setProgress(progress);
        }
    }
}