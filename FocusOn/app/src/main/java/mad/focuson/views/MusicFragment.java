package mad.focuson.views;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import mad.focuson.R;
import mad.focuson.views.adapters.MusicRecyclerViewAdapter;
import mad.focuson.views.placeholder.PlaceholderContent;

/**
 * A fragment representing a list of Items.
 */
public class MusicFragment extends Fragment implements MusicRecyclerViewAdapter.OnMusicClickListener {

    // TODO: Customize parameter argument names
    private static final String ARG_MUSIC_LIST = "music-list";
    private static final String ARG_MUSIC_NAME_LIST = "music-name-list";
    // TODO: Customize parameters
    private ArrayList<Integer> musics;
    private ArrayList<String> musicNames;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MusicFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static MusicFragment newInstance(ArrayList<Integer> musics, ArrayList<String> musicNames) {
        MusicFragment fragment = new MusicFragment();
        Bundle args = new Bundle();
        args.putIntegerArrayList(ARG_MUSIC_LIST, musics);
        args.putStringArrayList(ARG_MUSIC_NAME_LIST, musicNames);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            musics = getArguments().getIntegerArrayList(ARG_MUSIC_LIST);
            musicNames = getArguments().getStringArrayList(ARG_MUSIC_NAME_LIST);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_music_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;

            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new MusicRecyclerViewAdapter(this, musics, musicNames));
        }
    }

    @Override
    public void onMusicClick(int musicId) {
        Bundle bundle = new Bundle();
        bundle.putInt("selectedMusic", musicId);

        // The child fragment needs to still set the result on its parent fragment manager.
        getParentFragmentManager().setFragmentResult("response", bundle);
    }
}