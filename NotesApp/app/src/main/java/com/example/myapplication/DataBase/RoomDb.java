package com.example.myapplication.DataBase;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.myapplication.Models.Notes;

@Database(entities = Notes.class, version = 1, exportSchema = false)
public abstract class RoomDb extends RoomDatabase {

    private static RoomDb dataBase;
    private static String DATABASE_NAME = "NoteApp";

    public synchronized static RoomDb getInstance(Context context){
        if (dataBase == null){
            dataBase = Room.databaseBuilder(context.getApplicationContext(), RoomDb.class,DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return  dataBase;
    }

    public abstract mainData mainData();


}
