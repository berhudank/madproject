package mad.focuson.views;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.Arrays;

import mad.focuson.R;
import mad.focuson.Task;
import mad.focuson.interfaces.Views;
import mad.focuson.presenters.MainActivityPresenter;


public class MainActivity extends AppCompatActivity implements Views.MainActivityView {
    TextView taskName;
    TextView timer;
    ProgressBar progressBar;
    boolean isFragmentVisible = false;

    private MediaPlayer mediaPlayer;
    private int selectedMusic = -1;

    MainActivityPresenter presenter;

    ArrayList<Integer> themes = new ArrayList<>(
            Arrays.asList(
                    R.drawable.theme1,
                    R.drawable.theme2
                    /*R.drawable.theme3,
                    R.drawable.theme4,
                    R.drawable.theme5,
                    R.drawable.theme6,
                    R.drawable.theme7,
                    R.drawable.theme8,
                    R.drawable.theme9,
                    R.drawable.theme10*/));

    ArrayList<Integer> musics = new ArrayList<>(
            Arrays.asList(
                    R.raw.music1,
                    R.raw.music2
            ));

    ArrayList<String> musicNames = new ArrayList<>(
            Arrays.asList(
                    "music1",
                    "music2"
                    ));



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

        FragmentManager fm = getSupportFragmentManager();

        fm.setFragmentResultListener("response", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                int theme = result.getInt("selectedTheme", -1);
                int music = result.getInt("selectedMusic", -1);
                if (theme != -1)
                    findViewById(R.id.main).setBackgroundResource(theme);
                else if(music != -1)
                    stopMusic();
                    selectedMusic = music;

            }
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
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
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

    @Override
    public void startMusic() {
        if (selectedMusic != -1) {
            if (mediaPlayer == null) {
                mediaPlayer = MediaPlayer.create(this, selectedMusic);
                mediaPlayer.setLooping(true); // Loop the music
            }
            mediaPlayer.start();
        }
    }

    @Override
    public void stopMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer = null; // Or mediaPlayer.stop() if you want to stop it entirely
        }
    }

    ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        if (result.getData() != null) {
                            String response = result.getData().getStringExtra("response");
                            if (response.equals("select")) {
                                presenter.handleTask((Task) result.getData().getSerializableExtra("selectedTask"));
                            } else if (response.equals("delete")) {
                                presenter.detachCurrentTask();
                            }
                        }
                    }
                }
            }
    );


    class Listener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (v instanceof Button) {
                FragmentManager fm = getSupportFragmentManager();
                int id = v.getId();
                if (id == R.id.btnTasks) {
                    launcher.launch(new Intent(MainActivity.this, TasksActivity.class));
                } else if (id == R.id.btnLeaderboard) {
                    startActivity(new Intent(MainActivity.this, LeaderboardActivity.class));
                }
                else if (id == R.id.btnTheme) {
                    showFragment(fm, ThemeSelectionFragment.newInstance(themes), "themes");
                }
                else if (id == R.id.btnBgMusic) {
                    showFragment(fm, MusicFragment.newInstance(musics, musicNames), "musics");
                }
                    // for other activities
            }
        }


        private void showFragment(FragmentManager fm, Fragment fragment, String tag) {
            FragmentTransaction transaction = fm.beginTransaction();

            // Check if the fragment with this tag is already displayed
            Fragment currentFragment = fm.findFragmentByTag(tag);

            if (currentFragment != null) {
                // If the fragment is already visible, toggle visibility off
                transaction.remove(currentFragment);
                findViewById(R.id.fragmentContainerView).setVisibility(View.INVISIBLE);
            } else {
                // Replace the current fragment with the new one
                transaction.replace(R.id.fragmentContainerView, fragment, tag);
                findViewById(R.id.fragmentContainerView).setVisibility(View.VISIBLE);
                fm.popBackStack();
            }

            transaction.commit();
        }
    }
}