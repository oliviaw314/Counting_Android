package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.os.SystemClock;
import android.widget.Chronometer;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CreativeFeature extends AppCompatActivity {

    private TextView message;
    private Button start;
    private Button stop;
    private Button backButton;
    private Button continueButton;
    private Chronometer chronometer;
    private boolean isRunning = false;
    private String statistic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_creative_feature2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        message = findViewById(R.id.textView4);
        message.setText("Welcome to reading time estimator! When you press start, we will display a piece of text. Please start reading at your normal pace and when you have finished reading, press stop. We will calculate your reading speed and estimate how long it will take for you to read your chosen text. Enjoy! ");

        chronometer = findViewById(R.id.chronometer);
        start = findViewById(R.id.startButton);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // makes sure that start hasn't already been pressed
                if (!isRunning) {
                    message.setText("Mr. and Mrs. Dursley, of number four, Privet Drive, were proud to say" +
                            "that they were perfectly normal, thank you very much. They were the last" +
                            "people you'd expect to be involved in anything strange or mysterious," +
                            "because they just didn't hold with such nonsense." +
                            "Mr. Dursley was the director of a firm called Grunnings, which made" +
                            "drills. He was a big, beefy man with hardly any neck, although he did" +
                            "have a very large mustache. Mrs. Dursley was thin and blonde and had" +
                            "nearly twice the usual amount of neck, which came in very useful as she" +
                            "spent so much of her time craning over garden fences, spying on the neighbors.");
                    // begins counting from 0
                    chronometer.setBase(SystemClock.elapsedRealtime());
                    chronometer.start();
                    isRunning = true;
                }
            }
        });


        stop = findViewById(R.id.stopButton);

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // checks if the chronometer is running
                // e.g. if the user just presses stop when it is not running nothing happens
                if (isRunning) {
                    chronometer.stop();
                    isRunning = false;

                    // calculate the reading time for sample text and estimated reading time for chosen text
                    long elapsedMilliseconds = SystemClock.elapsedRealtime() - chronometer.getBase();

                    // time is less than 10 seconds
                    if (elapsedMilliseconds<10000) {
                        Toast.makeText(CreativeFeature.this,"It seems you finished reading too quickly. Please read at your normal pace to ensure accurate results. Press 'Start' to try again.",Toast.LENGTH_LONG).show();
                    }

                    //convert the milliseconds into minutes (6000f represents 60,000 milliseconds --> 1 minute)
                    float elapsedMinutes = elapsedMilliseconds / 60000f;
                    //calculates words per minute -- 112 represents total word count of sample text
                    float readingSpeed = 112/ elapsedMinutes;

                    MainActivity mainActivity = new MainActivity();
                    float estimatedTime = mainActivity.getText().size() / readingSpeed;

                    statistic = "Estimated time to read the actual text: " +estimatedTime+" minutes.";
                    message.setText(statistic);
                }
            }
        });

        //button to continue to next page
        continueButton = (Button) findViewById(R.id.continueButton3);

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreativeFeature.this, ExportPDF.class);
                startActivity(intent);
            }
        });

        //button to go back to previous page
        backButton = (Button) findViewById(R.id.backButton3);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public String getStatistic() {
        return statistic;
    }
}