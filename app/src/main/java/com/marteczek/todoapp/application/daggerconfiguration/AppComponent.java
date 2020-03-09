package com.marteczek.todoapp.application.daggerconfiguration;

import com.marteczek.todoapp.application.TodoApplication;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;

@Singleton
@Component(modules = {AndroidInjectionModule.class,
        AppModule.class,
        DBModule.class,
//        ServiceModule.class,
        ViewModelModule.class})
public interface AppComponent extends AndroidInjector<TodoApplication> {

    @Component.Factory
    interface Factory extends AndroidInjector.Factory<TodoApplication> {
    }
}
