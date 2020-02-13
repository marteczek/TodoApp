package com.marteczek.todoapp.application.daggerconfiguration;

import android.app.Application;

import com.marteczek.todoapp.database.TodoDatabase;
import com.marteczek.todoapp.database.TodoDatabaseHelper;
import com.marteczek.todoapp.database.TodoDatabaseHelperImpl;
import com.marteczek.todoapp.database.dao.TodoDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
class DBModule {

    @Singleton
    @Provides
    TodoDatabase todoDatabase(Application application) {
        return TodoDatabase.getDatabase(application.getApplicationContext());
    }

    @Singleton
    @Provides
    TodoDatabaseHelper todoDatabaseHelper(Application application) {
        return new TodoDatabaseHelperImpl(application.getApplicationContext());
    }

    @Singleton
    @Provides
    TodoDao todoDao(TodoDatabase todoDatabase) {
        return todoDatabase.todoDao();
    }
}
