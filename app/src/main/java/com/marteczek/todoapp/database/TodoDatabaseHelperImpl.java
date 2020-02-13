package com.marteczek.todoapp.database;

import android.content.Context;

public class TodoDatabaseHelperImpl implements TodoDatabaseHelper {

    private final TodoDatabase db;

    public TodoDatabaseHelperImpl(Context context) {
        db = TodoDatabase.getDatabase(context);
    }

    @Override
    public void execute(Runnable body) {
        TodoDatabase.databaseWriteExecutor.execute(body);
    }

}
