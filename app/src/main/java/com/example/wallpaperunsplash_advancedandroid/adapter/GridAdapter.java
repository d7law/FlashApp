package com.example.wallpaperunsplash_advancedandroid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wallpaperunsplash_advancedandroid.R;
import com.example.wallpaperunsplash_advancedandroid.models.ImageFiles;

import java.util.ArrayList;

public class GridAdapter extends RecyclerView.Adapter<EditorialAdapter.EDVH> {

    public Context context;
    public ArrayList<ImageFiles> imgArrayList;
    public EditorialAdapter.OnItemClickListener listener;
    public GridAdapter() {}

    public GridAdapter(Context context, ArrayList<ImageFiles> imgArrayList, EditorialAdapter.OnItemClickListener listener) {
        this.context = context;
        this.imgArrayList = imgArrayList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public EditorialAdapter.EDVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.grid_photo_item, parent, false);
        return new EditorialAdapter.EDVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EditorialAdapter.EDVH holder, int position) {
//        ImageFiles modal = imgArrayList.get(position);
//
//        holder.ownerName.setText(modal.getOwnerName());
//        holder.idImg.setText(modal.getId_img());
//        ImageView iv = holder.img;
//        Picasso.get().load(modal.getImage()).into(iv);
        holder.bind(imgArrayList.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return imgArrayList.size();
    }
}
