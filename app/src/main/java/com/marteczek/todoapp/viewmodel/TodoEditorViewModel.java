package com.marteczek.todoapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.marteczek.todoapp.database.entity.Todo;
import com.marteczek.todoapp.service.SaveTodoStatus;
import com.marteczek.todoapp.service.TodoService;

public class TodoEditorViewModel extends AndroidViewModel {

    private TodoService todoService;

    public TodoEditorViewModel(@NonNull Application application, TodoService todoService) {
        super(application);
        this.todoService = todoService;
    }

    public LiveData<SaveTodoStatus> insertTodo(Todo todo) {
         return todoService.insertTodo(todo);
    }
}
