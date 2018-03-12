package de.patruck.florian.todo.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

/**
 * Created by Florian on 11.03.2018.
 */

@Database(entities = {Todo.class}, version = 4)
public abstract class TodoDatabase extends RoomDatabase {
    public abstract TodoDao todoDao();
}
