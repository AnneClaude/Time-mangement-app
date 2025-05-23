package com.example.timewise;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActivityAddTask extends AppCompatActivity {


    private Button btnAdd;
    private EditText editTextDescription , editTextNeededTime, editTextDeadline, editTextTaskname;
    private ImageView imageView;


    //FireBase Database Reference
    private DatabaseReference databaseReference ;
    //link to realtime Database connected to app

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //The onCreate function initializes the activity when it is first created,
        // setting up the layout (activity_main) and configuring the app to handle
        // edge-to-edge display by adjusting view padding based on system UI
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_task);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth= FirebaseAuth.getInstance();
        String uid= mAuth.getCurrentUser().getUid();
        //firebase database initialization,
        // the reference gets the instance of the realtime database with the existed URL
        databaseReference= FirebaseDatabase.getInstance().getReference("tasks/"+uid);

        editTextTaskname= findViewById(R.id.taskname);
        editTextDescription=findViewById(R.id.description);
        editTextNeededTime=findViewById(R.id.neededtime);
        editTextDeadline=findViewById(R.id.deadlinedate);
        imageView=findViewById(R.id.bookicon);


        btnAdd=findViewById(R.id.AddTaskButton);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Tasks task= new Tasks(editTextTaskname.getText().toString(), editTextDescription.getText().toString(),editTextNeededTime.getText().toString(),editTextDeadline.getText().toString());//constructor
                updateIconBasedOnText(editTextTaskname.getText().toString(), task);
                saveTaskToFirebase(task);

            }
        });
    }

    /**
     * this method adds the task to the real time firebase - using UID reference
     * @param task - the task created by the user
     */
    private void saveTaskToFirebase(Tasks task){
        DatabaseReference newTaskRef= databaseReference.push();
        newTaskRef.setValue(task).addOnSuccessListener(aVoid-> { //adds the object to the key URL
            Toast.makeText(this, "Task added successfully", Toast.LENGTH_SHORT).show();
            finish();//close the activity after successful submission
        }).addOnFailureListener(e -> Toast.makeText(this, "Failed to add your task"
                + e.getMessage(), Toast.LENGTH_SHORT).show());

    }

    /**
     * the icon is generate according to keywords found in the text using regual expression
     * @param text
     * @param tasks
     */
    public void updateIconBasedOnText(String text, Tasks tasks) {
        // Compile the patterns once
        Pattern studyPattern = Pattern.compile("\\b(homework|study|studies|studying|studied|exam|exams|school|read|reading|book|notebook|books|notebooks|task|submit|submittion)\\b", Pattern.CASE_INSENSITIVE);
        Pattern gymPattern = Pattern.compile("\\b(gym|workout|exercise|fitness|training|sport)\\b", Pattern.CASE_INSENSITIVE);
        Pattern artPattern = Pattern.compile("\\b(art|painting|drawing|sculpture|creative|design|paint|draw)\\b", Pattern.CASE_INSENSITIVE);
        Pattern foodPattern = Pattern.compile("\\b(lunch|dinner|supper|food|burger|chips|resturant|burgers)\\b", Pattern.CASE_INSENSITIVE);
        Pattern musicPattern = Pattern.compile("\\b(music|singing|song|concert|band|instrument|guitar|piano|songs|sing|violin)\\b", Pattern.CASE_INSENSITIVE);
        Pattern waterThings = Pattern.compile("\\b(swimming|swim|fishing|fish|water|sea|ocean|surf|surfing|beach)\\b", Pattern.CASE_INSENSITIVE);
        Pattern petPattern = Pattern.compile("\\b(dog|animals|puppy|pet|animal|pets|dogs|walk |bird|hamster|cats|cat)\\b", Pattern.CASE_INSENSITIVE);
        Pattern sleepingPattern = Pattern.compile("\\b(sleep|night|sleeping|late|moon|late time|sleeps)\\b", Pattern.CASE_INSENSITIVE);
        Pattern cakePattern = Pattern.compile("\\b(cake|celebration|party|birthday|candeles|dessert|celebrate|celebrating|birthdays)\\b", Pattern.CASE_INSENSITIVE);


        // Check for each pattern
        if (studyPattern.matcher(text).find())
        {
            imageView.setImageResource(R.drawable.book);
            tasks.setImage(R.drawable.book);
        }
        else if (cakePattern.matcher(text).find())
        {
            imageView.setImageResource(R.drawable.cakeicon);
            tasks.setImage(R.drawable.cakeicon);
        }
        else if (gymPattern.matcher(text).find())
        {
            imageView.setImageResource(R.drawable.baseline_sports_basketball_24);
            tasks.setImage(R.drawable.baseline_sports_basketball_24);
        }
        else if (sleepingPattern.matcher(text).find())
        {
            imageView.setImageResource(R.drawable.baseline_nights_stay_24);
            tasks.setImage(R.drawable.baseline_nights_stay_24);
        }
        else if (foodPattern.matcher(text).find())
        {
            imageView.setImageResource(R.drawable.baseline_fastfood_24);
            tasks.setImage(R.drawable.baseline_fastfood_24);
        }
        else if (waterThings.matcher(text).find())
        {
            imageView.setImageResource(R.drawable.baseline_water_24);
            tasks.setImage(R.drawable.baseline_water_24);
        }
        else if (petPattern.matcher(text).find())
        {
            imageView.setImageResource(R.drawable.baseline_pets_24);
            tasks.setImage(R.drawable.baseline_pets_24);

        }
        else if (musicPattern.matcher(text).find())
        {
            imageView.setImageResource(R.drawable.music);
            tasks.setImage(R.drawable.music);
        }
        else if (artPattern.matcher(text).find()) {
            imageView.setImageResource(R.drawable.baseline_draw_24);
            tasks.setImage(R.drawable.baseline_draw_24);
        }
        else
        {
            imageView.setImageResource(R.drawable.checklist_navyblue);
            tasks.setImage(R.drawable.checklist_navyblue);
        }

    }
}

