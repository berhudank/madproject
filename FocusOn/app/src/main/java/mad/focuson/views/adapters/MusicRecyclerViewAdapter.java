package mad.focuson.views.adapters;

import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import mad.focuson.R;
import mad.focuson.views.placeholder.PlaceholderContent.PlaceholderItem;

import java.util.ArrayList;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PlaceholderItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MusicRecyclerViewAdapter extends RecyclerView.Adapter<MusicRecyclerViewAdapter.ViewHolder> {

    private final ArrayList<Integer> musics;
    private final ArrayList<String> musicNames;
    private final OnMusicClickListener listener;
    public MusicRecyclerViewAdapter(OnMusicClickListener listener, ArrayList<Integer> musics, ArrayList<String> musicNames) {
        this.musics = musics;
        this.musicNames = musicNames;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_music, parent, false);
        return new MusicRecyclerViewAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mId = musics.get(position);
        holder.mMusicNameView.setText(musicNames.get(position));
        // set OnClickListener to this row. When user taps on any region on the row, onClick should be called
        ((View) holder.mMusicNameView.getParent()).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onMusicClick(holder.mId);
            }
        });

    }

    @Override
    public int getItemCount() {
        return musics.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public int mId;
        public final TextView mMusicNameView;

        public ViewHolder(View view) {
            super(view);
            mMusicNameView = view.findViewById(R.id.musicName);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mMusicNameView.getText() + "'";
        }
    }


    public interface OnMusicClickListener {
        void onMusicClick(int musicId);
    }
}