package com.example.fitnesswizards.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fitnesswizards.MainActivity;
import com.example.fitnesswizards.R;
import com.example.fitnesswizards.db.Database;
import com.example.fitnesswizards.db.entity.Player;
import com.example.fitnesswizards.viewmodel.PlayerViewModel;

public class CharacterCreationActivity extends AppCompatActivity {
    PlayerViewModel playerViewModel;
    Boolean isPlayerCreated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_creation);

        initPlayerData();

        Button createButton = findViewById(R.id.creation_create_button);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isPlayerCreated == false){
                    Intent intent = new Intent(CharacterCreationActivity.this, MainActivity.class);
                    //Get player creation info from EditText fields
                    EditText playerNameEditText = findViewById(R.id.playerName_edit_text);
                    EditText playerClassEditText = findViewById(R.id.playerClass_edit_text);
                    String playerName = playerNameEditText.getText().toString();
                    String playerClass = playerClassEditText.getText().toString();
                    //Put data into intent
                    intent.putExtra("Player Name", playerName);
                    intent.putExtra("Player Class", playerClass);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(), R.string.player_exists, Toast.LENGTH_LONG).show();
                }

            }
        });

        Button cancelButton = findViewById(R.id.creation_cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CharacterCreationActivity.this, MainMenuActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initPlayerData(){
        //Connect with data
        playerViewModel = ViewModelProviders.of(this)
                .get(PlayerViewModel.class);

        //Add Player Data Observer
        playerViewModel.getPlayerLiveData().observe(this, new Observer<Player>() {
            @Override
            public void onChanged(Player player) {
                if(player != null){
                    isPlayerCreated = true;
                }

            }
        });
    }
}
