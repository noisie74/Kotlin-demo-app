package michael.com.kotlindemo.java.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import michael.com.kotlindemo.R;
import michael.com.kotlindemo.java.model.Task;

import static com.google.common.base.Preconditions.checkNotNull;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private List<Task> mTasks;
    public OnItemClickListener clickListener;


    public void setOnItemClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }


    public TaskAdapter(List<Task> tasks) {
        setList(tasks);
    }

    public void replaceData(List<Task> tasks) {
        setList(tasks);
        notifyDataSetChanged();
    }

    private void setList(List<Task> tasks) {
        mTasks = checkNotNull(tasks);

    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView taskText;
        private CheckBox taskCompletedCheckBox;


        public ViewHolder(final View view) {
            super(view);
            this.taskText = (TextView) view.findViewById(R.id.task_title);
            this.taskCompletedCheckBox = (CheckBox) view.findViewById(R.id.complete);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clickListener != null)
                        clickListener.onItemClick(view, getLayoutPosition());
                }
            });


        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        Task data = mTasks.get(position);

        String title = data.getTaskTitle();
        boolean taskCompleted = data.isCompleted();

        holder.taskText.setText(title);

        if (taskCompleted) {
            holder.taskCompletedCheckBox.setChecked(true);
            holder.taskCompletedCheckBox.setClickable(false);
        } else {
            holder.taskCompletedCheckBox.setChecked(false);
            holder.taskCompletedCheckBox.setClickable(true);
        }

    }

    @Override
    public int getItemCount() {
        return mTasks.size();
    }

    interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }
}
