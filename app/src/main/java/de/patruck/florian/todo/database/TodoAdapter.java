package de.patruck.florian.todo.database;

import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.time.temporal.WeekFields;
import java.util.Calendar;
import java.util.Locale;

import de.patruck.florian.todo.BuildConfig;
import de.patruck.florian.todo.R;

/**
 * Created by Florian on 11.03.2018.
 */

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {

    private TodoDao dao;
    public int count;

    public static class DateNow {
        public int dayOfMonts = 0, months = 0, year = 0;
        public byte currentDay = 1;
        public DateNow() {
            Calendar calendar = Calendar.getInstance();

            String dateNow = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.getTime());
            String[] dateIndividual = dateNow.split("-");

            if (BuildConfig.DEBUG && dateIndividual == null) {
                throw new AssertionError();
            }

            try {
                dayOfMonts = Integer.valueOf(dateIndividual[2]);
                months = Integer.valueOf(dateIndividual[1]);
                year = Integer.valueOf(dateIndividual[0]);
            } catch (NumberFormatException ex) {
                Log.e(TodoAdapter.class.getSimpleName(), "Bad number!!");
            }

            String currentDayStr = calendar.getTime().toString();
            currentDayStr = currentDayStr.substring(0, currentDayStr.indexOf(' '));

            switch (currentDayStr) {
                case "Mon": {
                    break;
                }
                case "Tue": {
                    currentDay <<= 1;
                    break;
                }
                case "Wed": {
                    currentDay <<= 2;
                    break;
                }
                case "Thu": {
                    currentDay <<= 3;
                    break;
                }
                case "Fri": {
                    currentDay <<= 4;
                    break;
                }
                case "Sat": {
                    currentDay <<= 5;
                    break;
                }
                case "Sun": {
                    currentDay <<= 6;
                    break;
                }
                default: {
                    if(BuildConfig.DEBUG) {
                        throw new AssertionError();
                    }
                    break;
                }
            }
        }
    }

    public TodoAdapter(TodoDao dao) {
        this.dao = dao;

        refresh();
    }

    public void swapDao(TodoDao dao) {
        this.dao = dao;
        notifyDataSetChanged();
    }

    public void refresh() {
        count = 0;

        DateNow dateNow = new DateNow();

        for(Todo todo : this.dao.getAll()) {
            if(todo.checked) {
                ++count;
            }
            else if ((!todo.every) && todo.dayOfMonths == dateNow.dayOfMonts && todo.month == dateNow.months && todo.year == dateNow.year) {
                ++count;
                todo.checked = true;
                dao.update(todo);
            }
            else if((todo.days & dateNow.currentDay) > 0) {
                ++count;
            }
            else {
                if(BuildConfig.DEBUG) {
                    throw new AssertionError();
                }
            }
        }
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

        holder.itemView.setTag(todo.id);
        String todoText = todo.todoText;

        holder.tv_title.setText(todoText);
    }

    @Override
    public int getItemCount() {
        return count;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_title;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.todo_text);
        }
    }
}
