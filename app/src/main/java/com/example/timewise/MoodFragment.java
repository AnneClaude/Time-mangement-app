package com.example.timewise;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import java.util.ArrayList;
import java.util.List;

public class MoodFragment extends Fragment {

    private final List<CheckBox> checkBoxList = new ArrayList<>();

    public MoodFragment() {
        // empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mood, container, false);

        CheckBox checkBox1 = view.findViewById(R.id.checkBox1);
        CheckBox checkBox2 = view.findViewById(R.id.checkBox2);
        CheckBox checkBox3 = view.findViewById(R.id.checkBox3);
        CheckBox checkBox4 = view.findViewById(R.id.checkBox4);
        CheckBox checkBox5 = view.findViewById(R.id.checkBox5);

        checkBoxList.add(checkBox1);
        checkBoxList.add(checkBox2);
        checkBoxList.add(checkBox3);
        checkBoxList.add(checkBox4);
        checkBoxList.add(checkBox5);

        // Set up listeners for each checkbox
        for (CheckBox checkBox : checkBoxList) {
            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    uncheckOthers(buttonView);
                }
            });
        }

        return view;
    }

    private void uncheckOthers(CompoundButton selectedCheckBox) {
        for (CheckBox checkBox : checkBoxList) {
            if (checkBox != selectedCheckBox) {
                checkBox.setChecked(false);
            }
        }
    }
}
