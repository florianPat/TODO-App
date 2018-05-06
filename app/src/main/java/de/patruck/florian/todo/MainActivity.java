package de.patruck.florian.todo;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.accessibility.AccessibilityManager;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Logger;

import de.patruck.florian.todo.database.Todo;
import de.patruck.florian.todo.database.TodoAdapter;
import de.patruck.florian.todo.database.TodoDao;
import de.patruck.florian.todo.database.TodoDatabase;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 1;

    private RecyclerView recyclerView;
    private TodoDatabase database;
    private TodoAdapter adapter;
    private TodoDao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddTODOActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        recyclerView = findViewById(R.id.recyler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        database = Room.databaseBuilder(this, TodoDatabase.class, "todo").allowMainThreadQueries().fallbackToDestructiveMigration().build();
        dao = database.todoDao();

        //NOTE: If I want to clear the whole database: here you go!
        //dao.delete(dao.getAll());

        adapter = new TodoAdapter(dao);
        recyclerView.setAdapter(adapter);

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int id = (int) viewHolder.itemView.getTag();

                Todo todo = dao.getTodoFromId(id);

                TextView textView = viewHolder.itemView.findViewById(R.id.todo_text);
                Drawable background = textView.getBackground();

                if(BuildConfig.DEBUG && !(background instanceof ColorDrawable)) {
                    throw new AssertionError();
                } else {
                    if (((ColorDrawable) background).getColor() == viewHolder.itemView.getResources().getColor(R.color.colorViewHolderSelected)
                            || (!todo.every)) {
                        if (dao.delete(todo) == 1) {
                            --adapter.count;
                            dao.delete(todo);
                            adapter.swapDao(dao);
                        } else {
                            if (BuildConfig.DEBUG) {
                                throw new AssertionError();
                            }
                        }
                    } else {
                        TodoAdapter.DateNow dateNow = new TodoAdapter.DateNow();

                        if ((dateNow.currentDay & todo.days) > 0) {
                            todo.days &= ~dateNow.currentDay;
                            todo.finished |= dateNow.currentDay;
                            dao.update(todo);
                        }

                        --adapter.count;
                        adapter.deleteTodos.add(todo);
                        dao.delete(todo);
                        adapter.swapDao(dao);
                    }
                }
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    protected void onResume() {
        super.onResume();

        adapter.refresh();
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                if (data.hasExtra(AddTODOActivity.TODO_TEXT) && data.hasExtra(AddTODOActivity.TODO_DATE) &&
                        data.hasExtra(AddTODOActivity.TODO_EVERY)) {
                    Todo todo = new Todo();
                    todo.todoText = data.getStringExtra(AddTODOActivity.TODO_TEXT);
                    todo.every = data.getBooleanExtra(AddTODOActivity.TODO_EVERY, false);
                    todo.days = data.getByteExtra(AddTODOActivity.TODO_DAY, (byte)0);
                    todo.checked = false;
                    todo.finished = 0;

                    int date[] = data.getIntArrayExtra(AddTODOActivity.TODO_DATE);
                    if (BuildConfig.DEBUG && date.length != 3) {
                        throw new AssertionError();
                    }
                    todo.dayOfMonths = date[0];
                    todo.month = date[1];
                    todo.year = date[2];

                    if (!todo.todoText.equals("")) {
                        dao.insert(todo);
                    } else {
                        Log.e(MainActivity.class.getSimpleName(), "Returned nothing!");
                    }
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_info) {
            Intent intent = new Intent(getApplicationContext(), InfoActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(!adapter.deleteTodos.isEmpty())
        {
            Todo[] todos = new Todo[adapter.deleteTodos.size()];
            adapter.deleteTodos.toArray(todos);
            dao.insert(todos);
        }

        database.close();
    }
}
