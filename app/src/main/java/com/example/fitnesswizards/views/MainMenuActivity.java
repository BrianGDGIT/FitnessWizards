package com.example.fitnesswizards.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.fitnesswizards.MainActivity;
import com.example.fitnesswizards.R;

public class MainMenuActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        Button createButton = findViewById(R.id.create_button);
        Button loadButton = findViewById(R.id.load_button);
        Button sign_in_Button = findViewById(R.id.sign_in_button);
        Button sign_out_Button = findViewById(R.id.sign_out_button);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenuActivity.this, CharacterCreationActivity.class);
                startActivity(intent);
            }
        });


        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenuActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


        sign_out_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenuActivity.this, MainActivity.class);
                intent.putExtra("Sign In", 2);
                findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
                findViewById(R.id.sign_out_button).setVisibility(View.GONE);
                startActivity(intent);
            }
        });


            sign_in_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenuActivity.this, MainActivity.class);
                intent.putExtra("Sign In", 1);
                findViewById(R.id.sign_in_button).setVisibility(View.GONE);
                findViewById(R.id.sign_out_button).setVisibility(View.VISIBLE);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View view) {

    }
}
