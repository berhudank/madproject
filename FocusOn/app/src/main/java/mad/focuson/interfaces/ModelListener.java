package mad.focuson.interfaces;

import java.util.ArrayList;

import mad.focuson.Task;

public interface ModelListener {
    void updateTaskList(ArrayList<Task> taskList);
}
