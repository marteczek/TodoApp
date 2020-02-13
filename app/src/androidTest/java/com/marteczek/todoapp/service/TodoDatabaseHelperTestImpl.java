package com.marteczek.todoapp.service;

import com.marteczek.todoapp.database.TodoDatabaseHelper;

public class TodoDatabaseHelperTestImpl implements TodoDatabaseHelper {

    @Override
    public void execute(Runnable body) {
        body.run();
    }
}
