package com.example.recipieapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class ViewRecipeActivity extends AppCompatActivity {
    private final int INGREDIENT_SIZE = 130;

    private ListView listViewIngredients;
    private TextView textViewInstructions;
    private EditText editTextAlarmTime;
    private ProgressBar progressBarAlarm;
    private Button buttonStartPauseTimer, buttonCancelTimer;

    private View.OnClickListener startTimerOnClick, pauseTimerOnClick;
    private Handler handlerTimer;
    private Runnable runnableOnSeconds;

    private MediaPlayer mediaPlayerAlarm;

    private String recipeID, recipeName, recipeDesc, recipeInstructions, recipeIngredientsStr;
    private String[] recipeIngredients;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recipe);

        listViewIngredients = findViewById(R.id.listViewIngredients);
        textViewInstructions = findViewById(R.id.textViewInstructions);
        editTextAlarmTime = findViewById(R.id.editTextAlarmTime);
        progressBarAlarm = findViewById(R.id.progressBarAlarm);
        buttonStartPauseTimer = findViewById(R.id.buttonStartPauseTimer);
        buttonCancelTimer = findViewById(R.id.buttonCancelTimer);

        Intent thisIntent = getIntent();
        recipeID = thisIntent.getStringExtra("recipe-id");
        recipeName = thisIntent.getStringExtra("recipe-name");
        recipeDesc = thisIntent.getStringExtra("recipe-desc");
        recipeInstructions = thisIntent.getStringExtra("recipe-instructions");
        recipeIngredientsStr = thisIntent.getStringExtra("recipe-ingredients");
        recipeIngredients = ingredientsToLst(recipeIngredientsStr);

        setTitle(recipeName.substring(0, 1).toUpperCase() + recipeName.substring(1));
        listViewIngredients.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listViewIngredients.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_multiple_choice, recipeIngredients));
        textViewInstructions.setText(recipeInstructions);

        ViewGroup.LayoutParams params = listViewIngredients.getLayoutParams();
        params.height = INGREDIENT_SIZE * recipeIngredients.length;

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

            buttonCancelTimer.setEnabled(true);

            buttonStartPauseTimer.setText("Pause");
            buttonStartPauseTimer.setOnClickListener(pauseTimerOnClick);

            editTextAlarmTime.setEnabled(false);
        };

        pauseTimerOnClick = view -> {
            // stop handler function
            stopCountingTime();

            // rename to start/resume timer
            buttonStartPauseTimer.setText("Resume");
            buttonStartPauseTimer.setOnClickListener(startTimerOnClick);
        };

        buttonStartPauseTimer.setOnClickListener(startTimerOnClick);

        buttonCancelTimer.setOnClickListener(view -> {
            progressBarAlarm.setProgress(0);

            buttonStartPauseTimer.setOnClickListener(startTimerOnClick);
            buttonStartPauseTimer.setText("Start");
            buttonStartPauseTimer.setEnabled(true);

            editTextAlarmTime.setEnabled(true);

            buttonCancelTimer.setEnabled(false);
            buttonCancelTimer.setText("Pause");

            stopCountingTime();

            editTextAlarmTime.setText("");

            if(mediaPlayerAlarm != null && mediaPlayerAlarm.isPlaying())
                mediaPlayerAlarm.stop();
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

                if(timeLeft <= 0)
                    onTimerReachedEnd();
            }
        };

        handlerTimer = new Handler();

    }

    private void onTimerReachedEnd() {
        stopCountingTime();

        buttonStartPauseTimer.setOnClickListener(startTimerOnClick);
        buttonStartPauseTimer.setText("Start");
        buttonStartPauseTimer.setEnabled(false);
        editTextAlarmTime.setText("Time is up!");
        editTextAlarmTime.setEnabled(true);

        buttonCancelTimer.setText("END");

        progressBarAlarm.setProgress(0);

        mediaPlayerAlarm = MediaPlayer.create(this,
                Settings.System.DEFAULT_ALARM_ALERT_URI);
        mediaPlayerAlarm.setLooping(true);
        mediaPlayerAlarm.start();

        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            vibrator.vibrate(
                    VibrationEffect.createOneShot(2000,
                            VibrationEffect.DEFAULT_AMPLITUDE));
        else
            vibrator.vibrate(2000);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.view_recipe_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.editRecipe) {
            editRecipe();

        } else if(item.getItemId() == R.id.deleteRecipe) {
            RecipeDAO db = RoomDBHelper.getInstance(this).recipeDAO();
            db.deleteRecipeById(recipeID);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void editRecipe() {
        Intent intent = new Intent(this, EditRecipeActivity.class);
        intent.putExtra("recipe-id", recipeID);
        intent.putExtra("recipe-name", recipeName);
        intent.putExtra("recipe-desc", recipeDesc);
        intent.putExtra("recipe-instructions", recipeInstructions);
        intent.putExtra("recipe-ingredients", recipeIngredientsStr);
        startActivity(intent);
        finish();
    }
}
