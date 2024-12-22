package fr.vbillard.lasertargetcompanion;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import fr.vbillard.lasertargetcompanion.activities.StatistiquesActivity;
import fr.vbillard.lasertargetcompanion.activities.TargetActivity;


public class MainActivity extends AppCompatActivity {
    private Button buttonProject = null;
    private Button buttonStorage = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        buttonProject = (Button) findViewById(R.id.buttonTirer);
        buttonStorage = (Button) findViewById(R.id.buttonStatistiques);

        buttonProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent secondeActivite = new Intent(MainActivity.this, TargetActivity.class);


                // Puis on lance l'intent !
                startActivity(secondeActivite);
            }
        });
        buttonStorage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent secondeActivite = new Intent(MainActivity.this, StatistiquesActivity.class);

                startActivity(secondeActivite);
            }
        });
    }
}