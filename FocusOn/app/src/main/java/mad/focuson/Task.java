package mad.focuson;

public class Task {
    private String taskName;
    private long time;
    private long breakTime;
    private int numberOfSession;

    public void decrementTime(){
        time--;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getBreakTime() {
        return breakTime;
    }

    public void setBreakTime(long breakTime) {
        this.breakTime = breakTime;
    }

    public int getNumberOfSession() {
        return numberOfSession;
    }

    public void setNumberOfSession(int numberOfSession) {
        this.numberOfSession = numberOfSession;
    }
}
