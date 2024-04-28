package com.example.torchvisionapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.torchvisionapp.R;
import com.example.torchvisionapp.model.FileItem;

import java.io.File;
import java.util.ArrayList;

public class LoadExistingFileViewModel extends AndroidViewModel {

    public LoadExistingFileViewModel(@NonNull Application application) {
        super(application);
    }

    public ArrayList<FileItem> loadExistingFolder(String path) {
        ArrayList<FileItem> fileItems = new ArrayList<>();
        FileExplorer fileExplorer = new FileExplorer(getApplication().getApplicationContext());
        ArrayList<File> files = fileExplorer.loadExistingFolderFromPath(path);
        if (files != null) {
            for (File f : files) {
                FileItem fileItem = new FileItem();
                fileItem.setName(f.getName());
                fileItem.setIcon(R.drawable.iconfolder_actived);
                fileItem.setStatus(fileExplorer.countNumberOfFileInDirectory(path) + " files");
                fileItem.setPath(f.getPath());
            }
        }
        return fileItems;
    }
}
