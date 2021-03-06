package com.example.fitnesswizards;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.fitnesswizards.db.entity.Player;
import com.example.fitnesswizards.viewmodel.PlayerViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class CharacterFragment extends Fragment {
    private ImageView playerImageView;
    private TextView playerNameTextView;
    private TextView playerClassTextView;
    private TextView playerLevelTextView;
    private TextView playerExperienceTextView;

    private ProgressBar playerExperienceBar;

    String playerClass;

    private int PLAYERLEVELEXPREQ = 100;

    //Fragment View
    View view;

    PlayerViewModel playerViewModel;



    public CharacterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_character, container, false);

        //Set layout elements
        playerImageView = view.findViewById(R.id.player_imageView);
        playerNameTextView = view.findViewById(R.id.player_name_textView);
        playerClassTextView = view.findViewById(R.id.player_class_textView);
        playerLevelTextView = view.findViewById(R.id.player_level_textView);
        playerExperienceBar = view.findViewById(R.id.experienceBar);
        playerExperienceTextView = view.findViewById(R.id.player_experience_textView);
        initPlayerView();

        return view;
    }

    private void initPlayerView(){
        //Connect with data
        playerViewModel = ViewModelProviders.of(this)
                .get(PlayerViewModel.class);

        //Add Player Data Observer
        playerViewModel.getPlayerLiveData().observe(this, new Observer<Player>() {
            @Override
            public void onChanged(Player player) {
                String playerName = player.getPlayerName();
                playerClass = player.getPlayerClass();
                int playerLevel = player.getPlayerLevel();
                int playerExperience = player.getPlayerExperience();
                int experienceToNextLevel = playerLevel * PLAYERLEVELEXPREQ - playerExperience;

                playerNameTextView.setText(playerName);
                playerClassTextView.setText(playerClass);
                playerLevelTextView.setText(Integer.toString(playerLevel));
                playerExperienceTextView.setText(Integer.toString(playerExperience));
                playerExperienceBar.setProgress(PLAYERLEVELEXPREQ - experienceToNextLevel);

                //Set class image
                if(playerClass.equals("Wizard")){
                    playerImageView.setImageResource(R.drawable.portrait_wizard);
                }else if(playerClass.equals("Necromancer")){
                    playerImageView.setImageResource(R.drawable.portrait_necro);
                }
            }
        });
        //Connect with data end

    }



}
