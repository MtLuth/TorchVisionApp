package com.example.torchvisionapp.model;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class FileExplorer {

    Context context;

    public FileExplorer(Context context) {
        this.context =  context;
    }

    public ArrayList<File> loadExistingFolderFromPath(String filePath) {
        ArrayList<File> folderList = new ArrayList<>();
            File directory = new File(filePath);
            if (directory.exists() && directory.isDirectory()) {
                Log.i("directory", "complete");
                File[] files = directory.listFiles();
                if (files!=null) {
                    for (File file: files) {
                        if (file.isDirectory()) {
                            folderList.add(file);
                            Log.i("load", file.getName());
                        }
                    }
                }
            }
        return folderList;
    }
    public int countNumberOfFileInDirectory(String path) {
        int count = -1;
        File directory = new  File(path);
        if (directory.isDirectory() && directory.exists()) {
            count = directory.listFiles().length;
        }
        return count;
    }

    public ArrayList<File> loadAllTextFiles(String path) {
        ArrayList<File> fileTxtList = new ArrayList<>();
        File directory = new File(path);
        if (directory.exists() && directory.isDirectory()) {
            Log.i("pathname", path);
            File[] files = directory.listFiles();
            Log.i("pathname", files.length+"");
            if (files != null) {
                for (File file : files) {
                    Log.i("pathname", file.getName());
                    if (file.isFile() && file.getName().endsWith(".txt")) {
                        fileTxtList.add(file);
                        Log.i("load", "Added file: " + file.getName());
                    }
                }
            }
        }
        return fileTxtList;
    }
}
