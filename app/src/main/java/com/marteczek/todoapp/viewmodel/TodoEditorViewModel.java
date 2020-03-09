package com.marteczek.todoapp.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.marteczek.todoapp.R;
import com.marteczek.todoapp.database.entity.Todo;
import com.marteczek.todoapp.database.entity.type.TaskCategory;
import com.marteczek.todoapp.service.SaveTodoStatus;
import com.marteczek.todoapp.service.TodoService;

import java.util.Arrays;
import java.util.List;

public class TodoEditorViewModel extends AndroidViewModel {

    private TodoService todoService;
    private LiveData<SaveTodoStatus> lastInsertTodoStatus = null;

    public TodoEditorViewModel(@NonNull Application application, TodoService todoService) {
        super(application);
        this.todoService = todoService;
    }

    public LiveData<SaveTodoStatus> getLastInsertTodoStatus() {
        return lastInsertTodoStatus;
    }

    public LiveData<SaveTodoStatus> insertTodo(Todo todo) {
        lastInsertTodoStatus = todoService.insertTodo(todo);
        return lastInsertTodoStatus;
    }

    public List<CategoryItem> getAllCategories(Context context) {
        CategoryItem[] categoryItem = new CategoryItem[]{
            new CategoryItem(TaskCategory.OTHER, context.getString(R.string.category_other)),
            new CategoryItem(TaskCategory.WORK, context.getString(R.string.category_work)),
            new CategoryItem(TaskCategory.SHOPPING, context.getString(R.string.category_shopping))};
        return Arrays.asList(categoryItem);
    }

    public static class CategoryItem {
        private String key;
        private String text;

        CategoryItem(String key, String text) {
            this.key = key;
            this.text = text;
        }

        public String getKey() {
            return key;
        }

        @Override
        @NonNull public String toString() {
            return text;
        }
    }
}
