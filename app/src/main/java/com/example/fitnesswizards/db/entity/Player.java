package com.example.fitnesswizards.db.entity;

import com.example.fitnesswizards.db.entity.Creature;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "players")
public class Player {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int playerID;


    //Player Stats
    private String playerName;
    private String playerClass;
    private int playerExperience = 0;
    private int playerKills = 0;
    private int playerLevel = 1;

    public void setPlayerID(int id){
        playerID = id;
    }
    public int getPlayerID(){
        return playerID;
    }
    public void setPlayerName(String name){
        playerName = name;
    }
    public String getPlayerName(){
        return playerName;
    }
    public void setPlayerClass(String pClass) {playerClass = pClass; }
    public String getPlayerClass() { return playerClass; }

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

    public void setPlayerLevel(int level) { playerLevel = level; }
    public int getPlayerLevel() { return playerLevel;}
}
