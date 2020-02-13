package com.marteczek.todoapp.application.daggerconfiguration;

import com.marteczek.todoapp.database.TodoDatabaseHelper;
import com.marteczek.todoapp.database.dao.TodoDao;
import com.marteczek.todoapp.service.TodoService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ServiceModule {

    @Singleton
    @Provides
    TodoService todoService(TodoDao todoDao, TodoDatabaseHelper todoDatabaseHelper) {
        return new TodoService(todoDao, todoDatabaseHelper);
    }
}
