package com.example.torchvisionapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.torchvisionapp.R;
import com.example.torchvisionapp.model.FileExplorer;
import com.example.torchvisionapp.model.FileItem;

import java.io.File;
import java.util.ArrayList;

public class TextConverterViewModel extends AndroidViewModel {
    private ArrayList<File> listFolders;
    private ArrayList<FileItem> listFileItem;
    public TextConverterViewModel(@NonNull Application application) {
        super(application);
    }
    public ArrayList<FileItem> loadFolderList(File path) {
        listFolders = new ArrayList<>();
        listFileItem = new ArrayList<>();
        FileExplorer fileExplorer = new FileExplorer(getApplication().getApplicationContext());
        listFolders = fileExplorer.loadExistingFolderFromPath(path.getPath());
        if (listFolders != null) {
            for (File f: listFolders) {
                if (f.isDirectory()) {
                    FileItem item = new FileItem();
                    item.setName(f.getName());
                    item.setIcon(R.drawable.iconfolder_actived);
                    item.setPath(f.getPath());
                    item.setStatus(fileExplorer.countNumberOfFileInDirectory(f.getPath())+" files");
                    listFileItem.add(item);
                }
            }
        }
        return listFileItem;
    }
}
