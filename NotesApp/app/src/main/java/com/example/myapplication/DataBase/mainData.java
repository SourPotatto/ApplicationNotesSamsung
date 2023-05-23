package com.example.myapplication.DataBase;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.myapplication.Models.Notes;

import java.util.List;

@Dao
public interface mainData {

    @Insert (onConflict = REPLACE)
    void insert(Notes notes);

    @Query("SELECT * FROM notes ORDER BY ID Desc")
    List<Notes> getAll();

    @Query("UPDATE notes SET title = :Title, notes = :Notes WHERE ID = :ID")
    void update(int ID, String Title, String Notes);

    @Delete
    void delete(Notes notes);

    @Query("UPDATE notes SET pinned = :pin WHERE ID = :id")
    void pin (int id, boolean pin);
}
