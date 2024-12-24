package mad.focuson.presenters;

import android.os.CountDownTimer;
import android.view.View;

import mad.focuson.Model;
import mad.focuson.Task;
import mad.focuson.interfaces.ModelListener;
import mad.focuson.interfaces.Views;


public class MainActivityPresenter implements ModelListener, View.OnClickListener {
    Views.MainActivityView mainActivityView;
    Model model;
    Task currentTask;
    CountDownTimer countDownTimer;

    public MainActivityPresenter(Views.MainActivityView mainActivityView){
        this.mainActivityView = mainActivityView;
        model = Model.getInstance();
    }

    @Override
    public void onClick(View v) {
        if(currentTask != null){
            if (countDownTimer != null){
                stopTimer();
                // maybe we can also notify the model here
            }
            else{
                setNewTimer(currentTask.getRemainingWorkDuration());
                startTimer();
            }
        }

    }

    public void handleTask(Task selectedTask){
        if(currentTask != null){
            stopTimer();
            // Send the state of the current task to the model
        }
        mainActivityView.updateTaskName(selectedTask.getTaskName());
        long workDuration = selectedTask.getWorkDuration()/1000;
        String time = workDuration/60 + ":" + (workDuration%60);
        mainActivityView.updateTimer(time);
        mainActivityView.updateProgress(0);
        currentTask = selectedTask;
    }

    // this needs to be run on another thread
    private void setNewTimer(long milliseconds){
        countDownTimer = new CountDownTimer(milliseconds, 1000) {

            public void onTick(long millisUntilFinished) {
                //mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
                currentTask.decrementRemainingWorkDuration();
                String remainingTime = millisUntilFinished/60000 + ":" + (millisUntilFinished%60000)/1000;
                mainActivityView.updateTimer(remainingTime);
                //update progress
                mainActivityView.updateProgress(
                        (int) ( (currentTask.getWorkDuration() - currentTask.getRemainingWorkDuration()) / (double) currentTask.getWorkDuration() * 100)
                );
            }

            public void onFinish() {
                mainActivityView.updateTimer("Time's up!");
            }
        };
    }

    private void stopTimer(){
        if(countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }

    private void startTimer(){
        if(countDownTimer != null) {
            countDownTimer.start();
        }
    }

}
