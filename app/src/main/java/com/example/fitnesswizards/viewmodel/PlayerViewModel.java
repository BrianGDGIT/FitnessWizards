package com.example.fitnesswizards.viewmodel;

import android.app.Application;

import com.example.fitnesswizards.MainActivity;
import com.example.fitnesswizards.Repository;
import com.example.fitnesswizards.db.Database;

import androidx.lifecycle.AndroidViewModel;

public class PlayerViewModel extends AndroidViewModel {
    private Repository repository;

    public PlayerViewModel(MainActivity mainActivity){
        super(mainActivity.getApplication());
        repository = Repository.getRepository(Database.getDatabase(mainActivity));
    }
}
