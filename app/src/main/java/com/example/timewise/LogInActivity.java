package com.example.timewise;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LogInActivity extends AppCompatActivity {

    private EditText editEmail, editPassword;
    private Button btnLogIn, btnAlreadyAcc, btnFrogetPassword;

    private FirebaseAuth mAuth;


    //SharedPreferences in Android is a way to store small amounts
    // of data (like settings or user info) as key-value pairs.
    // It saves data locally on the device,
    // even after the app is closed.

    // Constants for SharedPreferences file name and keys
    private static final String SHARED_PREF_NAME ="mypref";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_EMAIL = "email";
    private SharedPreferences sharedPreferences;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //The onCreate function initializes the activity when it is first created,
        // setting up the layout (activity_main) and configuring the app to handle
        // edge-to-edge display by adjusting view padding based on system UI
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_log_in);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance(); //initializes the mAuth object,
        // giving access to Firebase Authentication methods for handling user login,

        editPassword = findViewById(R.id.Password);
        editEmail = findViewById(R.id.EmailAddress);
        btnLogIn = findViewById(R.id.LogInButton);
        btnFrogetPassword = findViewById(R.id.forgotPass);

        // Initialize SharedPreferences using the defined preference file name and private mode
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);

        // Retrieve the saved password from SharedPreferences, or null if not found
        String password = sharedPreferences.getString(KEY_PASSWORD, null);

        // Retrieve the saved email from SharedPreferences, or null if not found
        String email = sharedPreferences.getString(KEY_EMAIL, null);

        if (password != null || email != null) { // make sure non of the password or email are empty
            editEmail.setText(email);
            editPassword.setText(password);
        }


        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get the email and the password by user
                String email = editEmail.getText().toString();
                String password = editPassword.getText().toString();

                if (!email.isEmpty() && !password.isEmpty()) {
                    logIn(email, password);
                } else {
                    Toast.makeText(LogInActivity.this, "Empty Email or Password", Toast.LENGTH_LONG).show();
                }


            }
        });

        btnAlreadyAcc = findViewById(R.id.dontHaveAccButton);
        btnAlreadyAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent(LogInActivity.this, SignUpActivity.class);
                startActivity(go);
            }
        });

        btnFrogetPassword.setOnClickListener(new View.OnClickListener() { // When "Forget Password" button is clicked
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LogInActivity.this); // Create a dialog builder
                View dialogView = getLayoutInflater().inflate(R.layout.forgotpass_dialog, null); // Inflate the custom forgot password layout
                EditText emailBox = dialogView.findViewById(R.id.emailBox); // Find the email input box inside the dialog

                builder.setView(dialogView); // Set the custom view to the dialog
                AlertDialog dialog = builder.create(); // Create the dialog from builder
                dialogView.findViewById(R.id.btnReset).setOnClickListener(new View.OnClickListener() { // When "Reset" button inside dialog is clicked
                    @Override
                    public void onClick(View view) {
                        String userEmail = emailBox.getText().toString(); // Get entered email from input box
                        if (TextUtils.isEmpty(userEmail) && !Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) { // Check if email is empty or invalid
                            Toast.makeText(LogInActivity.this, "Enter your registered email id", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        mAuth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() { // Send password reset email
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(LogInActivity.this, "Check your email", Toast.LENGTH_SHORT).show(); // Show success message
                                    dialog.dismiss(); // Close the dialog
                                } else {
                                    Toast.makeText(LogInActivity.this, "Unable to send, failed", Toast.LENGTH_SHORT).show(); // Show error message
                                }
                            }
                        });
                    }
                });
                dialogView.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() { // When "Cancel" button inside dialog is clicked
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss(); // Close the dialog
                    }
                });
                if (dialog.getWindow() != null) {
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0)); // Make dialog background transparent
                }
                dialog.show(); // Show the dialog
            }
        });
    }


    private void logIn(String email, String password)
    {
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(LogInActivity.this, " Succesfull Login", Toast.LENGTH_SHORT).show();
                    Intent go= new Intent (LogInActivity.this, BottomNavMenuActivtiy.class);

                    //The code saves the user's entered email and password into local storage (SharedPreferences).
                    //It uses an editor to put the text values and then applies (saves) the changes.
                    SharedPreferences.Editor editor= sharedPreferences.edit();
                    editor.putString(KEY_PASSWORD,editPassword.getText().toString());
                    editor.putString(KEY_EMAIL,editEmail.getText().toString());
                    editor.apply();
                    startActivity(go);
                }
                else
                {
                    Toast.makeText(LogInActivity.this,"The login process failed",Toast.LENGTH_LONG).show();
                }
            }
        });
    }





}