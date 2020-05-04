package com.example.fitnesswizards.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.fitnesswizards.MainActivity;
import com.example.fitnesswizards.R;

public class CharacterCreationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_creation);

        Button createButton = findViewById(R.id.creation_create_button);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
}
