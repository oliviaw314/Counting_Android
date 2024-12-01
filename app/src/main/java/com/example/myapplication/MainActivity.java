package com.example.myapplication;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

public class MainActivity extends AppCompatActivity {

    private Button uploadTextFile;
    private TextView intro;
    private List<String> text;
    private List<String> commonWords;
    private int sentenceCount;
    private ActivityResultLauncher<Intent> filePickerLauncher;
    private Button continueButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // putting commonWords into array lists
        AssetManager assets = this.getAssets();
        try (InputStream inputStream = assets.open("CommonWords")){
            //Log.d("first step",textOne.toString());
            commonWords = createTexts(inputStream);
            //Log.d("TAG", commonWords.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        intro = (TextView) findViewById(R.id.textView);
        String message = "Welcome! Please select the text file or pdf file you would like to analyze below.";
        intro.setText(message);
        //Log.d("intro", intro.getText().toString());

        uploadTextFile = (Button) findViewById(R.id.file_picker);
        uploadTextFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                // allows all file types including text file & pdf file
                intent.setType("*/*");
                String[] mimeTypes = {"application/pdf", "text/plain"};
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);

                filePickerLauncher.launch(intent);
                //Log.d("happens","true");
            }
        });

        filePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    Log.e("happensAfter","true");
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri uri = result.getData().getData();

                        try (InputStream inputStream = getContentResolver().openInputStream(uri)) {
                        if ("application/pdf".equals(getContentResolver().getType(uri))) {
                          text = readTextFromPdf(inputStream);
                        }
                        else if ("text/plain".equals(getContentResolver().getType(uri))) {
                                text = createTexts(inputStream);
                        }
                        } catch (IOException e) {
                            //Log.e("FileRead", "Error reading file", e);
                        }
                    }
                });

        // removes all common words from text
        List<String> modifiedText = new ArrayList<>();

        for (String word : text) {
            boolean containsCommon = false;
            for (String commonWord : commonWords) {
                if (word.equals(commonWord)) {
                    containsCommon = true;
                    break; // exit the loop to prevent further checks (since alr found that equals)
                }
            }
            if (!containsCommon) {
                modifiedText.add(word);
            }
        }
        text=modifiedText;


        //button to continue to next page
        continueButton = (Button) findViewById(R.id.continueButton1);

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,StatisticsPage1.class);
                startActivity(intent);
            }
        });

    }

    public List<String> getText() {
        return text;
    }

    public int getSentenceCount() {
        return sentenceCount;
    }

    private List<String> createTexts(InputStream inputStream) throws IOException {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {

            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append(" ");
            }

            //count the number of sentences
            sentenceCount = 0;
            for (char ch: stringBuilder.toString().toCharArray()) {
                if (ch == '.' || ch == '?' || ch == '!') {
                    sentenceCount++;
                }
            }


            // Keep only lowercase letters, spaces, and apostrophes
            String normalizedText = stringBuilder.toString().toLowerCase().replaceAll("[^a-z\\s']", "");
            //Log.d("apostrophes", normalizedText);

            return Arrays.asList(normalizedText.split("\\s+"));
        }
    }

    private List<String> readTextFromPdf(InputStream inputStream) {
        try {
            // Access PDF from assets
            PdfReader reader = new PdfReader(inputStream);
            StringBuilder text = new StringBuilder();

            for (int i = 1; i <= reader.getNumberOfPages(); i++) {
                text.append(PdfTextExtractor.getTextFromPage(reader, i));
            }
            reader.close();

            //count the number of sentences
            sentenceCount = 0;
            for (char ch: text.toString().toCharArray()) {
                if (ch == '.' || ch == '?' || ch == '!') {
                    sentenceCount++;
                }
            }

            String normalizedText = text.toString().toLowerCase().replaceAll("[^a-z\\s']", "");

            return Arrays.asList(normalizedText.split("\\s+"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }


}