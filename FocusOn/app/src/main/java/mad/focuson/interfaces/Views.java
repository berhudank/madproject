package mad.focuson.interfaces;

import mad.focuson.Task;

public interface Views {
    interface MainActivityView{
        void updateTaskName(String taskName);
        void updateProgress(int progress);
        void updateTimer(String time);
    }
    interface TasksActivityView{}
}
