package mad.focuson;

import java.io.Serializable;
import java.text.SimpleDateFormat;

public class Task implements Serializable {
    private String taskName;
    private long workDuration;
    private long breakTime;
    private int numberOfSessions;
    private int sessionsLeft;
    private long remainingWorkDuration;
    private int remind;
    private long deadline;

    public Task(String taskName, long workDuration, long breakTime, int numberOfSessions, int remind, long deadline) {
        this.taskName = taskName;
        this.workDuration = workDuration;
        this.breakTime = breakTime;
        this.numberOfSessions = numberOfSessions;
        this.remind = remind;
        this.deadline = deadline;
        remainingWorkDuration = workDuration;
        sessionsLeft = numberOfSessions;
    }

//    public Task(long breakTime, int numberOfSessions, String taskName, long workDuration) {
//        this.breakTime = breakTime;
//        this.numberOfSessions = numberOfSessions;
//        this.taskName = taskName;
//        this.workDuration = workDuration;
//        this.remainingWorkDuration = workDuration;
//        sessionsLeft = numberOfSessions;
//    }

    public boolean isFinished(){
        return sessionsLeft == 0;
    }

    public void decrementRemainingWorkDuration(){
        remainingWorkDuration -= 1000;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public long getWorkDuration() {
        return workDuration;
    }

    public void setWorkDuration(long workDuration) {
        this.workDuration = workDuration;
    }

    public long getRemainingWorkDuration() {
        return remainingWorkDuration;
    }

    public long getBreakTime() {
        return breakTime;
    }

    public void setBreakTime(long breakTime) {
        this.breakTime = breakTime;
    }

    public int getNumberOfSessions() {
        return numberOfSessions;
    }

    public void setNumberOfSessions(int numberOfSessions) {
        this.numberOfSessions = numberOfSessions;
    }

    public int getSessionsLeft() {
        return sessionsLeft;
    }

    public void setSessionsLeft(int sessionsLeft) {
        this.sessionsLeft = sessionsLeft;
    }

    public int getRemind() {
        return remind;
    }

    public void setRemind(int remind) {
        this.remind = remind;
    }

    public long getDeadline() {
        return deadline;
    }

    public void setDeadline(long deadline) {
        this.deadline = deadline;
    }

    public void setRemainingWorkDuration(long millisUntilFinished) {
        remainingWorkDuration = millisUntilFinished;
    }
}
