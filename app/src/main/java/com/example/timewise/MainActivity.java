package com.example.timewise;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {


    private Button btnGoToSignUp, btnGoToLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) { //The onCreate function
        // initializes the activity when it is first created, setting up the layout (activity_main)
        // and configuring the app to handle edge-to-edge display by
        // adjusting view padding based on system UI
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //if the users decides to sign up//
        btnGoToSignUp = findViewById(R.id.buttonGoToSignUp);
        btnGoToSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent go = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(go);
            }

        });

        //if the users decides to log in//
        btnGoToLogin = findViewById(R.id.buttonGoToLogin);
        btnGoToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent(MainActivity.this, LogInActivity.class);
                startActivity(go);
            }
        });


    }
}