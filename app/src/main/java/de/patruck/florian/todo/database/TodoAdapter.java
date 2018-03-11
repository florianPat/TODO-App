package de.patruck.florian.todo.database;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import de.patruck.florian.todo.R;

/**
 * Created by Florian on 11.03.2018.
 */

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {

    private TodoDao dao;

    public TodoAdapter(TodoDao dao) {
        this.dao = dao;
    }

    public void swapDao(TodoDao dao) {
        this.dao = dao;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_view_holder, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Todo[] todos = dao.getAll();
        if(position >= todos.length)
            return;

        Todo todo = todos[position];
        String todoText = todo.todoText;

        holder.tv_title.setText(todoText);
        holder.itemView.setTag(todo.id);
    }

    @Override
    public int getItemCount() {
        return dao.getAll().length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_title;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.todo_text);
        }
    }
}
