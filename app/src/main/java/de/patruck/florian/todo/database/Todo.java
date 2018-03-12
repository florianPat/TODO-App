package de.patruck.florian.todo.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by Florian on 11.03.2018.
 */

@Entity
public class Todo {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String todoText;
    public int dayOfMonths;
    public int month;
    public int year;
    public boolean every;
    public boolean checked;
    public byte days;
}
