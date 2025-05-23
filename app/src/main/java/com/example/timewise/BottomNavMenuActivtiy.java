package com.example.timewise;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BottomNavMenuActivtiy extends AppCompatActivity {

    private Button btnLogout;
    BottomNavigationView bottomNavigationView;
    private FirebaseAuth mAuth; // Firebase authentication instance

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this); // Enables edge-to-edge drawing for the activity
        setContentView(R.layout.activity_bottom_nav_menu_activtiy); // Set the layout for the activity

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        }); // Adjust layout padding to avoid overlapping with system bars

        mAuth = FirebaseAuth.getInstance(); // Initialize Firebase Authentication

        btnLogout = findViewById(R.id.btnlogout); // Link the logout button from layout
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut(); // Sign out the user from Firebase
                startActivity(new Intent(BottomNavMenuActivtiy.this, LogInActivity.class)); // Redirect to login screen
                finish(); // Close current activity
                Toast.makeText(BottomNavMenuActivtiy.this, "logout successfully", Toast.LENGTH_SHORT).show(); // Show logout message
            }
        });

        if (savedInstanceState == null) {
            replaceFragment(new HomeFragment()); // Load HomeFragment by default when activity starts
        }

        // Create instances of all fragments
        HomeFragment hf = new HomeFragment();
        HabitTrackerFragment habitrackerfrag = new HabitTrackerFragment();
        ProfileFragment profilefrag = new ProfileFragment();
        TimerFragment timerfrag = new TimerFragment();
        MoodFragment moodfrag = new MoodFragment();

        bottomNavigationView = findViewById(R.id.bottomNavigationView); // Link bottom navigation view from layout

        bottomNavigationView.setOnItemSelectedListener(item -> {
            // Handle bottom navigation item clicks
            if (item.getItemId() == R.id.home) {
                replaceFragment(hf); // Switch to HomeFragment
            }
            if (item.getItemId() == R.id.habitTracker) {
                replaceFragment(habitrackerfrag); // Switch to HabitTrackerFragment
            }
            if (item.getItemId() == R.id.profile) {
                replaceFragment(profilefrag); // Switch to ProfileFragment
            }
            if (item.getItemId() == R.id.timer) {
                replaceFragment(timerfrag); // Switch to TimerFragment
            }
            if (item.getItemId() == R.id.mood) {
                replaceFragment(moodfrag); // Switch to MoodFragment
            }

            return true; // Return true to show selected item as active
        });

    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager(); // Get the FragmentManager
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction(); // Start a new transaction
        fragmentTransaction.replace(R.id.frameLayoutBottom, fragment); // Replace the existing fragment
        fragmentTransaction.commit(); // Commit the transaction
    }

}
