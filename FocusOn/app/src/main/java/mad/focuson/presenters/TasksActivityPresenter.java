package mad.focuson.presenters;

import android.view.View;

import mad.focuson.Model;
import mad.focuson.interfaces.ModelListener;
import mad.focuson.interfaces.Views;

public class TasksActivityPresenter implements ModelListener, View.OnClickListener {
    Views.TasksActivityView tasksActivityView;
    Model model;
    public TasksActivityPresenter(Views.TasksActivityView tasksActivityView){
        this.tasksActivityView = tasksActivityView;
        model = Model.getInstance();
    }
    @Override
    public void onClick(View v) {

    }
}
