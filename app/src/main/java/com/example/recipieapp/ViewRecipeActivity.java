package com.example.recipieapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class ViewRecipeActivity extends AppCompatActivity {
    private ListView listViewIngredients;
    private TextView textViewInstructions, textViewName;
    private EditText editTextAlarmTime;
    private ProgressBar progressBarAlarm;
    private Button buttonStartTimer, buttonRestartTimer;

    private View.OnClickListener startTimerOnClick, pauseTimerOnClick;
    Handler handlerTimer;
    Runnable runnableOnSeconds;

    private String recipeName, recipeDesc, recipeInstructions;
    private String[] recipeIngredients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recipe);

        textViewName = findViewById(R.id.textViewName1);
        listViewIngredients = findViewById(R.id.listViewIngredients);
        textViewInstructions = findViewById(R.id.textViewInstructions);
        editTextAlarmTime = findViewById(R.id.editTextAlarmTime);
        progressBarAlarm = findViewById(R.id.progressBarAlarm);
        buttonStartTimer = findViewById(R.id.buttonStartTimer);
        buttonRestartTimer = findViewById(R.id.buttonRestartTimer);

        Intent thisIntent = getIntent();
        recipeName = thisIntent.getStringExtra("recipe-name");
        recipeDesc = thisIntent.getStringExtra("recipe-desc");
        recipeInstructions = thisIntent.getStringExtra("recipe-instructions");
        recipeIngredients = ingredientsToLst(thisIntent.getStringExtra("recipe-ingredients"));

        textViewName.setText(recipeName);
        listViewIngredients.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listViewIngredients.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_multiple_choice, recipeIngredients));
        textViewInstructions.setText(recipeInstructions);

        startTimerOnClick = view -> {
            // get time from user
            // at most should be HH:MM:SS
            String inputAlarm = editTextAlarmTime.getText().toString();
            if(inputAlarm.isEmpty()) {
                Toast.makeText(ViewRecipeActivity.this, "Must have a time to " +
                        "start the timer!", Toast.LENGTH_SHORT).show();
                return;
            } else if(inputAlarm.split(":").length > 3) {
                Toast.makeText(ViewRecipeActivity.this, "Should be in HH:MM:SS format!"
                        , Toast.LENGTH_SHORT).show();
                return;
            }

            int timeInSeconds = getAlarmInputTimeInSeconds();
            progressBarAlarm.setMax(timeInSeconds);

            startCountingTime();

            buttonRestartTimer.setEnabled(true);

            buttonStartTimer.setText("Pause");
            buttonStartTimer.setOnClickListener(pauseTimerOnClick);

            editTextAlarmTime.setEnabled(false);
            // on finish, should make some sort of noise/notification
            // start restart time
        };

        pauseTimerOnClick = view -> {
            // stop handler function
            stopCountingTime();
            // rename to start/resume timer
            buttonStartTimer.setText("Resume");
            buttonStartTimer.setOnClickListener(startTimerOnClick);

            editTextAlarmTime.setEnabled(false);
        };

        buttonStartTimer.setOnClickListener(startTimerOnClick);

        buttonRestartTimer.setOnClickListener(view -> {
            progressBarAlarm.setProgress(0);
        });

        // this runs every 10 seconds, used by handler to
        // update things every second
        runnableOnSeconds = new Runnable() {
            public void run() {
                progressBarAlarm.setProgress(progressBarAlarm.getProgress() + 1);

                // infer time from progress
                int timeLeft = progressBarAlarm.getMax() - progressBarAlarm.getProgress();

                int hours = timeLeft / (60 * 60);
                int minutes = (timeLeft % (60 * 60)) / 60;
                int seconds = timeLeft % 60;
                editTextAlarmTime.setText(
                        String.format("%02d:%02d:%02d", hours, minutes, seconds));

                handlerTimer.postDelayed(this, 1000);

                if(timeLeft == 0)
                    onTimerReachedEnd();
            }
        };

        handlerTimer = new Handler();
    }

    private void onTimerReachedEnd() {
        stopCountingTime();

        buttonStartTimer.setOnClickListener(startTimerOnClick);
        buttonStartTimer.setText("Start");
        buttonRestartTimer.setEnabled(false);

        editTextAlarmTime.setText("");
        editTextAlarmTime.setEnabled(true);

        progressBarAlarm.setProgress(0);
    }

    private int getAlarmInputTimeInSeconds() {
        int inputTimeHours, inputTimeMinutes, inputTimeSeconds;
        String[] inputtedTimes = editTextAlarmTime.getText().toString().split(":");

        inputTimeHours = inputTimeMinutes = 0;

        inputTimeSeconds = Integer.parseInt(inputtedTimes[inputtedTimes.length - 1]);
        if(inputtedTimes.length >= 2)
            inputTimeMinutes = Integer.parseInt(inputtedTimes[inputtedTimes.length - 2]);
        if(inputtedTimes.length == 3)
            inputTimeHours = Integer.parseInt(inputtedTimes[0]);

        return inputTimeSeconds + 60*inputTimeMinutes + 60*60*inputTimeHours;
    }

    private void startCountingTime() {
        handlerTimer.postDelayed(runnableOnSeconds, 1000);
    }

    private void stopCountingTime() {
        handlerTimer.removeCallbacks(runnableOnSeconds);
    }

    private String[] ingredientsToLst(String ingredientsStr) {
        return ingredientsStr.split(";");
    }
}