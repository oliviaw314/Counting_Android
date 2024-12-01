package com.example.myapplication;

import android.graphics.Color;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;

public class ExportPDF extends AppCompatActivity {

    private TextView message;
    private Button exportButton;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_export_pdf);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        message = findViewById(R.id.textView5);
        exportButton = findViewById(R.id.exportPDF);
        backButton = findViewById(R.id.backButton4);

        //button to go back to previous page
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        message.setText("You can save all the statistics about your text into a PDF file below!");
        exportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exportPDF();
            }
        });
    }

    private void exportPDF() {
        PdfDocument document = new PdfDocument();
        // assigns the width and height
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(300, 600, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);
        // drawing surface to add text
        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);

        // x-coordinate - horizontal position where text starts
        // y-coordinate - vertical position where text starts
        StatisticsPage1 statisticsPage1 = new StatisticsPage1();
        StatisticsPage2 statisticsPage2 = new StatisticsPage2();
        CreativeFeature creativeFeature = new CreativeFeature();
        String text = statisticsPage1.getMessage()+"\n"+statisticsPage2.getMessage()+"\n"+creativeFeature.getStatistic();
        canvas.drawText(text,10,25,paint);
        try {
            //makes the directory path the user's downloads - pdf saves to downloads
            String directoryPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
            File file = new File(directoryPath);
            if (!file.exists()) {
                // creates the directory if it doesn't exist
                file.mkdirs();
            }
            //concatenates file path and file name to write the pdf file to device's storage
            String filePath = directoryPath + "/textStatistics.pdf";
            document.writeTo(new FileOutputStream(filePath));
            // length_long = message stays for 3.5 seconds
            Toast.makeText(this, "PDF saved to " + filePath, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
            // length_short = message stays for 2 seconds
            Toast.makeText(this, "Error saving PDF: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        document.close();

    }
}