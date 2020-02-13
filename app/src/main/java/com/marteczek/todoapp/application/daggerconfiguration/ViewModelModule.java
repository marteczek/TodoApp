package com.marteczek.todoapp.application.daggerconfiguration;

import android.app.Application;

import androidx.lifecycle.ViewModel;

import com.marteczek.todoapp.application.daggerconfiguration.viewmodelfactory.ViewModelFactory;
import com.marteczek.todoapp.service.TodoService;
import com.marteczek.todoapp.viewmodel.TodoEditorViewModel;
import com.marteczek.todoapp.viewmodel.TodoListViewModel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;

import javax.inject.Provider;

import dagger.MapKey;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;

@Module
class ViewModelModule {

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @MapKey
    @interface ViewModelKey {
        Class<? extends ViewModel> value();
    }

    @Provides
    ViewModelFactory viewModelFactory(Map<Class<? extends ViewModel>, Provider<ViewModel>> providerMap) {
        return new ViewModelFactory(providerMap);
    }

    @Provides
    @IntoMap
    @ViewModelKey(TodoEditorViewModel.class)
    ViewModel todoEditorViewModel(Application application, TodoService todoService) {
        return new TodoEditorViewModel(application, todoService);
    }

    @Provides
    @IntoMap
    @ViewModelKey(TodoListViewModel.class)
    ViewModel todoListViewModel(Application application, TodoService todoService) {
        return new TodoListViewModel(application, todoService);
    }
}
