package com.marteczek.todoapp.service;

import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.marteczek.todoapp.database.TodoDatabase;
import com.marteczek.todoapp.database.TodoDatabaseHelper;
import com.marteczek.todoapp.database.dao.TodoDao;
import com.marteczek.todoapp.database.entity.Todo;
import com.marteczek.todoapp.database.entity.type.TaskCategory;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class TodoServiceTest {

    private TodoDatabaseHelper databaseHelper = new TodoDatabaseHelperTestImpl();

    private TodoDatabase db;

    private TodoDao todoDao;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule =
            new InstantTaskExecutorRule();

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, TodoDatabase.class)
                .build();
        todoDao = db.todoDao();
    }

    @After
    public void closeDb() {
        db.close();
    }


    @Test
    public void insertTodo_oneTodoProvided_correctRowIsInTheDatabase() {
        //given
        String name = "name";
        Date completionDate = new Date(0);
        String category = TaskCategory.WORK;
        Todo todo = new Todo(name, completionDate, category);
        TodoService todoService = new TodoService(todoDao, databaseHelper);
        //when
        todoService.insertTodo(todo);
        //then
        List<Todo> todos = todoDao.findAll();
        assertEquals(1, todos.size());
        Todo newTodo = todos.get(0);
        assertEquals(name, newTodo.getName());
        assertEquals(completionDate, newTodo.getCompletionDate());
        assertEquals(category, newTodo.getCategory());
    }

    @Test
    public void findAllTodosAsync_thereIsOneRowInTheDatabase_oneRowIsReturned() {
        //given
        Todo todo = new Todo("name", null, null);
        todoDao.insert(todo);
        TodoService todoService = new TodoService(todoDao, databaseHelper);
        //when
        LiveData<List<Todo>> todosLiveData = todoService.findAllTodos();
        //then
        todosLiveData.observeForever(todos -> assertEquals(1, todos.size()));
    }
}
