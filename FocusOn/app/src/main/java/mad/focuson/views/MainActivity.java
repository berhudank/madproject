package mad.focuson.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import android.widget.Button;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import mad.focuson.R;
import mad.focuson.interfaces.Views;
import mad.focuson.presenters.MainActivityPresenter;

public class MainActivity extends AppCompatActivity implements Views.MainActivityView {
    TextView txtTaskName;
    Chronometer timer;
    ProgressBar progressBar;


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

        MainActivityPresenter presenter = new MainActivityPresenter(this);

        Listener listener = new Listener();

        LinearLayout bottomNavigation = findViewById(R.id.bottomNavigation);
        for (int i = 0; i < bottomNavigation.getChildCount(); i++) {
            bottomNavigation.getChildAt(i).setOnClickListener(listener);
        }
        findViewById(R.id.btnProfile).setOnClickListener(listener);
        findViewById(R.id.btnSettings).setOnClickListener(listener);


    }

    class Listener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            if(v instanceof Button){
                int id = v.getId();
                if (id == R.id.btnTasks) {
                    startActivity(new Intent(MainActivity.this, TasksActivity.class));
                } else if (id == R.id.btnLeaderboard) {
                    startActivity(new Intent(MainActivity.this, LeaderboardActivity.class));
                }
            }
        }
    }


}