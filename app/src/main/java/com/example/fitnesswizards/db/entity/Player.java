package com.example.fitnesswizards.db.entity;

import com.example.fitnesswizards.db.entity.Creature;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "players")
public class Player extends Creature {
    @PrimaryKey
    @NonNull
    private String playerName;

    //Player Stats
    private int playerExperience = 0;
    private int playerKills = 0;

    public void setPlayerName(String name){
        playerName = name;
    }

    public String getPlayerName(){
        return playerName;
    }

    public void setPlayerExperience(int experience){
        playerExperience = experience;
    }

    public int getPlayerExperience(){
        return playerExperience;
    }

    public void setPlayerKills(int kills){
        playerKills = kills;
    }
    public int getPlayerKills(){
        return playerKills;
    }
}
