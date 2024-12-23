package mad.focuson.presenters;

import android.view.View;

import mad.focuson.interfaces.ModelListener;
import mad.focuson.interfaces.Views;
import mad.focuson.Model;
import mad.focuson.Task;

public class TasksActivityPresenter implements ModelListener, View.OnClickListener {
    private Views.TasksView view;
    private Model model;

    public TasksActivityPresenter(Views.TasksView view, Model model) {
        this.view = view;
        this.model = model;
    }

    public void loadTasks() {
        view.showTasks(model.getTasks());
    }

    public void addNewTask(String title, int estimatedPomodoros) {
        Task task = new Task(title, estimatedPomodoros);
        model.addTask(task);
        loadTasks();
    }

    public void selectTask(Task task) {
        model.setCurrentTask(task);
        view.showTaskDetails(task);
    }

    public void showAddTaskDialog() {
        view.showAddTaskDialog();
    }

    public void deleteTask(Task task) {
        model.getTasks().remove(task);
        view.showTaskDeleted();
        loadTasks();
    }

    public void updateTask(Task task) {
        // Find and update the task in the model
        int index = model.getTasks().indexOf(task);
        if (index != -1) {
            model.getTasks().set(index, task);
            view.showTaskUpdated();
            loadTasks();
        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void sendSelectedTask(Task selectedTask) {

    }
}
