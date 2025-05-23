package com.example.timewise;

import static androidx.core.content.ContextCompat.getSystemService;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass to handle timer functionality.
 * Allows users to set a timer, start/stop it, and trigger notifications on completion.
 */
public class TimerFragment extends Fragment {

    // Parameter arguments for fragment initialization
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // Parameters to hold argument values
    private String mParam1;
    private String mParam2;

    // Default constructor
    public TimerFragment() {
        // Required empty public constructor
    }

    public static TimerFragment newInstance(String param1, String param2) {
        TimerFragment fragment = new TimerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    // UI Components
    private TextView Timer; // Displays the timer countdown in HH:mm:ss format
    private Button setTimeButton, startButton; // Buttons for setting time and starting/stopping the timer
    private CountDownTimer countDownTimer; // Countdown timer for the timer functionality
    private boolean isTimerRunning = false; // Tracks whether the timer is currently running
    private long timeInMillis = 0; // Stores the selected timer duration in milliseconds

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for the fragment
        return inflater.inflate(R.layout.fragment_timer, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize the UI components
        Timer = view.findViewById(R.id.timer); // Timer TextView
        setTimeButton = view.findViewById(R.id.SelectTime); // Button to set the timer
        startButton = view.findViewById(R.id.startTimer); // Button to start/stop the timer

        // Set up a listener for the "Set Time" button
        setTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the time picker dialog when the button is clicked
                openTimePicker(view);
            }
        });

        // Set up a listener for the "Start/Stop Timer" button
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isTimerRunning) { // If the timer is not running
                    startTimer(timeInMillis); // Start the timer

                    // Calculate the time when the notification should trigger
                    long afterT = System.currentTimeMillis() + timeInMillis;

                    // Create an intent for the notification service
                    Intent intent = new Intent(view.getContext(), NotificationService.class);

                    // Set up a pending intent for triggering the notification service
                    PendingIntent afterIntent = PendingIntent.getForegroundService(
                            view.getContext(),
                            2,
                            intent,
                            PendingIntent.FLAG_IMMUTABLE
                    );

                    // Get the AlarmManager service to set an alarm
                    AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, afterT, afterIntent);

                } else if (isTimerRunning) { // If the timer is running
                    stopTimer(); // Stop the timer
                }
            }
        });
    }

    /**
     * Opens a TimePickerDialog for the user to select the timer duration.
     */
    private void openTimePicker(View view) {
        int TimerHour = 0; // Default hour value
        int TimerMinute = 0; // Default minute value

        // Create and configure the TimePickerDialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                getContext(),
                (view1, hour, minute) -> {
                    // Convert selected hours and minutes to milliseconds
                    timeInMillis = (hour * 3600 + minute * 60) * 1000;
                },
                TimerHour,
                TimerMinute,
                true // Use 24-hour format
        );

        // Convert the timer duration to hours, minutes, and seconds
        int hours = (int) (timeInMillis / (1000 * 60 * 60));
        int minutes = (int) ((timeInMillis / (1000 * 60)) % 60);
        int seconds = (int) (timeInMillis / 1000) % 60;

        // Update the Timer TextView to display the selected time
        Timer.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));

        // Show the TimePickerDialog
        timePickerDialog.show();
    }


    private void startTimer(long timeInMillis) {
        // Initialize and start the countdown timer
        countDownTimer = new CountDownTimer(timeInMillis, 1000) { // Tick interval is 1 second
            @Override
            public void onTick(long millisUntilFinished) {
                // Calculate hours, minutes, and seconds from the remaining time
                int hours = (int) (millisUntilFinished / (1000 * 60 * 60));
                int minutes = (int) ((millisUntilFinished / (1000 * 60)) % 60);
                int seconds = (int) (millisUntilFinished / 1000) % 60;

                // Update the Timer TextView to show the countdown
                Timer.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
            }

            @Override
            public void onFinish() {
                // Reset the timer display and button label when finished
                Timer.setText("00:00:00");
                isTimerRunning = false;
                startButton.setText("Start Timer");
            }
        }.start();

        // Update the timer status and button label
        isTimerRunning = true;
        startButton.setText("Stop Timer");
    }

    /**
     * Stops the currently running countdown timer.
     */
    private void stopTimer() {
        if (countDownTimer != null) { // If the timer is running
            countDownTimer.cancel(); // Cancel the timer
            countDownTimer = null; // Clear the timer reference
        }
        // Reset the timer status and UI
        isTimerRunning = false;
        startButton.setText("Start Timer");
        Timer.setText("00:00:00"); // Reset the Timer TextView
    }
}
