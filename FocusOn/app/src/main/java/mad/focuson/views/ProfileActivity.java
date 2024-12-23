package mad.focuson.views;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import mad.focuson.R;

public class ProfileActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        
        // Enable back button in action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Profile");
        }
        
        // Initialize profile UI elements
        TextView totalPomodoros = findViewById(R.id.txtTotalPomodoros);
        TextView completedTasks = findViewById(R.id.txtCompletedTasks);
        
        // Load stats from SharedPreferences
        SharedPreferences prefs = getSharedPreferences("FocusOn", Context.MODE_PRIVATE);
        int pomodoroCount = prefs.getInt("pomodorosCompleted", 0);
        totalPomodoros.setText(String.format("Total Pomodoros: %d", pomodoroCount));
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
} 