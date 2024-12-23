package mad.focuson.interfaces;

import mad.focuson.Task;
import java.util.List;

public interface Views {
    interface MainView {
        void updateTimer(String time);
        void updateTaskName(String taskName);
        void showBreakMessage(boolean isLongBreak);
        void updatePomodoroCount(int count);
        void playTimerEndSound();
        void enableStartButton(boolean enable);
        void enablePauseButton(boolean enable);
        void enableStopButton(boolean enable);
        void setTask(Task task);
        Task getTask();
        void navigateToTasks();
        void navigateToLeaderboard();
        void navigateToProfile();
        void navigateToSettings();
    }

    interface TasksView {
        void showTasks(List<Task> tasks);
        void showAddTaskDialog();
        void showTaskDetails(Task task);
        void showTaskUpdated();
        void showTaskDeleted();
    }
}
