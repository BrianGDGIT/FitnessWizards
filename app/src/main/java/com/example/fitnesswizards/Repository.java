package com.example.fitnesswizards;

import com.example.fitnesswizards.db.Database;
import com.example.fitnesswizards.db.dao.PlayerDao;

public class Repository {
    private static Repository repository;
    private PlayerDao playerDao;

    Repository(Database db){
        playerDao = db.playerDao();
    }

    //Singleton
    //Getter
    public static Repository getRepository(Database db){
        if(repository == null){
            repository = new Repository(db);
        }
        return repository;
    }

}
