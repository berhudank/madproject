package mad.focuson.views;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import mad.focuson.R;

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        
        // Enable back button in action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Settings");
        }
        
        // Add settings options
        Switch soundSwitch = findViewById(R.id.switchSound);
        Switch vibrationSwitch = findViewById(R.id.switchVibration);
        
        SharedPreferences prefs = getSharedPreferences("FocusOn", Context.MODE_PRIVATE);
        soundSwitch.setChecked(prefs.getBoolean("soundEnabled", true));
        vibrationSwitch.setChecked(prefs.getBoolean("vibrationEnabled", true));
        
        soundSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefs.edit().putBoolean("soundEnabled", isChecked).apply();
        });
        
        vibrationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefs.edit().putBoolean("vibrationEnabled", isChecked).apply();
        });
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