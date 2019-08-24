package com.example.fitnesswizards.db;

import android.content.Context;

import com.example.fitnesswizards.db.dao.PlayerDao;
import com.example.fitnesswizards.db.entity.Player;

import androidx.room.Room;
import androidx.room.RoomDatabase;


//Define entities(tables) in the database
@androidx.room.Database(entities = {Player.class}, version = 1)
public abstract class Database extends RoomDatabase {
    //Define DAOS
    public abstract PlayerDao playerDao();

    //Singleton design patter recommended by Google
    private static volatile Database INSTANCE;

    public static Database getDatabase(final Context context){
        if(INSTANCE == null){
            synchronized (Database.class){
                if(INSTANCE == null){
                    //Create database
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            Database.class, "database")
                            .fallbackToDestructiveMigration()
                            .build();

                }
            }
        }
        return INSTANCE;
    }
}
