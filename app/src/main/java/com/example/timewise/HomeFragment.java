package com.example.timewise;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private ArrayList<Tasks> dataArrayList = new ArrayList<>();
    private ArrayList<Tasks> doneArrayList = new ArrayList<>();
    private ListAdapter listAdapter, listAdapterDone;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mAuth = FirebaseAuth.getInstance();
        String uid = mAuth.getCurrentUser().getUid(); // Get user ID
        databaseReference = FirebaseDatabase.getInstance().getReference("tasks/" + uid);

        // Display current date
        TextView textViewDate = view.findViewById(R.id.getDate);
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM d, ''yy", Locale.getDefault());
        textViewDate.setText(dateFormat.format(new Date()));

        // Initialize ListViews
        ListView listView = view.findViewById(R.id.listview);
        listAdapter = new ListAdapter(getActivity(), dataArrayList);
        listView.setAdapter(listAdapter);

        ListView listViewDone = view.findViewById(R.id.listviewDone);
        listAdapterDone = new ListAdapter(getActivity(), doneArrayList);
        listViewDone.setAdapter(listAdapterDone);

        // Open task details when clicking a task
        listView.setOnItemClickListener((parent, view1, position, id) -> {
            Tasks clickedTask = dataArrayList.get(position);
            Intent intent = new Intent(getActivity(), TaskDetailedActivity.class);
            intent.putExtra("task", clickedTask);
            startActivity(intent);
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageButton addTaskBtn = view.findViewById(R.id.AddNewTask);
        addTaskBtn.setOnClickListener(v -> {
            Intent go = new Intent(getActivity(), ActivityAddTask.class);
            startActivity(go);
        });

        fetchFirebaseItems();
    }

    private void fetchFirebaseItems() {
        //The fetchFirebaseItems() function retrieves task data from
        // a Firebase Realtime Database and updates two separate lists
        // based on each task’s completion status. It uses a
        // ValueEventListener to listen for data changes and responds
        // by clearing the current lists, categorizing the tasks into
        // either a "done" list or a "to-do" list, and then refreshing
        // the UI by notifying their respective adapters. If the data
        // fetch fails, it displays an error message to the user using
        // a toast.
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                dataArrayList.clear();
                doneArrayList.clear();
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    Tasks firebaseItem = itemSnapshot.getValue(Tasks.class);
                    if (firebaseItem != null) {
                        if (firebaseItem.getIsdone()) {
                            doneArrayList.add(firebaseItem);
                        } else {
                            dataArrayList.add(firebaseItem);
                        }
                    }
                }
                listAdapter.notifyDataSetChanged();
                listAdapterDone.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Failed to fetch data: "
                        + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void moveTask(Tasks task, boolean isDone) {
        String uid = mAuth.getCurrentUser().getUid();
        DatabaseReference taskRef = FirebaseDatabase.getInstance().getReference("tasks/" + uid);

        taskRef.orderByChild("task").equalTo(task.getTask()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    snapshot.getRef().child("isdone").setValue(isDone);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Error updating task: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Move task between lists
        if (isDone) {
            dataArrayList.remove(task);
            doneArrayList.add(task);
        } else {
            doneArrayList.remove(task);
            dataArrayList.add(task);
        }

        listAdapter.notifyDataSetChanged();
        listAdapterDone.notifyDataSetChanged();
    }
}
