package com.example.mytomatogameapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        Button restartButton = (Button)findViewById(R.id.restartButton);
        restartButton.setText("Restart");

        Intent i = new Intent(this, MainActivity2.class);
        startActivity(i);
    }

    public void onClickListener(View v){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

}