package mad.focuson.presenters;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

import mad.focuson.Task;
import mad.focuson.interfaces.Views;


public class MainActivityPresenter implements View.OnClickListener {
    String currentuserPath ="users/bthn";
    String currentuser= "bthn";
    Views.MainActivityView mainActivityView;
    Task currentTask;
    CountDownTimer countDownTimer;
    boolean isBreak = false;


    public MainActivityPresenter(Views.MainActivityView mainActivityView){
        this.mainActivityView = mainActivityView;
    }

    public void onClick(View v) {
        if (currentTask != null && !currentTask.isFinished()) {
            if (countDownTimer != null) {
                stopTimer();
            } else{

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
                    db.collection("users").document(currentuser).collection("tasks").document(currentTask.getTaskId())
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
                        mainActivityView.stopMusic();
                        currentTask.setTimestamp(Timestamp.now());
                        db.collection("users").document(currentuser).collection("tasks").document(currentTask.getTaskId())
                                .update("timestamp", currentTask.getTimestamp())
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
                        db.collection("leaderboard").whereEqualTo("userId", currentuserPath)
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            if (task.getResult().isEmpty()) {
                                                // Document does not exist, add a new document
                                                // TODO: make 2 digits after decimal point
                                                double newScore = currentTask.getWorkDuration() * currentTask.getNumberOfSessions() / 60.0;

                                                Map<String, Object> newLeaderboardData = new HashMap<>();
                                                newLeaderboardData.put("userId", db.document(currentuserPath));
                                                newLeaderboardData.put("score", newScore);

                                                db.collection("leaderboard")
                                                        .add(newLeaderboardData) // Add a new document to the collection
                                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                            @Override
                                                            public void onSuccess(DocumentReference documentReference) {
                                                                Log.d("success", "New document added to leaderboard w.replace(\"users/\",\"\")ith ID: " + documentReference.getId());
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Log.w("error", "Error adding new document: " + e.getMessage());
                                                            }
                                                        });
                                            } else {
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    double newScore = ((double) document.getData().get("score")) + (currentTask.getWorkDuration() * currentTask.getNumberOfSessions() / 60.0);
                                                    db.collection("leaderboard").document(document.getId())
                                                            .update("score", newScore)
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void unused) {
                                                                    Log.d("success", "Score updated successfully!");
                                                                }
                                                            })
                                                            .addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    Log.w("error", "Error updating score: " + e.getMessage());
                                                                }
                                                            });
                                                    Log.d("success", document.getId() + " => " + document.getData());
                                                }
                                            }
                                        } else {
                                            Log.w("error", "Error getting documents", task.getException());
                                        }
                                    }
                                });
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
