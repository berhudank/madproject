package mad.focuson.presenters;

import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;

import mad.focuson.R;
import mad.focuson.Task;
import mad.focuson.interfaces.ModelListener;
import mad.focuson.interfaces.Views;
import mad.focuson.Music;
import mad.focuson.Model;


public class MainActivityPresenter implements ModelListener, View.OnClickListener {
    private Views.MainView view;
    private Model model;
    private Music music;
    private Handler handler;
    private static final int TIMER_DELAY = 1000; // 1 second

    public MainActivityPresenter(Views.MainView view, Model model, Music music) {
        this.view = view;
        this.model = model;
        this.music = music;
        this.handler = new Handler();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnStartStop) {
            if (!model.isRunning()) {
                startTimer();
            } else {
                pauseTimer();
            }
        } else if (id == R.id.btnStop) {
            stopTimer();
        } else if (id == R.id.btnTasks) {
            view.navigateToTasks();
        } else if (id == R.id.btnLeaderboard) {
            view.navigateToLeaderboard();
        } else if (id == R.id.btnProfile) {
            view.navigateToProfile();
        } else if (id == R.id.btnSettings) {
            view.navigateToSettings();
        }
    }

    @Override
    public void sendSelectedTask(Task selectedTask) {
        model.setCurrentTask(selectedTask);
        view.updateTaskName(selectedTask.getTitle());
    }

    private final Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            if (model.isRunning()) {
                model.tick();
                updateView();
                
                if (model.isTimerComplete()) {
                    handleTimerComplete();
                } else {
                    handler.postDelayed(this, TIMER_DELAY);
                }
            }
        }
    };

    public void startTimer() {
        model.startTimer();
        view.enableStartButton(false);
        view.enablePauseButton(true);
        view.enableStopButton(true);
        handler.post(timerRunnable);
    }

    public void pauseTimer() {
        model.pauseTimer();
        view.enableStartButton(true);
        view.enablePauseButton(false);
        handler.removeCallbacks(timerRunnable);
    }

    public void stopTimer() {
        model.stopTimer();
        view.enableStartButton(true);
        view.enablePauseButton(false);
        view.enableStopButton(false);
        handler.removeCallbacks(timerRunnable);
        updateView();
    }

    private void handleTimerComplete() {
        model.handleTimerComplete();
        music.playTimerEndSound();
        
        if (model.isBreakTime()) {
            view.showBreakMessage(model.getPomodorosCompleted() % 4 == 0);
        }
        
        updateView();
        view.enableStartButton(true);
        view.enablePauseButton(false);
        view.enableStopButton(false);
    }

    private void updateView() {
        int timeRemaining = model.getCurrentTime();
        int minutes = timeRemaining / 60;
        int seconds = timeRemaining % 60;
        view.updateTimer(String.format("%02d:%02d", minutes, seconds));
        
        Task currentTask = model.getCurrentTask();
        if (currentTask != null) {
            view.updateTaskName(currentTask.getTitle());
        }
        
        view.updatePomodoroCount(model.getPomodorosCompleted());
    }
}
