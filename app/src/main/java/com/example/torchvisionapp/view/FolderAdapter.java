package com.example.torchvisionapp.view;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.torchvisionapp.R;

import java.util.List;

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.FolderViewHolder> {
    private List<String> folderList;
    private Context context;

    public FolderAdapter(Context context, List<String> folderList) {
        this.context = context;
        this.folderList = folderList;
    }

    @NonNull
    @Override
    public FolderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.folder_item, parent, false);
        return new FolderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FolderViewHolder holder, int position) {
        String folderName = folderList.get(position);
        holder.txtFolderName.setText(folderName);
    }

    @Override
    public int getItemCount() {
        return folderList.size();
    }

    public class FolderViewHolder extends RecyclerView.ViewHolder {
        TextView txtFolderName;

        public FolderViewHolder(@NonNull View itemView) {
            super(itemView);
            txtFolderName = itemView.findViewById(R.id.nameFolder);
        }
    }
}

