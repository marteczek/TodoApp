package com.marteczek.todoapp.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.marteczek.todoapp.R;
import com.marteczek.todoapp.application.daggerconfiguration.viewmodelfactory.ViewModelFactory;
import com.marteczek.todoapp.database.entity.Todo;
import com.marteczek.todoapp.service.SaveTodoStatus;
import com.marteczek.todoapp.viewmodel.TodoEditorViewModel;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class TodoEditorActivity extends AppCompatActivity {

    private static final String STATE_COMPLETION_DATE = "state_completed_date";
    private static final String DIALOG_COMPLETION_DATE = "dialog_completion_date";

    private TodoEditorViewModel viewModel;

    private EditText todoNameEditText;

    private EditText completionDateEditText;

    private Spinner categorySpinner;

    private Date completionDate;

    @Inject
    ViewModelFactory viewModelFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_editor);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        viewModel = new ViewModelProvider(this, viewModelFactory).get(TodoEditorViewModel.class);
        bindViewModelData();
        bindUIViews();
        configureCategorySpinner();
    }

    private void bindViewModelData() {
        LiveData<SaveTodoStatus> lastInsertTodoStatus = viewModel.getLastInsertTodoStatus();
        if (lastInsertTodoStatus != null) {
            viewModel.getLastInsertTodoStatus().observe(this, this::onTodoSavingResult);
        }
    }

    private void bindUIViews() {
        todoNameEditText = findViewById(R.id.todoName);
        completionDateEditText = findViewById(R.id.completionDate);
        categorySpinner = findViewById(R.id.category);
        completionDateEditText.setOnClickListener(this::enterCompletionDate);
        Button saveButton = findViewById(R.id.save);
        saveButton.setOnClickListener(this::saveTodo);
        Button cancelButton = findViewById(R.id.cancel);
        cancelButton.setOnClickListener(this::cancel);
    }

    private void configureCategorySpinner() {
        ArrayAdapter<TodoEditorViewModel.CategoryItem> categoryAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, new ArrayList<>(viewModel.getAllCategories(this)));
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);
    }

    private void enterCompletionDate(View view) {
        DialogFragment completionDateDialog = new DatePickerDialogFragment(completionDate);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        completionDateDialog.show(transaction, DIALOG_COMPLETION_DATE);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (completionDate != null) {
            outState.putLong(STATE_COMPLETION_DATE, completionDate.getTime());
        }
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        long date = savedInstanceState.getLong(STATE_COMPLETION_DATE, -1L);
        if (date != -1L) {
            updateCompletionDate(new Date(date));
        }
    }

    private void updateCompletionDate(Date date) {
        completionDate = date;
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getApplicationContext());
        completionDateEditText.setText(dateFormat.format(completionDate));
    }

    private void saveTodo(View view) {
        String todoName = todoNameEditText.getText().toString();
        if (!todoName.isEmpty()) {
            TodoEditorViewModel.CategoryItem categoryItem =
                    (TodoEditorViewModel.CategoryItem) categorySpinner.getSelectedItem();
            String category = categoryItem == null ? null : categoryItem.getKey();
            Todo todo = new Todo(todoName, completionDate, category);
            viewModel.insertTodo(todo).observe(this, this::onTodoSavingResult);
        } else {
            Toast.makeText(this, R.string.fill_todo_name, Toast.LENGTH_SHORT).show();
        }
    }

    private void onTodoSavingResult(SaveTodoStatus status) {
        if (status.isSuccess()) {
            Toast.makeText(this, R.string.todo_has_been_added, Toast.LENGTH_SHORT).show();
            finish();
        } else {
            showDialogRetryTodoSaving(status.getTodo());
        }
    }

    private void showDialogRetryTodoSaving(Todo todo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.saving)
                .setMessage(R.string.question_retry_save)
                .setPositiveButton(R.string.yes, (di, i) ->
                    viewModel.insertTodo(todo).observe(this, this::onTodoSavingResult))
                .setNegativeButton(R.string.no, (di, i) -> finish())
                .show();
    }

    private void cancel(View view) {
        finish();
    }

    public static class DatePickerDialogFragment extends DialogFragment {
        private Date completionDate;

        public DatePickerDialogFragment() {
        }

        private DatePickerDialogFragment(Date completionDate) {
            this.completionDate = completionDate;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            TodoEditorActivity activity = (TodoEditorActivity) getActivity();
            final Calendar calendar = Calendar.getInstance();
            if (completionDate != null) {
                calendar.setTime(completionDate);
            }
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            assert activity != null;
            return new DatePickerDialog(activity,
                    (v, y, m, d) -> activity.updateCompletionDate(new GregorianCalendar(y, m, d).getTime()),
                    year, month, day);
        }
    }
}
