package com.example.timewise;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import  com.example.timewise.databinding.ActivityTaskDetailedBinding;


public class TaskDetailedActivity extends AppCompatActivity {
    //the binding connects the activity with the layout without the need to use find viewById()
    ActivityTaskDetailedBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       binding = ActivityTaskDetailedBinding.inflate(getLayoutInflater());
       setContentView(binding.getRoot());


       //get the intent which got the user to this activity and all the extras
       Intent intent= this.getIntent();
       if (intent!=null)
       {
           Tasks tasks = (Tasks) intent.getSerializableExtra("task");
            if (tasks!=null) {
                binding.taskname.setText(tasks.getTask());

                binding.icon.setImageResource(tasks.getImage());

                binding.description.setText(tasks.getDescription());

                binding.neededtime.setText(tasks.getTime());

                binding.deadlinedate.setText(tasks.getDeadline());

            }


       }
    }
}