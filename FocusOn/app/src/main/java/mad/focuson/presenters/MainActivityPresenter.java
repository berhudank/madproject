package mad.focuson.presenters;

import android.view.View;

import mad.focuson.interfaces.ModelListener;
import mad.focuson.interfaces.Views;


public class MainActivityPresenter implements ModelListener, View.OnClickListener {
    Views.MainActivityView mainActivityView;
    public MainActivityPresenter(Views.MainActivityView mainActivityView){
        this.mainActivityView = mainActivityView;
    }

    @Override
    public void onClick(View v) {

    }
}
