package mad.focuson;

public class Task {
    private long id;
    private String title;
    private int estimatedPomodoros;
    private int pomodorosCompleted;
    private boolean completed;
    private int workDuration;      // in minutes
    private int breakDuration;     // in minutes
    private int sessionCount;
    private long deadline;         // timestamp
    private int reminderDays;

    public Task(String title, int estimatedPomodoros) {
        this.id = System.currentTimeMillis();
        this.title = title;
        this.estimatedPomodoros = estimatedPomodoros;
        this.pomodorosCompleted = 0;
        this.completed = false;
        this.workDuration = 25;    // default 25 minutes
        this.breakDuration = 5;    // default 5 minutes
        this.sessionCount = 1;     // default 1 session
        this.deadline = 0;         // no deadline by default
        this.reminderDays = 0;     // no reminder by default
    }

    // Constructor with all parameters
    public Task(String title, int workDuration, int breakDuration, int sessionCount, 
                long deadline, int reminderDays) {
        this.id = System.currentTimeMillis();
        this.title = title;
        this.workDuration = workDuration;
        this.breakDuration = breakDuration;
        this.sessionCount = sessionCount;
        this.deadline = deadline;
        this.reminderDays = reminderDays;
        this.estimatedPomodoros = sessionCount;
        this.pomodorosCompleted = 0;
        this.completed = false;
    }

    // Getters and setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public int getPomodorosCompleted() { return pomodorosCompleted; }
    public void incrementPomodorosCompleted() { this.pomodorosCompleted++; }
    
    public int getEstimatedPomodoros() { return estimatedPomodoros; }
    public void setEstimatedPomodoros(int estimatedPomodoros) { 
        if (estimatedPomodoros <= 0) {
            throw new IllegalArgumentException("Estimated pomodoros must be positive");
        }
        this.estimatedPomodoros = estimatedPomodoros; 
    }
    
    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }
    
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public float getProgress() {
        if (estimatedPomodoros == 0) return 0;
        return (float) pomodorosCompleted / estimatedPomodoros;
    }

    // New getters and setters
    public int getWorkDuration() { return workDuration; }
    public void setWorkDuration(int workDuration) { this.workDuration = workDuration; }
    
    public int getBreakDuration() { return breakDuration; }
    public void setBreakDuration(int breakDuration) { this.breakDuration = breakDuration; }
    
    public int getSessionCount() { return sessionCount; }
    public void setSessionCount(int sessionCount) { this.sessionCount = sessionCount; }
    
    public long getDeadline() { return deadline; }
    public void setDeadline(long deadline) { this.deadline = deadline; }
    
    public int getReminderDays() { return reminderDays; }
    public void setReminderDays(int reminderDays) { this.reminderDays = reminderDays; }
}
