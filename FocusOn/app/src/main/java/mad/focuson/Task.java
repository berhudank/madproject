package mad.focuson;

import java.io.Serializable;

public class Task implements Serializable {
    private String taskName;
    private long workDuration;
    private long breakTime;
    private int numberOfSessions;
    private int sessionsLeft;
    private long remainingWorkDuration;

    public Task(long breakTime, int numberOfSessions, String taskName, long workDuration) {
        this.breakTime = breakTime;
        this.numberOfSessions = numberOfSessions;
        this.taskName = taskName;
        this.workDuration = workDuration;
        this.remainingWorkDuration = workDuration;
        sessionsLeft = numberOfSessions;
    }

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
}
