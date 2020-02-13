package com.marteczek.todoapp.database.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.marteczek.todoapp.database.entity.type.TaskCategory;

import java.util.Date;

@Entity(tableName = "todos")
public class Todo {

    @PrimaryKey(autoGenerate = true)
    private Long id;

    @NonNull
    private String name;

    @ColumnInfo(name="completion_date")
    private Date completionDate;

    @TaskCategory
    private String category;

    public Todo(@NonNull String name, Date completionDate, String category) {
        this.name = name;
        this.completionDate = completionDate;
        this.category = category;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public Date getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(Date doingDate) {
        this.completionDate = doingDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(@TaskCategory String category) {
        this.category = category;
    }
}
