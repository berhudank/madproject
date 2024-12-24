package mad.focuson.views.adapters;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import mad.focuson.R;
import mad.focuson.Task;
import mad.focuson.interfaces.Views;

public class TaskRecyclerViewAdapter extends RecyclerView.Adapter<TaskRecyclerViewAdapter.ViewHolder> {
    private final List<Task> tasks;
    Views.TasksActivityView tasksActivityView;
    public TaskRecyclerViewAdapter(List<Task> tasks, Views.TasksActivityView tasksActivityView) {
        this.tasks = tasks;
        this.tasksActivityView = tasksActivityView;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_row_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Task task = tasks.get(position);
        holder.mItem = task;
        holder.mTaskNameView.setText(task.getTaskName());
        holder.mSessionInfoView.setText(task.getNumberOfSessions() - task.getSessionsLeft() + "/" + task.getNumberOfSessions());
        holder.mTaskStatusView.setImageResource(task.isFinished() ? R.drawable.ic_task_completed : R.drawable.ic_task);

        if(!task.isFinished()) {
            holder.mSendButtonView.setVisibility(View.VISIBLE);
            holder.mSendButtonView.setImageResource(R.drawable.ic_send_task);
            holder.mSendButtonView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // setResult() ... and finish
                    tasksActivityView.returnResult(Activity.RESULT_OK, new Intent().putExtra("selectedTask", task));
                }
            });
            if(task.getWorkDuration() == task.getRemainingWorkDuration()){
                holder.mEditButtonView.setVisibility(View.VISIBLE);
                holder.mEditButtonView.setImageResource(R.drawable.ic_edit_task);
                holder.mEditButtonView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // start TaskSettingsFragment...
                        tasksActivityView.sendToSettings(task);
                    }
                });
            }
        }
        else {
            holder.mSendButtonView.setVisibility(View.INVISIBLE);
            holder.mEditButtonView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public final View mView;
        public final TextView mTaskNameView;
        public final TextView mSessionInfoView;
        public final ImageView mTaskStatusView;
        public final ImageButton mEditButtonView;
        public final ImageButton mSendButtonView;
        public Task mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTaskNameView = view.findViewById(R.id.txtTaskName);
            mSessionInfoView = view.findViewById(R.id.txtSessionInfo);
            mTaskStatusView = view.findViewById(R.id.imgTaskStatus);
            mEditButtonView = view.findViewById(R.id.imgBtnEditTask);
            mSendButtonView = view.findViewById(R.id.imgBtnSendTask);
        }

//        @Override
//        public String toString() {
//            return super.toString() + " '" + mHeaderView.getText() + "'";
//        }
    }
}
