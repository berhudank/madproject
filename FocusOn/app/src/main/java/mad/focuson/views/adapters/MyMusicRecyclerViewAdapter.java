package mad.focuson.views.adapters;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import android.view.View;

import mad.focuson.Music;
import mad.focuson.R;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Music}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyMusicRecyclerViewAdapter extends RecyclerView.Adapter<MyMusicRecyclerViewAdapter.ViewHolder> {

    private final List<Music> mValues;

    public MyMusicRecyclerViewAdapter(List<Music> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View musicView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_music, parent, false);
        return new ViewHolder(musicView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        //holder.mIdView.setText(mValues.get(position).id);
        //holder.mContentView.setText(mValues.get(position).content);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //public final TextView mIdView;
        //public final TextView mContentView;
        public Music mItem;
        public final View mView;

        public ViewHolder(View musicView) {
            super(musicView);
            mView = musicView;
            //mIdView = musicView.itemNumber;
            //mContentView = musicView.content;
        }

        @Override
        public String toString() {
            return super.toString() + " '"  /*+ mContentView.getText() + "'"*/;
        }
    }
}