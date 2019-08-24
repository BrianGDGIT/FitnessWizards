package com.example.fitnesswizards;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.fitnesswizards.db.Database;

public class MainActivity extends AppCompatActivity {
    //Permissions constant
    private static final int MY_PERMISSIONS_REQUEST_ACCESSFINELOCATION = 1;

    private static final String TAG = MainActivity.class.getSimpleName();
    private TextView stepsSinceOpenedText;
    private TextView totalStepsTakenText;

    private Pedometer pedometer;

    //Buttons
    private Button characterButton;
    private Button exploreButton;

    //Initiate fragment manager
    final FragmentManager fragmentManager = getSupportFragmentManager();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set initial fragment in view
        Fragment mapFragment = new MapFragment();
        commitTransaction(mapFragment);

        //Buttons
        characterButton = (Button) findViewById(R.id.character_button);
        characterButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //Commit character fragment
                Fragment characterFragment = new CharacterFragment();
                commitTransaction(characterFragment);
            }
        });

        exploreButton = (Button) findViewById(R.id.explore_button);
        exploreButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //Commit character fragment
                Fragment characterFragment = new MapFragment();
                commitTransaction(characterFragment);
            }
        });

        //Get layout elements
        stepsSinceOpenedText = findViewById(R.id.main_opened_steps_taken);
        totalStepsTakenText = findViewById(R.id.main_steps_taken);

        stepsSinceOpenedText.setText("0");

        //Create Pedometer object
        pedometer = new Pedometer(this);
        //Setup listener for Pedometer object
        pedometer.setPodometerListener(new Pedometer.PedometerListener(){
            @Override
            public void onSensorChanged(int stepsSinceOpened, int stepsTotal) {
                stepsSinceOpenedText.setText(String.valueOf(stepsSinceOpened));
                totalStepsTakenText.setText(String.valueOf(stepsTotal));
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


    @Override
    protected void onResume() {
        super.onResume();
        pedometer.registerSensorListener();
    }

}
