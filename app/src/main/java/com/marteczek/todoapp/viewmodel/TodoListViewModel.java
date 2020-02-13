package com.marteczek.todoapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.marteczek.todoapp.database.entity.Todo;
import com.marteczek.todoapp.service.TodoService;

import java.util.List;

public class TodoListViewModel extends AndroidViewModel {

    private final TodoService todoService;

    private LiveData<List<Todo>> todos;

    public TodoListViewModel(@NonNull Application application, TodoService todoService) {
        super(application);
        this.todoService = todoService;
    }

    public LiveData<List<Todo>> getTodos() {
        if (todos == null) {
            todos = todoService.findAllTodos();
        }
        return todos;
    }

    public void deleteTodo(Todo todo) {
        todoService.deleteTodo(todo);
    }
}
