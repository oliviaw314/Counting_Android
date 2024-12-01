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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StatisticsPage1 extends AppCompatActivity {

    private Button topFive;
    private TextView statistics;
    private Button goBack;
    private Button continueButton;
    private StringBuilder message;
    private List<Integer> frequencies;
    private List<String> modifiedList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_statistics_page1);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        statistics = (TextView) findViewById(R.id.textView2);
        MainActivity mainActivity = new MainActivity();

        frequencies = new ArrayList<>();
        modifiedList = new ArrayList<>();
        countFrequency(mainActivity.getText(), modifiedList, frequencies);
        sortText(modifiedList, frequencies);

        message = new StringBuilder();
        message.append("Some fun statistics about your text!\n"+"Word count: "+mainActivity.getText().size()+"\n Sentence count: "+mainActivity.getSentenceCount()+"\nMost common words: \n");

        int count = 0;
        while (count<5) {
            message.append("\n"+(count+1)+". '"+modifiedList.get(modifiedList.size()-1-count)+"' with "+frequencies.get(frequencies.size()-1-count)+" occurrences");
            count++;
        }

        //counts the number of unique words and identifies them
        int noOfUniqueWords = 0;
        StringBuilder uniqueWordMessage = new StringBuilder();
        uniqueWordMessage.append("Unique words: \n");
        for (int i=0; i<modifiedList.size(); i++) {
            if (frequencies.get(i)==1) {
                noOfUniqueWords++;
                uniqueWordMessage.append(modifiedList.get(i)+", ");
            }
        }
        message.append("\nNumber of unique words: "+noOfUniqueWords);
        message.append("\n"+uniqueWordMessage);

        statistics.setText(message);

        //button to continue to next page
        continueButton = (Button) findViewById(R.id.continueButton);

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StatisticsPage1.this, StatisticsPage2.class);
                startActivity(intent);
            }
        });

        //button to go back to previous page
        goBack = (Button) findViewById(R.id.backButton);

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    public StringBuilder getMessage() {
        return message;
    }

    public List<Integer> getFrequencies() {
        return frequencies;
    }

    public List<String> getModifiedList() {
        return modifiedList;
    }

    private void countFrequency(List<String> chosenText, List<String> modifiedList, List<Integer> frequencies) {
        for (String word : chosenText) {
            // if word appears more than once in text
            if (modifiedList.contains(word)) {
                // have to check from modified list not text because text will always contain its own word
                int index = modifiedList.indexOf(word);
                frequencies.set(index, frequencies.get(index)+1);
            }
            else {
                modifiedList.add(word);
                frequencies.add(1);
            }
        }
    }


    // sorts words based on their frequencies
    private void sortText(List<String> modifiedList, List<Integer> frequencies) {

        // using selection sort
        for (int i=0; i<frequencies.size(); i++) {
            int minNumber = frequencies.get(i);
            int minIndex = i;

            // finding the smallest number remaining in the array
            for (int j=i; j<frequencies.size(); j++) {
                if (frequencies.get(j)<minNumber) {
                    minNumber = frequencies.get(j);
                    minIndex = j;
                }
            }
            int temp = frequencies.get(i);
            frequencies.set(i, minNumber);
            frequencies.set(minIndex, temp);

            String temp2 = modifiedList.get(i);
            modifiedList.set(i, modifiedList.get(minIndex));
            modifiedList.set(minIndex, temp2);
        }
    }

}