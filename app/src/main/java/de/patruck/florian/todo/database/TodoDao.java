package de.patruck.florian.todo.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

/**
 * Created by Florian on 11.03.2018.
 */

@Dao
public interface TodoDao {
    @Query("SELECT * FROM Todo")
    Todo[] getAll();

    @Insert
    void insert(Todo... todos);

    @Delete
    int delete(Todo... todos);

    @Update
    int update(Todo... users);

}
