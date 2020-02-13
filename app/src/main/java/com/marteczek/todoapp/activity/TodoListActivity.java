package com.marteczek.todoapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.marteczek.todoapp.R;
import com.marteczek.todoapp.application.daggerconfiguration.viewmodelfactory.ViewModelFactory;
import com.marteczek.todoapp.viewmodel.TodoListViewModel;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

import static com.marteczek.todoapp.application.Configuration.Debug.D;
import static java.lang.Math.round;

public class TodoListActivity extends AppCompatActivity {

    private static final String TAG = "TodoListActivity";

    private TodoListViewModel viewModel;

    private TextView emptyListInfoTextView;

    @Inject
    ViewModelFactory viewModelFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        viewModel = new ViewModelProvider(this, viewModelFactory).get(TodoListViewModel.class);
        bindUIViews();
        configureRecyclerView();
    }

    private void bindUIViews() {
        emptyListInfoTextView = findViewById(R.id.empty_list_info);
        FloatingActionButton addButton = findViewById(R.id.add_todo);
        addButton.setOnClickListener(this::addTodo);
    }

    private void configureRecyclerView() {
        TodoListAdapter adapter = new TodoListAdapter(this);
        viewModel.getTodos().observe(this, todos -> {
            adapter.setTodos(todos);
            if (todos != null && todos.size() > 0) {
                emptyListInfoTextView.setVisibility(View.GONE);
            } else {
                emptyListInfoTextView.setVisibility(View.VISIBLE);
            }
        });
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ItemTouchHelper.Callback callback = new TodoListActivity.ItemTouchHelperCallback(adapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);
    }

    private void addTodo(View view) {
        Intent intent = new Intent(this, TodoEditorActivity.class);
        startActivity(intent);
    }

    public class ItemTouchHelperCallback extends ItemTouchHelper.Callback {

        private final TodoListAdapter adapter;

        private ItemTouchHelperCallback(TodoListAdapter adapter) {
            this.adapter= adapter;
        }

        @Override
        public boolean isItemViewSwipeEnabled() {
            return true;
        }

        @Override
        public int getMovementFlags(@NonNull RecyclerView recyclerView,
                                    @NonNull RecyclerView.ViewHolder viewHolder) {
            return makeMovementFlags(0, ItemTouchHelper.END);
        }

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView,
                              @NonNull RecyclerView.ViewHolder viewHolder,
                              @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            viewModel.deleteTodo(adapter.getTodo(viewHolder.getAdapterPosition()));
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView,
                                @NonNull RecyclerView.ViewHolder viewHolder,
                                float dX, float dY, int actionState, boolean isCurrentlyActive) {
            final View itemView = viewHolder.itemView;
            if (dX > 0) {
                // draw a red background with a trash can while sliding
                Paint p = new Paint();
                p.setARGB(0xff, 0xff, 0, 0);
                c.drawRect((float) itemView.getLeft(), (float) itemView.getTop(), dX,
                        (float) itemView.getBottom(), p);
                Drawable drawable = AppCompatResources.getDrawable(TodoListActivity.this,
                        R.drawable.ic_delete_white_36dp);
                if (drawable != null) {
                    float oneDPI = getResources().getDisplayMetrics().xdpi /
                            DisplayMetrics.DENSITY_DEFAULT;
                    int margin = round(16 * oneDPI);
                    int dim = round(24 * oneDPI);
                    int mid = (itemView.getTop() + itemView.getBottom()) / 2;
                    drawable.setBounds(itemView.getLeft() + margin, mid - dim / 2,
                            itemView.getLeft() + margin + dim, mid + dim / 2);
                    c.clipRect(itemView.getLeft(), itemView.getTop(), dX, itemView.getBottom());
                    drawable.draw(c);
                }
                float alpha = 1f;
                if (dX < itemView.getWidth()) {
                    alpha = 1f - Math.abs(dX) / (float) itemView.getWidth();
                }
                itemView.setAlpha(alpha);
                if(D) Log.d(TAG, "."+ actionState +"." + isCurrentlyActive + "." + dX + ".");
            }
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    }
}
