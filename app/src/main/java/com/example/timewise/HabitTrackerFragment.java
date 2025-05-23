package com.example.timewise;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView; // A calendar interface for date selection.
import android.widget.TextView; // Displays text on the UI.
import android.widget.Toast; // Displays short messages to the user.

import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat; // For formatting dates.
import java.util.ArrayList; // List to store selected dates.
import java.util.Collections; // Utility for sorting lists.
import java.util.Locale; // For consistent date formatting based on the locale.

/**
 * A simple {@link Fragment} subclass to track habits using a calendar.
 */
public class HabitTrackerFragment extends Fragment {
    // UI components
    private CalendarView calendarView;
    private TextView selectedDatesText, longestStreakText, currentStreakText;

    // List to store marked dates (in milliseconds since the epoch)
    private ArrayList<Long> markedDates;

    // default constructor
    public HabitTrackerFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        markedDates = new ArrayList<>(); // Initialize the list of marked dates.
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_habit_tracker, container, false);

        // Bind UI elements to layout components
        calendarView = view.findViewById(R.id.calendarView);
        selectedDatesText = view.findViewById(R.id.selectedDatesText);
        longestStreakText = view.findViewById(R.id.longestStreak);
        currentStreakText = view.findViewById(R.id.thisStreak);

        // Add a listener to the CalendarView for date selection
        calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
            long selectedDate = getTimestamp(year, month, dayOfMonth); // Get the selected date in milliseconds.
            long today = getTodayTimestamp(); // Get today's date in milliseconds.

            // Prevent users from selecting future dates.
            if (selectedDate > today) {
                Toast.makeText(getContext(), "You cannot mark future dates!", Toast.LENGTH_SHORT).show();
                return;
            }
            // Prevent users from modifying past data.
            if (selectedDate < today) {
                Toast.makeText(getContext(), "You cannot change past data!", Toast.LENGTH_SHORT).show();
                return;
            }

            //  marking/unmarking the selected date.
            if (markedDates.contains(selectedDate)) {
                markedDates.remove(selectedDate); // Unmark the date.
                Toast.makeText(getContext(), "Date unmarked", Toast.LENGTH_SHORT).show();
            } else {
                markedDates.add(selectedDate); // Mark the date.
                Toast.makeText(getContext(), "Date marked", Toast.LENGTH_SHORT).show();
            }

            updateSelectedDates(); // Refresh the list of selected dates.
            calculateStreaks(); // Update streak calculations.
        });

        return view;
    }

    /**
     * Get today's date in milliseconds, with time reset to midnight.
     */
    private long getTodayTimestamp() {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.set(java.util.Calendar.HOUR_OF_DAY, 0);
        calendar.set(java.util.Calendar.MINUTE, 0);
        calendar.set(java.util.Calendar.SECOND, 0);
        calendar.set(java.util.Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis(); // Returns time in milliseconds.
    }

    /**
     * Get a timestamp for a specific date, with time set to midnight.
     */
    private long getTimestamp(int year, int month, int day) {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.set(year, month, day, 0, 0, 0); // Set to the specific date with zeroed time.
        calendar.set(java.util.Calendar.MILLISECOND, 0); // Remove milliseconds.
        return calendar.getTimeInMillis();
    }

    /**
     * Update the UI with the list of selected (marked) dates.
     */
    private void updateSelectedDates() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()); // Format dates to `dd/MM/yyyy`.
        ArrayList<Long> sortedDates = new ArrayList<>(markedDates);
        Collections.sort(sortedDates); // Sort dates in ascending order.

        // Build a string of selected dates to display.
        StringBuilder datesString = new StringBuilder("Selected Dates:\n");
        for (Long date : sortedDates) {
            datesString.append(sdf.format(date)).append("\n"); // Append each date in readable format.
        }

        selectedDatesText.setText(datesString.toString()); // Update the TextView.
    }

    /**
     * Calculate the longest streak and current streak of consecutive marked dates.
     */
    private void calculateStreaks() {
        if (markedDates.isEmpty()) {
            // No dates marked; reset streaks to zero.
            longestStreakText.setText("Longest Streak: 0");
            currentStreakText.setText("Current Streak: 0");
            return;
        }
        // Sort marked dates to calculate streaks.
        ArrayList<Long> sortedDates = new ArrayList<>(markedDates);
        Collections.sort(sortedDates);

        int longestStreak = 1; // Start with a minimum streak of 1.
        int currentStreak = 1;

        // Iterate through the sorted dates to calculate streaks.
        for (int i = 1; i < sortedDates.size(); i++) {
            long diff = sortedDates.get(i) - sortedDates.get(i - 1); // Calculate difference between consecutive dates.

            // Check if dates are consecutive (difference of 1 day).
            if (diff == 24 * 60 * 60 * 1000) {
                currentStreak++; // Extend the current streak.
                longestStreak = Math.max(longestStreak, currentStreak); // Update the longest streak if needed.
            } else {
                currentStreak = 1; // Reset the current streak if not consecutive.
            }
        }

        // Update the UI with calculated streaks.
        longestStreakText.setText("Longest Streak: " + longestStreak);
        currentStreakText.setText("Current Streak: " + currentStreak);
    }
}
