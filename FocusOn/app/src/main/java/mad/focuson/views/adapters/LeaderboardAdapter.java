package mad.focuson.views.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import mad.focuson.R;

public class LeaderboardAdapter extends BaseAdapter {
    private final Activity activity;
    private final ArrayList<Map<String, Object>> leaderboard;

    public LeaderboardAdapter(Activity activity, ArrayList<Map<String, Object>> leaderboard) {
        this.activity = activity;
        this.leaderboard = leaderboard;
    }

    @Override
    public int getCount() {
        return leaderboard.size();
    }

    @Override
    public Object getItem(int position) {
        return leaderboard.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View rowView;
        rowView= activity.getLayoutInflater().inflate(R.layout.leaderboard_row_item,null);
        TextView txtUserName = rowView.findViewById(R.id.txtUserName);
        TextView txtTotalHours = rowView.findViewById(R.id.txtTotalHours);

        // Get the current item
        Map<String, Object> currentItem = leaderboard.get(position);
        txtUserName.setText(currentItem.get("userId").toString());
        txtTotalHours.setText(currentItem.get("score") != null ? currentItem.get("score").toString() : "0");

        return rowView;
    }
}
