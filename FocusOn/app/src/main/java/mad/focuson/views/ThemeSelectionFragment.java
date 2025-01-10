package mad.focuson.views;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;

import mad.focuson.R;
import mad.focuson.views.adapters.ThemeAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ThemeSelectionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ThemeSelectionFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "themes";

    // TODO: Rename and change types of parameters
    private ArrayList<Integer> themes;

    public ThemeSelectionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ThemeSelectionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ThemeSelectionFragment newInstance(ArrayList<Integer> themes) {
        ThemeSelectionFragment fragment = new ThemeSelectionFragment();
        Bundle args = new Bundle();
        args.putIntegerArrayList(ARG_PARAM1, themes);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            themes = getArguments().getIntegerArrayList(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_theme_selection, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        GridView gridView = view.findViewById(R.id.gridViewThemes);
        ThemeAdapter adapter = new ThemeAdapter(this.getContext(), themes);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Handle theme selection
                Bundle bundle = new Bundle();
                bundle.putInt("selectedTheme", themes.get(position));

                // The child fragment needs to still set the result on its parent fragment manager.
                getParentFragmentManager().setFragmentResult("response", bundle);
            }
        });
    }
}