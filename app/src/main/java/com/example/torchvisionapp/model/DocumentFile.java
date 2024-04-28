package com.example.torchvisionapp.model;

import android.content.Context;
import android.widget.Toast;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class DocumentFile {
    private Context context;

    public DocumentFile(Context context) {
        this.context = context;
    }

    public void saveAsFile(String content, String fileName, String format, String path) {
        if (fileName.isEmpty() || format.isEmpty()) {
            Toast.makeText(context, "Please enter file name and format", Toast.LENGTH_SHORT).show();
        }
        File directory = new File(path);
        if (!directory.exists() && directory.isDirectory()) {
            directory.mkdirs();
        }
        File file = new File(directory, fileName + "." + format);
        file.setReadable(true);
        file.setWritable(true);
        Toast.makeText(context, "" + fileName + "." + format, Toast.LENGTH_SHORT).show();

        if (file.exists()) {
            Toast.makeText(context, "File already exists", Toast.LENGTH_SHORT).show();
            return;
        }

        String textToWrite = content;

        try {
            FileWriter writer = new FileWriter(file);

            writer.write(textToWrite);
            writer.flush();
            writer.close();

            Toast.makeText(context, "File saved successfully "+path, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {

            e.printStackTrace();
            Toast.makeText(context, "Error saving file", Toast.LENGTH_SHORT).show();
        }
    }
}
