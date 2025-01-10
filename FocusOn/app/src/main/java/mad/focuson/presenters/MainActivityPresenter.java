package mad.focuson.presenters;

import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.Map;

import mad.focuson.Task;
import mad.focuson.interfaces.Views;
import mad.focuson.views.MainActivity;


public class MainActivityPresenter implements View.OnClickListener {
    Views.MainActivityView mainActivityView;
    Task currentTask;
    CountDownTimer countDownTimer;
    boolean isBreak = false;


    public MainActivityPresenter(Views.MainActivityView mainActivityView){
        this.mainActivityView = mainActivityView;
    }

    public void onClick(View v) {
        if (currentTask != null) {
            if (countDownTimer != null) {
                stopTimer();
            } else {
                setNewTimer(currentTask.getRemainingWorkDuration());
                startTimer();
            }
        }
    }

    public void handleTask(Task selectedTask) {
        isBreak = false;
        if (currentTask != null) {
            stopTimer();
        }
        mainActivityView.updateTaskName(selectedTask.getTaskName());
        long workDuration = selectedTask.getWorkDuration() / 1000;
        String time = workDuration / 60 + ":" + (workDuration % 60);
        mainActivityView.updateTimer(time);
        mainActivityView.updateProgress(0);
        currentTask = selectedTask;
    }


    // this needs to be run on another thread
    private void setNewTimer(long milliseconds) {
        countDownTimer = new CountDownTimer(milliseconds, 1000) {

            public void onTick(long millisUntilFinished) {
                currentTask.setRemainingWorkDuration(millisUntilFinished); // Update remaining time
                String remainingTime = millisUntilFinished / 60000 + ":" + (millisUntilFinished % 60000) / 1000;
                mainActivityView.updateTimer(remainingTime);
                // update progress
                mainActivityView.updateProgress(
                        (int) ((currentTask.getWorkDuration() - millisUntilFinished) / (double) currentTask.getWorkDuration() * 100)
                );
            }

            public void onFinish() {
                if(!isBreak) {
                    currentTask.decrementRemainingSessions();
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("users").document("ali").collection("tasks").document(currentTask.getTaskId())
                            .update("remainingSessions", currentTask.getRemainingSessions())
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
                    if (!currentTask.isFinished()){
                        setNewTimer(currentTask.getBreakTime());
                        isBreak = true;
                        mainActivityView.updateTaskName("Session is finished, it is break time");
                    }
                    else{
                        mainActivityView.updateTaskName("Task finished");
                        return;
                    }
                }
                else {
                    setNewTimer(currentTask.getWorkDuration());
                    isBreak = false;
                    mainActivityView.updateTaskName("Break finished, new session has started");
                }
                startTimer();
            }
        };
    }

    private void stopTimer() {
        if (countDownTimer != null) {
            mainActivityView.stopMusic();
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }

    private void startTimer() {
        if (countDownTimer != null) {
            mainActivityView.startMusic();
            countDownTimer.start();
        }
    }

    public void detachCurrentTask() {
        stopTimer();
        currentTask = null;
        mainActivityView.updateTaskName("");
        mainActivityView.updateTimer("00:00");
        mainActivityView.updateProgress(0);
    }
}
