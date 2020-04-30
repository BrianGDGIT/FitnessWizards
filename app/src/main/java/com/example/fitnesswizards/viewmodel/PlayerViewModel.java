package com.example.fitnesswizards.viewmodel;

import android.app.Application;

import com.example.fitnesswizards.MainActivity;
import com.example.fitnesswizards.Repository;
import com.example.fitnesswizards.db.Database;
import com.example.fitnesswizards.db.entity.Player;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class PlayerViewModel extends AndroidViewModel {
    private Repository repository;
    private LiveData<Player> playerLiveData;

    public PlayerViewModel(Application application){
        super(application);
        repository = Repository.getRepository(Database.getDatabase(application));
        playerLiveData = repository.getPlayer();
    }


}
