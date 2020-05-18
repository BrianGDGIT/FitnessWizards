package com.example.fitnesswizards;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.fitnesswizards.db.Database;
import com.example.fitnesswizards.db.entity.Player;
import com.example.fitnesswizards.viewmodel.PlayerViewModel;
import com.example.fitnesswizards.views.CharacterCreationActivity;

public class MainActivity extends AppCompatActivity {
    //Permissions constant
    private static final int MY_PERMISSIONS_REQUEST_ACCESSFINELOCATION = 1;

    private static final String TAG = MainActivity.class.getSimpleName();

    private TextView stepsSinceOpenedText;
    private TextView totalStepsTakenText;

    private Pedometer pedometer;

    //Used to save LiveData into player object for other uses
    private PlayerViewModel playerViewModel;
    private Player player;

    //Used for xp calculation
    int stepOffset = 0;


    //Buttons
    private Button characterButton;
    private Button exploreButton;

    //Initiate fragment manager
    final FragmentManager fragmentManager = getSupportFragmentManager();

    //Fragments
    Fragment mapFragment;
    Fragment characterFragment;

    //Fragment saved state
    Fragment.SavedState savedState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Create Player
        Player createdPlayer = new Player();
        createdPlayer.setPlayerName(getIntent().getStringExtra("Player Name"));
        createdPlayer.setPlayerClass(getIntent().getStringExtra("Player Class"));
        Database.getDatabase(this).createPlayer(createdPlayer);

        //Set initial fragment in view
        mapFragment = new MapFragment();
        characterFragment = new CharacterFragment();

        commitTransaction(mapFragment);

        //Buttons
        characterButton = (Button) findViewById(R.id.character_button);
        characterButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //Commit character fragment
                commitTransaction(characterFragment);
            }
        });

        exploreButton = (Button) findViewById(R.id.explore_button);
        exploreButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //Commit Map Fragment
                commitTransaction(mapFragment);
            }
        });

        //Get layout elements
        stepsSinceOpenedText = findViewById(R.id.main_opened_steps_taken);
        totalStepsTakenText = findViewById(R.id.main_steps_taken);

        stepsSinceOpenedText.setText("0");

        //Connect with Data in PlayerViewModel
        connectWithData();

        //Create Pedometer object
        pedometer = new Pedometer(this);
        //Setup listener for Pedometer object
        pedometer.setPodometerListener(new Pedometer.PedometerListener(){
            @Override
            public void onSensorChanged(int stepsSinceOpened, int stepsTotal) {

                stepsSinceOpenedText.setText(String.valueOf(stepsSinceOpened));
                totalStepsTakenText.setText(String.valueOf(stepsTotal));

                //Update Player with change in steps to xp
                if(player != null){
                    int playerExperience = player.getPlayerExperience();
                    int playerLevel = player.getPlayerLevel();

                    player.setPlayerExperience(playerExperience + (stepsSinceOpened - stepOffset));
                    //Set offset for next round of steps for xp calculation
                    stepOffset = stepsSinceOpened;

                    if(playerExperience / player.getPlayerLevel() >= 100){
                        player.setPlayerLevel(playerLevel + 1);
                    }
                    playerViewModel.update(player);
                }
            }
        });



    }

    private void commitTransaction(Fragment fragment){
        //Replace View with fragment
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);

        //Commit transaction
        transaction.commit();
    }

    private void connectWithData(){
        //Connect With data
        playerViewModel = ViewModelProviders.of(this)
                .get(PlayerViewModel.class);

        //Add player data observer
        playerViewModel.getPlayerLiveData().observe(this, new Observer<Player>(){
            @Override
            public void onChanged(Player p) {
                player = p;
            }
        });
    }


    public Database getDatabase(){
        return Database.getDatabase(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        pedometer.registerSensorListener();
    }

}
