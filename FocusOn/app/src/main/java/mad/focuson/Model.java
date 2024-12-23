package mad.focuson;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

// This should have only one instance throughout the app
public class Model {
    public static final int POMODORO_LENGTH = 25 * 60; // 25 minutes in seconds
    public static final int SHORT_BREAK_LENGTH = 5 * 60; // 5 minutes
    public static final int LONG_BREAK_LENGTH = 15 * 60; // 15 minutes
    private static final int POMODOROS_BEFORE_LONG_BREAK = 4;

    private List<Task> tasks;
    private Task currentTask;
    private int currentTime;
    private int pomodorosCompleted;
    private boolean isBreakTime;
    private boolean isRunning;
    private boolean isLongBreak;
    private SharedPreferences prefs;

    public Model(Context context) {
        prefs = context.getSharedPreferences("FocusOn", Context.MODE_PRIVATE);
        tasks = loadTasks();
        currentTime = POMODORO_LENGTH;
        pomodorosCompleted = prefs.getInt("pomodorosCompleted", 0);
        isBreakTime = false;
        isRunning = false;
        isLongBreak = false;
    }

    private List<Task> loadTasks() {
        List<Task> loadedTasks = new ArrayList<>();
        String tasksJson = prefs.getString("tasks", "");
        
        if (!tasksJson.isEmpty()) {
            try {
                JSONArray jsonArray = new JSONArray(tasksJson);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject taskJson = jsonArray.getJSONObject(i);
                    Task task = new Task(
                        taskJson.getString("title"),
                        taskJson.getInt("estimatedPomodoros")
                    );
                    task.setId(taskJson.getLong("id"));
                    task.setCompleted(taskJson.getBoolean("completed"));
                    
                    int completed = taskJson.getInt("pomodorosCompleted");
                    for (int j = 0; j < completed; j++) {
                        task.incrementPomodorosCompleted();
                    }
                    
                    loadedTasks.add(task);
                }
            } catch (JSONException e) {
                Log.e("Model", "Error loading tasks", e);
            }
        }
        return loadedTasks;
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public void setCurrentTask(Task task) {
        currentTask = task;
    }

    public void tick() {
        if (isRunning && currentTime > 0) {
            currentTime--;
        }
    }

    public void startTimer() {
        isRunning = true;
    }

    public void pauseTimer() {
        isRunning = false;
    }

    public void stopTimer() {
        isRunning = false;
        resetTimer();
    }

    public void resetTimer() {
        currentTime = isBreakTime ? 
            (pomodorosCompleted % POMODOROS_BEFORE_LONG_BREAK == 0 ? 
                LONG_BREAK_LENGTH : SHORT_BREAK_LENGTH) 
            : POMODORO_LENGTH;
    }

    public boolean isTimerComplete() {
        return currentTime <= 0;
    }

    public void handleTimerComplete() {
        if (!isBreakTime) {
            pomodorosCompleted++;
            if (currentTask != null) {
                currentTask.incrementPomodorosCompleted();
            }
        }
        isBreakTime = !isBreakTime;
        resetTimer();
    }

    public void savePomodoroCount() {
        prefs.edit().putInt("pomodorosCompleted", pomodorosCompleted).apply();
    }

    public void saveTasks() {
        JSONArray jsonArray = new JSONArray();
        for (Task task : tasks) {
            try {
                JSONObject taskJson = new JSONObject();
                taskJson.put("id", task.getId());
                taskJson.put("title", task.getTitle());
                taskJson.put("estimatedPomodoros", task.getEstimatedPomodoros());
                taskJson.put("pomodorosCompleted", task.getPomodorosCompleted());
                taskJson.put("completed", task.isCompleted());
                jsonArray.put(taskJson);
            } catch (JSONException e) {
                Log.e("Model", "Error saving task", e);
            }
        }
        prefs.edit().putString("tasks", jsonArray.toString()).apply();
    }

    public void startBreak() {
        isBreakTime = true;
        isLongBreak = pomodorosCompleted % POMODOROS_BEFORE_LONG_BREAK == 0;
        currentTime = isLongBreak ? LONG_BREAK_LENGTH : SHORT_BREAK_LENGTH;
    }

    public boolean isLongBreak() {
        return isLongBreak;
    }

    // Getters
    public int getCurrentTime() { return currentTime; }
    public boolean isBreakTime() { return isBreakTime; }
    public boolean isRunning() { return isRunning; }
    public List<Task> getTasks() { return tasks; }
    public Task getCurrentTask() { return currentTask; }
    public int getPomodorosCompleted() { return pomodorosCompleted; }
}
