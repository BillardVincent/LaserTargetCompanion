package fr.vbillard.lasertargetcompanion;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import fr.vbillard.lasertargetcompanion.activities.StatistiquesActivity;
import fr.vbillard.lasertargetcompanion.activities.TargetActivity;


public class MainActivity extends AppCompatActivity {
    private Button buttonTirer = null;
    private Button buttonStatistiques = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonTirer = findViewById(R.id.buttonTirer);
        buttonTirer.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, TargetActivity.class)));

        buttonStatistiques = findViewById(R.id.buttonStatistiques);
        buttonStatistiques.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, StatistiquesActivity.class)));
    }
}