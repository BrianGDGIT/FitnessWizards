package com.example.fitnesswizards.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;

import com.example.fitnesswizards.db.entity.Spell;

@Dao
public interface SpellDao {
    @Insert
    void insert(Spell spell);
}
