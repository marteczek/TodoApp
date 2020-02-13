package com.marteczek.todoapp.service;

import android.database.SQLException;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.marteczek.todoapp.database.TodoDatabaseHelper;
import com.marteczek.todoapp.database.dao.TodoDao;
import com.marteczek.todoapp.database.entity.Todo;

import java.util.List;

import static com.marteczek.todoapp.application.Configuration.Debug.E;

public class TodoService {

    private static final String TAG = "TodoService";

    private final TodoDao todoDao;

    private final TodoDatabaseHelper dbHelper;

    public TodoService(TodoDao todoDao, TodoDatabaseHelper todoDatabaseHelper) {
        this.todoDao = todoDao;
        this.dbHelper = todoDatabaseHelper;
    }

    public LiveData<SaveTodoStatus> insertTodo(final Todo todo) {
        MutableLiveData<SaveTodoStatus> status = new MutableLiveData<>();
        dbHelper.execute(() -> {
            try{
                long id = todoDao.insert(todo);
                long error = -1L;
                if (id != error) {
                    status.postValue(new SaveTodoStatus(true, todo));
                } else {
                    status.postValue(new SaveTodoStatus(false, todo));
                }
            } catch (SQLException e) {
                if (E) Log.e(TAG, "SQLException during row inserting");
                status.postValue(new SaveTodoStatus(false, todo));
            }
        });
        return status;
    }

    public void deleteTodo(final Todo todo) {
        dbHelper.execute(() -> {
            try{
                if (todo != null) {
                    todoDao.deleteById(todo.getId());
                }
            } catch (SQLException e) {
                if (E) Log.e(TAG, "SQLException during row deleting");
            }
        });
    }

    public LiveData<List<Todo>> findAllTodos() {
        return todoDao.findAllAsync();
    }
}
