package com.example.torchvisionapp.viewmodel;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;

public class FileExplorer {

    Context context;

    public FileExplorer(Context context) {
        this.context =  context;
    }

    public ArrayList<File> loadExistingFolderFromPath(String filePath) {
        ArrayList<File> folderList = new ArrayList<>();
        try {
            File directory = new File(filePath);
            if (directory.exists() && directory.isDirectory()) {
                Log.i("directory", "complete");
                File[] files = directory.listFiles();
                if (files!=null) {
                    for (File file: files) {
                        if (file.isDirectory()) {
                            folderList.add(file);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
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
}
