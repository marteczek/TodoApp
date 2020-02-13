package com.marteczek.todoapp.database.entity.type;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.marteczek.todoapp.database.entity.type.TaskCategory.*;

@StringDef({WORK, SHOPPING, OTHER})
@Retention(RetentionPolicy.SOURCE)
public @interface TaskCategory {
    String WORK = "category_work";
    String SHOPPING = "category_shopping";
    String OTHER = "category_other";
}
