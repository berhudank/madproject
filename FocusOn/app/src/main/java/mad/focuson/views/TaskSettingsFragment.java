package mad.focuson.views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import mad.focuson.R;
import mad.focuson.Task;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TaskSettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TaskSettingsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_TASK = "task";

    // TODO: Rename and change types of parameters
    private Task task;

    public TaskSettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param task Parameter 1.
     *
     * @return A new instance of fragment TaskSettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TaskSettingsFragment newInstance(Task task) {
        TaskSettingsFragment fragment = new TaskSettingsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_TASK, task);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            task = (Task) getArguments().getSerializable(ARG_TASK);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_task_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Toast.makeText(view.getContext(), "task: " + task, Toast.LENGTH_LONG).show();
    }
}