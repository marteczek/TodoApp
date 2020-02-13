package com.marteczek.todoapp.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.marteczek.todoapp.database.entity.Todo;

import java.util.List;

@Dao
public interface TodoDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Long insert(Todo item);

    @Query("DELETE FROM todos WHERE id = :id")
    void deleteById(Long id);

    @Query("SELECT * FROM todos")
    List<Todo> findAll();

    @Query("SELECT * FROM todos")
    LiveData<List<Todo>> findAllAsync();
}
