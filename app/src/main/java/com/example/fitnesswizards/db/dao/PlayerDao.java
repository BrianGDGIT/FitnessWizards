package com.example.fitnesswizards.db.dao;

import com.example.fitnesswizards.db.entity.Player;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface PlayerDao {
    @Insert
    void insert(Player player);

    @Update
    void update(Player player);

    @Delete
    void delete(Player player);

    @Query("DELETE FROM players")
    void deleteAll();

    @Query("SELECT * FROM players")
    LiveData<Player> getPlayer();
}
