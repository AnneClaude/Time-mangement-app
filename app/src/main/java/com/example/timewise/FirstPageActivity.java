package com.example.timewise;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.Manifest; // For Manifest.permission.*
import android.widget.EditText;
import android.widget.TextView;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class FirstPageActivity extends AppCompatActivity {

    Context context= this;
    private Button BtnLetsDoIt;
    @SuppressLint("InlinedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //The onCreate function initializes the activity when it is first created,
        // setting up the layout (activity_main) and configuring the app to handle
        // edge-to-edge display by adjusting view padding based on system UI
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_first_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ActivityCompat.requestPermissions((Activity) context,
                new String[]{android.Manifest.permission.POST_NOTIFICATIONS,
                        android.Manifest.permission.FOREGROUND_SERVICE },
                100);// Request user permission for notifications and foreground services with request code 100

        TextView name=findViewById(R.id.text_name);
        String username= getIntent().getStringExtra("Username");
        name.setText(username);

        BtnLetsDoIt= findViewById(R.id.LetsDoIt);
        BtnLetsDoIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstPageActivity.this, BottomNavMenuActivtiy.class);
                startActivity(intent);
                intent.putExtra("Username",username);
                startActivity(intent);
            }
        });
    }
}