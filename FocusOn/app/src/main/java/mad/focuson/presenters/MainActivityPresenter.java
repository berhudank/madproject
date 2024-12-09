package mad.focuson.presenters;

import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;

import mad.focuson.Task;
import mad.focuson.interfaces.ModelListener;
import mad.focuson.interfaces.Views;


public class MainActivityPresenter implements ModelListener, View.OnClickListener, Chronometer.OnChronometerTickListener {
    Views.MainActivityView mainActivityView;
    Task selectedTask;
    public MainActivityPresenter(Views.MainActivityView mainActivityView){
        this.mainActivityView = mainActivityView;
    }

    @Override
    public void onClick(View v) {
        Button btnStartStop = (Button) v;

    }


    @Override
    public void sendSelectedTask(Task selectedTask) {
        mainActivityView.setTask(selectedTask);
        this.selectedTask = selectedTask;
    }

    @Override
    public void onChronometerTick(Chronometer chronometer) {

        if(selectedTask.getTime() == 0){
            if (chronometer.isTheFinalCountDown()){
                chronometer.stop();
            }else {
                chronometer.setBase(selectedTask.getBreakTime());
                chronometer.start();
                selectedTask.setNumberOfSession(selectedTask.getNumberOfSession()-1);
            }
        }
        selectedTask.decrementTime();
    }
}
