package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.w3c.dom.Text;
import java.util.Random;

public class StatisticsPage2 extends AppCompatActivity {

    private Button goBack;
    private Button continueButton;
    private TextView text;
    private EditText insertNumber;
    private Button generateButton;
    private String paragraphMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_statistics_page2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        text = findViewById(R.id.textView3);
        text.setText("Welcome to random paragraph generator! Insert a number between 1-100 below to determine how random/unqiue you want your paragraph to be. 1 would be the least random (most frequently occuring words) paragraph and 100 would be the most unique and random paragraph!");


        generateButton = findViewById(R.id.generateParagraphButton);
        // button to generate random paragraph
        generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateRandomParagraph();
            }
        });


        insertNumber = findViewById(R.id.insertNumber);
        //button to continue to next page
        continueButton = (Button) findViewById(R.id.continueButton2);

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StatisticsPage2.this,CreativeFeature.class);
                startActivity(intent);
            }
        });

        //button to go back to previous page
        goBack = (Button) findViewById(R.id.backButton2);

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public String getMessage() {
        return paragraphMessage;
    }

    private void generateRandomParagraph() {
        String input = insertNumber.getText().toString();

        // if user didn't input anything into textview --> i.e. input is empty
        if (input.isEmpty()) {
            // toast.makeText is method where a short message as a pop-up is displayed to the user
            Toast.makeText(this, "Please enter a number between 1 and 100", Toast.LENGTH_SHORT).show();
            return;
        }

        // below making sure that number is not invalid and is between 1-100
        int degreeOfRandom;
        try {
            degreeOfRandom = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid number", Toast.LENGTH_SHORT).show();
            return;
        }

        if (degreeOfRandom < 1 || degreeOfRandom > 100) {
            Toast.makeText(this, "Please enter a number between 1 and 100", Toast.LENGTH_SHORT).show();
            return;
        }

        MainActivity mainActivity = new MainActivity();
        StringBuilder paragraph = new StringBuilder();
        Random random = new Random();

        // Calculate range based on temperature -- range would be from the beg of text to the percentage deriving from temp
        // uses zero-based indexing, index starting from 0
        int range = Math.max(1, (int) (mainActivity.getText().size() * (degreeOfRandom / 100.0)));

        for (int i = 0; i < 50; i++) {
            // selects random index within range
            int index = random.nextInt(range);
            String word = mainActivity.getText().get(index);
            paragraph.append(word);
            paragraph.append(" ");
        }

        paragraphMessage = "Your random paragraph!\n"+paragraph.toString().trim();
        text.setText(paragraphMessage);

    }


}