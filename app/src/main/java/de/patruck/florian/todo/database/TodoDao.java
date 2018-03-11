package de.patruck.florian.todo.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

/**
 * Created by Florian on 11.03.2018.
 */

@Dao
public interface TodoDao {
    @Query("SELECT * FROM Todo")
    Todo[] getAll();

    @Insert
    public void insert(Todo... todos);

    @Delete
    public void delete(Todo... todos);
}
