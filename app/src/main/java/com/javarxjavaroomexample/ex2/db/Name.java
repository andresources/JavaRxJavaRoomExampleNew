package com.javarxjavaroomexample.ex2.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "table_name", indices = {@Index(value = "name", unique = true)})
public class Name {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id")
    int id;

    @NonNull
    @ColumnInfo(name = "name")
    String name;

    public Name(@NonNull String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
