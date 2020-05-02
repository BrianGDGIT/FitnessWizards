package com.example.fitnesswizards;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.fitnesswizards.db.Database;
import com.example.fitnesswizards.db.dao.PlayerDao;
import com.example.fitnesswizards.db.entity.Player;

public class Repository {
    private static Repository repository;
    private PlayerDao playerDao;

    private LiveData<Player> player;

    private Repository(Database db){
        playerDao = db.playerDao();

        player = playerDao.getPlayer();
    }

    //Singleton
    //Get Repository
    public static Repository getRepository(Database db){
        if(repository == null){
            repository = new Repository(db);
        }
        return repository;
    }

    //Getters from PlayerViewModel
    public LiveData<Player> getPlayer(){
        return player;
    }

    //Update Player
    public void updatePlayer(Player player) { new updatePlayerTask(playerDao).execute(player);}

    //Tasks
    private static class updatePlayerTask extends AsyncTask<Player, Void, Void>{
        private PlayerDao dao;

        updatePlayerTask(PlayerDao dao){
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(final Player... players) {
            dao.update(players[0]);
            return null;
        }
    }
}
