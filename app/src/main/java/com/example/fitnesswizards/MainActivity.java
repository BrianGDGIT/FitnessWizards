package com.example.fitnesswizards;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.fitnesswizards.db.Database;
import com.example.fitnesswizards.db.entity.Player;
import com.example.fitnesswizards.viewmodel.PlayerViewModel;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.LeaderboardsClient;
import com.google.android.gms.tasks.OnSuccessListener;

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

    //Google Play
    GoogleSignInClient googleSignInClient;
    GoogleSignInAccount googleSignInAccount;
    private static final int RC_SIGN_IN = 9001;

    LeaderboardsClient leaderboardsClient;
    private static final int RC_LEADERBOARD_UI = 9004;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set Actionbar
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        //Create Google Play Client
        googleSignInClient = GoogleSignIn.getClient(this,
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN).build());

        //Sign in if Sign in button was clicked
        if(getIntent().getIntExtra("Sign In", -1) == 1){
            startSignInIntent();
            Log.d(TAG, "startSignInIntent()");
        }else if(getIntent().getIntExtra("Sign In", 1) == 2){
            signOut();
        }

        //Obtain Google Play Leaderboard
//        if(googleSignInAccount != null){
//////            Games.getLeaderboardsClient(this, googleSignInAccount);
//////        }

        //Create Player
        Player createdPlayer = new Player();
        createdPlayer.setPlayerName(getIntent().getStringExtra("Player Name"));
        createdPlayer.setPlayerClass(getIntent().getStringExtra("Player Class"));
        Database.getDatabase(this).createPlayer(createdPlayer);

        //Set initial fragment in view
        mapFragment = new MapFragment();
        characterFragment = new CharacterFragment();

        commitTransaction(characterFragment);

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

    private void startSignInIntent(){
        startActivityForResult(googleSignInClient.getSignInIntent(), RC_SIGN_IN);
    }

    private void signOut(){
        googleSignInClient.signOut();
    }

    private void showLeaderboard(){
        Games.getLeaderboardsClient(this, GoogleSignIn.getLastSignedInAccount(this))
                .getLeaderboardIntent(getString(R.string.leaderboard_id))
                .addOnSuccessListener(new OnSuccessListener<Intent>() {
                    @Override
                    public void onSuccess(Intent intent) {
                        startActivityForResult(intent, RC_LEADERBOARD_UI);
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Save GoogleSignIn Account, for use with leaderboard etc.
        if(requestCode == RC_SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(result.isSuccess()){
                googleSignInAccount = result.getSignInAccount();
            }else{
                String message = result.getStatus().getStatusMessage();
                if(message == null || message.isEmpty() ){
                    message = getString(R.string.sign_in_error);
                }
                new AlertDialog.Builder(this).setMessage(message)
                        .setNeutralButton(android.R.string.ok, null).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch(item.getItemId()){
            case R.id.toolbar_leaderboard:
                showLeaderboard();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        pedometer.registerSensorListener();
    }

    @Override
    protected void onStop() {
        super.onStop();

    }
}
