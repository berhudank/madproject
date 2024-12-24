package mad.focuson.interfaces;

import android.content.Intent;

import mad.focuson.Task;

public interface Views {
    interface MainActivityView{
        void updateTaskName(String taskName);
        void updateProgress(int progress);
        void updateTimer(String time);
    }
    interface TasksActivityView{
        void returnResult(int resultCode, Intent intent);
        void sendToSettings(Task selectedTask);
    }
}
