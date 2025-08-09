package com.example.taskmanagerpro.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskmanagerpro.R;
import com.example.taskmanagerpro.model.Task;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.VH> {
    public interface Listener {
        void onEdit(Task task);
        void onDelete(Task task);
        void onToggleDone(Task task);
    }

    private Context ctx;
    private List<Task> tasks;
    private Listener listener;

    public TaskAdapter(Context ctx, List<Task> tasks, Listener listener) {
        this.ctx = ctx;
        this.tasks = tasks;
        this.listener = listener;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.item_task, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Task t = tasks.get(position);
        holder.title.setText(t.getTitle());
        holder.desc.setText(t.getDescription());
        if (t.getDueDateMillis() > 0) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
            holder.due.setText(sdf.format(new Date(t.getDueDateMillis())));
        } else {
            holder.due.setText("Sin fecha");
        }
        holder.done.setChecked(t.isDone());
        String priorityLabel = "Baja";
        if (t.getPriority() == 1) priorityLabel = "Media";
        if (t.getPriority() == 2) priorityLabel = "Alta";
        holder.priority.setText(priorityLabel);

        holder.editBtn.setOnClickListener(v -> listener.onEdit(t));
        holder.deleteBtn.setOnClickListener(v -> listener.onDelete(t));
        holder.done.setOnCheckedChangeListener((buttonView, isChecked) -> {
            t.setDone(isChecked);
            listener.onToggleDone(t);
        });
    }

    @Override
    public int getItemCount() { return tasks.size(); }

    public void updateList(List<Task> data) {
        this.tasks = data;
        notifyDataSetChanged();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView title, desc, due, priority;
        CheckBox done;
        ImageButton editBtn, deleteBtn;

        VH(@NonNull View v) {
            super(v);
            title = v.findViewById(R.id.taskTitle);
            desc = v.findViewById(R.id.taskDesc);
            due = v.findViewById(R.id.taskDue);
            priority = v.findViewById(R.id.taskPriority);
            done = v.findViewById(R.id.taskDone);
            editBtn = v.findViewById(R.id.btnEdit);
            deleteBtn = v.findViewById(R.id.btnDelete);
        }
    }
}