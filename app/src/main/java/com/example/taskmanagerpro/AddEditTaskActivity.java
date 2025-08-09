package com.example.taskmanagerpro;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.taskmanagerpro.db.TaskDbHelper;
import com.example.taskmanagerpro.model.Task;

import java.util.Calendar;

public class AddEditTaskActivity extends AppCompatActivity {
    private EditText etTitle, etDesc;
    private TextView tvDue;
    private Spinner spPriority;
    private Button btnSave, btnClearDate;
    private TaskDbHelper db;
    private Task editing;
    private long dueMillis = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_task);

        etTitle = findViewById(R.id.etTitle);
        etDesc = findViewById(R.id.etDesc);
        tvDue = findViewById(R.id.tvDue);
        spPriority = findViewById(R.id.spPriority);
        btnSave = findViewById(R.id.btnSave);
        btnClearDate = findViewById(R.id.btnClearDate);

        db = new TaskDbHelper(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.priorities, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPriority.setAdapter(adapter);

        if (getIntent().hasExtra("task")) {
            editing = (Task) getIntent().getSerializableExtra("task");
            etTitle.setText(editing.getTitle());
            etDesc.setText(editing.getDescription());
            dueMillis = editing.getDueDateMillis();
            if (dueMillis > 0) tvDue.setText(android.text.format.DateFormat.getDateFormat(this).format(dueMillis));
            spPriority.setSelection(editing.getPriority());
        }

        tvDue.setOnClickListener(v -> showDatePicker());
        btnClearDate.setOnClickListener(v -> { dueMillis = 0; tvDue.setText("Sin fecha"); });

        btnSave.setOnClickListener(v -> {
            String title = etTitle.getText().toString().trim();
            if (title.isEmpty()) { etTitle.setError("Ingrese título"); return; }
            String desc = etDesc.getText().toString().trim();
            int pr = spPriority.getSelectedItemPosition();

            if (editing == null) {
                Task t = new Task();
                t.setTitle(title);
                t.setDescription(desc);
                t.setDueDateMillis(dueMillis);
                t.setPriority(pr);
                t.setDone(false);
                db.insertTask(t);
            } else {
                editing.setTitle(title);
                editing.setDescription(desc);
                editing.setDueDateMillis(dueMillis);
                editing.setPriority(pr);
                db.updateTask(editing);
            }
            setResult(RESULT_OK);
            finish();
        });
    }

    private void showDatePicker() {
        Calendar c = Calendar.getInstance();
        DatePickerDialog dpd = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            Calendar sel = Calendar.getInstance();
            sel.set(year, month, dayOfMonth, 0, 0);
            dueMillis = sel.getTimeInMillis();
            tvDue.setText(android.text.format.DateFormat.getDateFormat(this).format(dueMillis));
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        dpd.show();
    }
}