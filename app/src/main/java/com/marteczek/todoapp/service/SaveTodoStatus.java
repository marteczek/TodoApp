package com.marteczek.todoapp.service;

import com.marteczek.todoapp.database.entity.Todo;

public class SaveTodoStatus {

    private final boolean success;
    private final Todo todo;

    public SaveTodoStatus(boolean success, Todo todo) {
        this.success = success;
        this.todo = todo;
    }

    public boolean isSuccess() {
        return success;
    }

    public Todo getTodo() {
        return todo;
    }
}
