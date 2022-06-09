package com.example.wallpaperunsplash_advancedandroid.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wallpaperunsplash_advancedandroid.R;
import com.example.wallpaperunsplash_advancedandroid.models.ImageFiles;

import java.util.ArrayList;

public class PhotoOfUserAdapter extends RecyclerView.Adapter<PhotoOfUserAdapter.PoUAVH> {
    private ArrayList<ImageFiles> arrayList;
    private EditorialAdapter.OnItemClickListener listener;

    public PhotoOfUserAdapter(ArrayList<ImageFiles> arrayList, EditorialAdapter.OnItemClickListener listener) {
        this.arrayList = arrayList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PoUAVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_item, parent, false);
        return new PoUAVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PoUAVH holder, int position) {
        holder.bind(arrayList.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class PoUAVH extends RecyclerView.ViewHolder {

        public ImageView img;
        public TextView ownerName, idImg;

        public PoUAVH(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.ivRawImg);
            ownerName = itemView.findViewById(R.id.tvRawUsername);
            idImg = itemView.findViewById(R.id.tvRawId);
        }

        public void bind(ImageFiles imageFiles, EditorialAdapter.OnItemClickListener listener) {
            ownerName.setText("");
            idImg.setText("");

        }
    }
}
