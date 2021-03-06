package com.dastrix.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        String activeUser = com.dastrix.myapplication.SignIn.getDefaults("phone", MainActivity.this);

        if(!activeUser.isEmpty()) {
            Intent intent = new Intent(MainActivity.this, com.dastrix.myapplication.FoodPage.class);
            startActivity(intent);
        }

        Button btnSignIn = findViewById(R.id.btnSignIn);
        Button btnSignUp = findViewById(R.id.btnSignUp);

        btnSignIn.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, SignIn.class);
            startActivity(intent);
        });

        btnSignUp.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, SignUp.class);
            startActivity(intent);
        });
    }
}