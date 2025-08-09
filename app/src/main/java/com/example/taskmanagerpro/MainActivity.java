package com.example.taskmanagerpro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskmanagerpro.adapter.TaskAdapter;
import com.example.taskmanagerpro.db.TaskDbHelper;
import com.example.taskmanagerpro.model.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TaskDbHelper db;
    private TaskAdapter adapter;
    private List<Task> tasks;
    private static final int REQ_ADD = 1001;
    private static final int REQ_EDIT = 1002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new TaskDbHelper(this);
        RecyclerView rv = findViewById(R.id.recycler);
        rv.setLayoutManager(new LinearLayoutManager(this));

        tasks = db.getAllTasks();
        adapter = new TaskAdapter(this, tasks, new TaskAdapter.Listener() {
            @Override
            public void onEdit(Task task) {
                Intent i = new Intent(MainActivity.this, AddEditTaskActivity.class);
                i.putExtra("task", task);
                startActivityForResult(i, REQ_EDIT);
            }

            @Override
            public void onDelete(Task task) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Eliminar tarea")
                        .setMessage("¿Eliminar \"" + task.getTitle() + "\"?")
                        .setPositiveButton("Sí", (d, w) -> {
                            db.deleteTask(task.getId());
                            refresh();
                        })
                        .setNegativeButton("No", null)
                        .show();
            }

            @Override
            public void onToggleDone(Task task) {
                db.updateTask(task);
            }
        });
        rv.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.fabAdd);
        fab.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, AddEditTaskActivity.class);
            startActivityForResult(i, REQ_ADD);
        });
    }

    private void refresh() {
        tasks = db.getAllTasks();
        adapter.updateList(tasks);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        refresh();



    }
}