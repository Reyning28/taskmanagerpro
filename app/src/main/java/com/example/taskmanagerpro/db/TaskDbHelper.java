package com.example.taskmanagerpro.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.taskmanagerpro.model.Task;

import java.util.ArrayList;
import java.util.List;

public class TaskDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "taskmanager.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE = "tasks";
    public static final String COL_ID = "id";
    public static final String COL_TITLE = "title";
    public static final String COL_DESC = "description";
    public static final String COL_DUE = "due";
    public static final String COL_PRIORITY = "priority";
    public static final String COL_DONE = "done";

    public TaskDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_TITLE + " TEXT, " +
                COL_DESC + " TEXT, " +
                COL_DUE + " INTEGER, " +
                COL_PRIORITY + " INTEGER, " +
                COL_DONE + " INTEGER" +
                ");";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }

    // CRUD
    public long insertTask(Task t) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_TITLE, t.getTitle());
        cv.put(COL_DESC, t.getDescription());
        cv.put(COL_DUE, t.getDueDateMillis());
        cv.put(COL_PRIORITY, t.getPriority());
        cv.put(COL_DONE, t.isDone() ? 1 : 0);
        long id = db.insert(TABLE, null, cv);
        db.close();
        return id;
    }

    public int updateTask(Task t) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_TITLE, t.getTitle());
        cv.put(COL_DESC, t.getDescription());
        cv.put(COL_DUE, t.getDueDateMillis());
        cv.put(COL_PRIORITY, t.getPriority());
        cv.put(COL_DONE, t.isDone() ? 1 : 0);
        int rows = db.update(TABLE, cv, COL_ID + "=?", new String[]{String.valueOf(t.getId())});
        db.close();
        return rows;
    }

    public int deleteTask(long id) {
        SQLiteDatabase db = getWritableDatabase();
        int rows = db.delete(TABLE, COL_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
        return rows;
    }

    public List<Task> getAllTasks() {
        List<Task> out = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE, null, null, null, null, null, COL_PRIORITY + " DESC, " + COL_DUE + " ASC");
        while (c.moveToNext()) {
            Task t = new Task();
            t.setId(c.getLong(c.getColumnIndexOrThrow(COL_ID)));
            t.setTitle(c.getString(c.getColumnIndexOrThrow(COL_TITLE)));
            t.setDescription(c.getString(c.getColumnIndexOrThrow(COL_DESC)));
            t.setDueDateMillis(c.getLong(c.getColumnIndexOrThrow(COL_DUE)));
            t.setPriority(c.getInt(c.getColumnIndexOrThrow(COL_PRIORITY)));
            t.setDone(c.getInt(c.getColumnIndexOrThrow(COL_DONE)) == 1);
            out.add(t);
        }
        c.close();
        db.close();
        return out;
    }
}