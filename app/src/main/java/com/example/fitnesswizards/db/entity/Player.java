package com.example.fitnesswizards.db.entity;

import com.example.fitnesswizards.db.entity.Creature;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "players")
public class Player extends Creature {
    @PrimaryKey
    @NonNull
    private String name;

    //Player Stats
    int experience = 0;
    int kills = 0;

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }
}
