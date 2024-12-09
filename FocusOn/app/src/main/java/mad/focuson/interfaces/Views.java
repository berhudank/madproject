package mad.focuson.interfaces;

import mad.focuson.Task;

public interface Views {
    interface MainActivityView{
        public void setTask(Task task);
        public Task getTask();
    }
    interface TasksActivityView{}
}
