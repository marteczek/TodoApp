package com.marteczek.todoapp.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.marteczek.todoapp.R;
import com.marteczek.todoapp.database.entity.Todo;
import com.marteczek.todoapp.database.entity.type.TaskCategory;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class TodoListAdapter extends RecyclerView.Adapter<TodoListAdapter.TodoViewHolder> {

    private final Context context;

    private final LayoutInflater inflater;

    private final DateFormat dateFormat;

    private List<Todo> todos;

    TodoListAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        dateFormat = android.text.format.DateFormat.getDateFormat(context);
    }

    @Override
    public @NonNull TodoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recyclerview_todo_item, parent, false);
        return new TodoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoViewHolder holder, int position) {
        if (todos != null) {
            Todo todo = todos.get(position);
            holder.nameTextView.setText(todo.getName());
            Date date = todo.getCompletionDate();
            holder.completionDateTextView.setText(date == null ? "" : dateFormat.format(date));
            String category = todo.getCategory();
            String categoryName = "";
            if (category != null) {
                switch (category) {
                    case TaskCategory.WORK:
                        categoryName = context.getString(R.string.category_work);
                        break;
                    case TaskCategory.SHOPPING:
                        categoryName = context.getString(R.string.category_shopping);
                        break;
                    case TaskCategory.OTHER:
                        categoryName = context.getString(R.string.category_other);
                        break;
                    default:
                        categoryName = "";
                }
            }
            holder.categoryTextView.setText(categoryName);
        } else {
            holder.nameTextView.setText("");
            holder.completionDateTextView.setText("");
            holder.categoryTextView.setText("");
        }
    }

    void setTodos(List<Todo> Todos){
        this.todos = Todos;
        notifyDataSetChanged();
    }

    Todo getTodo(int position) {
        if (todos != null) {
            return todos.get(position);
        } else {
            return null;
        }
    }

    @Override
    public int getItemCount() {
        return (todos != null) ? todos.size() : 0;
    }

    class TodoViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameTextView;
        private final TextView completionDateTextView;
        private final TextView categoryTextView;

        private TodoViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.todoName);
            completionDateTextView = itemView.findViewById(R.id.completion_date);
            categoryTextView = itemView.findViewById(R.id.category);
        }
    }
}
