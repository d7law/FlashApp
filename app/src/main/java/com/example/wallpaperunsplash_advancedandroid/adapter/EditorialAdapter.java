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

public class EditorialAdapter extends RecyclerView.Adapter<EditorialAdapter.EDVH> {
    public interface OnItemClickListener{
        void onItemClick(ImageFiles imageFiles);
    }
    public interface OnItemLongClickListener{
        void onItemLongClick(ImageFiles imageFiles);
    }
    //declare
    public Context context;
    public ArrayList<ImageFiles> imgArrayList;
    public OnItemClickListener onItemClickListener;
    public EditorialAdapter(){}
    public EditorialAdapter(Context context, ArrayList<ImageFiles> imgArrayList, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.imgArrayList = imgArrayList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public EDVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.image_item, parent, false);
        return new EDVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EDVH holder, int position) {
        holder.bind(imgArrayList.get(position), onItemClickListener);
    }

    @Override
    public int getItemCount() {
        return imgArrayList.size();
    }

    public static class EDVH extends RecyclerView.ViewHolder {
        public ImageView img;
        public TextView ownerName, idImg;
        public EDVH(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.ivRawImg);
            ownerName = itemView.findViewById(R.id.tvRawUsername);
            idImg = itemView.findViewById(R.id.tvRawId);
        }
        public void bind(final ImageFiles imageFiles, final OnItemClickListener listener){
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
        public void bind2(final ImageFiles imageFiles,final OnItemClickListener listener, final OnItemLongClickListener listenerLong){
            ownerName.setText(imageFiles.getOwnerName());
            idImg.setText(imageFiles.getId_img());
            Picasso.get().load(imageFiles.getImage()).into(img);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(imageFiles);
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listenerLong.onItemLongClick(imageFiles);
                    return true;
                }
            });
        }
    }
}
