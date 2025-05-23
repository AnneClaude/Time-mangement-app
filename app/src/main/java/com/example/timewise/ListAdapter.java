package com.example.timewise;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import java.util.ArrayList;

public class ListAdapter extends ArrayAdapter<Tasks> {

    //The list adapter acts as a bridge between the data (tasks) and
    // the UI component (like a ListView or RecyclerView),
    // converting each task into a visual item.
    // Its goal in the code is to display and update the task
    // lists (done and pending) dynamically when data changes.
    private ArrayList<Tasks> taskList;

    public ListAdapter(@NonNull Context context, ArrayList<Tasks> taskList) {
        super(context, R.layout.list_item, taskList);
        this.taskList = taskList;
    }

    @NonNull
    @Override


    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Tasks task = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        if (task != null) {
            ImageView imageView = convertView.findViewById(R.id.listImage);
            TextView name = convertView.findViewById(R.id.listName);
            TextView deadline = convertView.findViewById(R.id.deadline);
            CheckBox checkBox = convertView.findViewById(R.id.taskcheckbox);

            imageView.setImageResource(task.getImage());
            name.setText(task.getTask());
            deadline.setText(task.getDeadline());
            checkBox.setChecked(task.getIsdone());

            // Remove previous listeners
            checkBox.setOnCheckedChangeListener(null);

            // Set new listener to handle checkbox changes
            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                // Update the task's 'isdone' status based on checkbox state
                task.setIsdone(isChecked);

                // Find HomeFragment dynamically
                if (getContext() instanceof FragmentActivity) {
                    FragmentActivity activity = (FragmentActivity) getContext();
                    for (Fragment fragment : activity.getSupportFragmentManager().getFragments()) {
                        // If the fragment is an instance of HomeFragment
                        if (fragment instanceof HomeFragment) {
                            // Call the moveTask method to move the task based on its updated status
                            ((HomeFragment) fragment).moveTask(task, isChecked);
                            break;
                        }
                    }
                }
            });
        }
        return convertView;
    }
}
