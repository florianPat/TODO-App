package de.patruck.florian.todo;

import android.arch.persistence.room.Room;
import android.content.Intent;
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
import android.widget.Toast;

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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
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

        database = Room.databaseBuilder(this, TodoDatabase.class, "todo").allowMainThreadQueries().build();
        dao = database.todoDao();
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

                Todo todo = new Todo();
                todo.id = id;
                dao.delete(todo);
                adapter.swapDao(dao);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE) {
            if(resultCode == RESULT_OK) {
                if(data.hasExtra(AddTODOActivity.TODO_TEXT)) {
                    Todo todo = new Todo();
                    todo.todoText = data.getStringExtra(AddTODOActivity.TODO_TEXT);
                    if(todo.todoText != "") {
                        dao.insert(todo);
                        adapter.swapDao(dao);
                    }
                } else {
                    Log.e(MainActivity.class.getSimpleName(), "Returned nothing!");
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
}
