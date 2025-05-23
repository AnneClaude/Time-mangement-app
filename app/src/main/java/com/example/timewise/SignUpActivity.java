package com.example.timewise;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {

    private EditText editEmail, editPassword, editUserName;
    private Button btnSignUp;
    private Button btnAlreadyAccount;

    private FirebaseAuth mAuth;

    //SharedPreferences in Android is a way to store small amounts
    // of data (like settings or user info) as key-value pairs.
    // It saves data locally on the device,
    // even after the app is closed.

    // Constants for SharedPreferences file name and keys
    private static final String SHARED_PREF_NAME = "mypref";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_USERNAME = "username";

    // Declaration of SharedPreferences object
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //The onCreate function initializes the activity when it is first created,
        // setting up the layout (activity_main) and configuring the app to handle
        // edge-to-edge display by adjusting view padding based on system UI

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        editEmail = findViewById(R.id.EmailAddress);
        editPassword = findViewById(R.id.Password);
        editUserName = findViewById(R.id.username);
        btnSignUp = findViewById(R.id.buttonSignUp);

       // Initialize SharedPreferences with private mode
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editEmail.getText().toString();
                String password = editPassword.getText().toString();
                String username = editUserName.getText().toString();

                if (!email.isEmpty() && !password.isEmpty()) {
                    signUp(email, password, username);
                } else {
                    Toast.makeText(SignUpActivity.this, "Empty Email or Password", Toast.LENGTH_LONG).show();
                }

                // Create an editor to make changes to SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();

                // Save password entered by user into SharedPreferences
                editor.putString(KEY_PASSWORD, editPassword.getText().toString());

                // Save email entered by user into SharedPreferences
                editor.putString(KEY_EMAIL, editEmail.getText().toString());

                // Save username entered by user into SharedPreferences
                editor.putString(KEY_USERNAME, editUserName.getText().toString());

                // Apply changes asynchronously
                editor.apply();
            }
        });

        btnAlreadyAccount = findViewById(R.id.HaveAccountBtn);
        btnAlreadyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent(SignUpActivity.this, LogInActivity.class);
                startActivity(go);
            }
        });

        // initializes the mAuth object,
        // giving access to Firebase Authentication methods for handling user login,
        mAuth = FirebaseAuth.getInstance();
    }

    private void signUp(String email, String password, String username) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Create an editor to make changes to SharedPreferences
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    // Save password entered by user into SharedPreferences
                    editor.putString(KEY_PASSWORD, editPassword.getText().toString());

                    // Save email entered by user into SharedPreferences
                    editor.putString(KEY_EMAIL, editEmail.getText().toString());

                    // Save username entered by user into SharedPreferences
                    editor.putString(KEY_USERNAME, editUserName.getText().toString());

                    // Apply changes asynchronously
                    editor.apply();

                    Toast.makeText(SignUpActivity.this, "Successful sign up!", Toast.LENGTH_SHORT).show();
                    Intent go = new Intent(SignUpActivity.this, FirstPageActivity.class);
                    go.putExtra("Username", username);
                    startActivity(go);
                } else {
                    Toast.makeText(SignUpActivity.this, "Sign up failed", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
