package com.example.wallpaperunsplash_advancedandroid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wallpaperunsplash_advancedandroid.R;
import com.example.wallpaperunsplash_advancedandroid.models.ImageFiles;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class WallpaperAdapter extends RecyclerView.Adapter<WallpaperAdapter.WPVH> {
    public Context context;
    public ArrayList<ImageFiles> imgArrayList;
    public EditorialAdapter.OnItemClickListener listener;
    public WallpaperAdapter(Context context, ArrayList<ImageFiles> imgArrayList, EditorialAdapter.OnItemClickListener listener) {
        this.context = context;
        this.imgArrayList = imgArrayList;
        this.listener = listener;
    }
    public WallpaperAdapter(){}
    @NonNull
    @Override
    public WPVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.wallpaper_item, parent, false);
        return new WPVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WPVH holder, int position) {
//        ImageFiles modal = imgArrayList.get(position);
//        holder.ownerName.setText(modal.getOwnerName());
//        holder.idImg.setText(modal.getId_img());
//        ImageView iv = holder.img;
//        Picasso.get().load(modal.getImage()).into(iv);
//    }
        holder.bind(imgArrayList.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return imgArrayList.size();
    }

    public class WPVH extends RecyclerView.ViewHolder{
        public ImageView img;
        public TextView ownerName, idImg;
        public WPVH(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.ivRawWall);
            ownerName = itemView.findViewById(R.id.tvRawUsername);
            idImg = itemView.findViewById(R.id.tvRawId);
        }
        public void bind(final ImageFiles imageFiles, final EditorialAdapter.OnItemClickListener listener){
            ownerName.setText(imageFiles.getOwnerName());
            idImg.setText(imageFiles.getId_img());
            Picasso.get().load(imageFiles.getImage()).into(img);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(imageFiles);
                }
            });
        }
    }
}
