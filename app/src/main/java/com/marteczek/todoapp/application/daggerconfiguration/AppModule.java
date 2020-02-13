package com.marteczek.todoapp.application.daggerconfiguration;

import android.app.Application;

import com.marteczek.todoapp.activity.TodoEditorActivity;
import com.marteczek.todoapp.activity.TodoListActivity;
import com.marteczek.todoapp.application.TodoApplication;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
abstract class AppModule {

    @Binds
    abstract Application application(TodoApplication application);

    @ContributesAndroidInjector
    abstract TodoEditorActivity todoEditorActivity();

    @ContributesAndroidInjector
    abstract TodoListActivity todoListActivity();
}
