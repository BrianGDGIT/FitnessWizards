package com.example.fitnesswizards.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.fitnesswizards.MainActivity;
import com.example.fitnesswizards.R;
import com.example.fitnesswizards.db.Database;
import com.example.fitnesswizards.db.entity.Player;
import com.example.fitnesswizards.viewmodel.PlayerViewModel;

public class CharacterCreationActivity extends AppCompatActivity {
    PlayerViewModel playerViewModel;
    Boolean isPlayerCreated = false;

    String classSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_creation);

        initPlayerData();

        //Initialize class Spinner
        Spinner classSpinner = (Spinner) findViewById(R.id.class_selection_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.classes_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        classSpinner.setAdapter(adapter);

        //Create listener to determine what item in the Spinner list was selected
        classSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                classSelected = classSpinner.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Button createButton = findViewById(R.id.creation_create_button);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isPlayerCreated == false){
                    Intent intent = new Intent(CharacterCreationActivity.this, MainActivity.class);
                    //Get player creation info from EditText fields
                    EditText playerNameEditText = findViewById(R.id.playerName_edit_text);

                    String playerName = playerNameEditText.getText().toString();
                    String playerClass = classSelected;

                    //Check if character information is good
                    if(playerName != null && playerClass != null){
                        //Put data into intent and send to activity
                        intent.putExtra("Player Name", playerName);
                        intent.putExtra("Player Class", playerClass);
                        startActivity(intent);
                    }
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
