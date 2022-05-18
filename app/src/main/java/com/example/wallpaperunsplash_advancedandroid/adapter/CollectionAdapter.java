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
import com.example.wallpaperunsplash_advancedandroid.models.CollectionPreFiles;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter.CAVH> {
    public Context context;
    public ArrayList<CollectionPreFiles> collectionArray;


    public CollectionAdapter(){}
    public CollectionAdapter(Context context, ArrayList<CollectionPreFiles> collectionArray) {
        this.context = context;
        this.collectionArray = collectionArray;
    }

    @NonNull
    @Override
    public CAVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.wcate_item, parent, false);
        return new CAVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CAVH holder, int position) {

        CollectionPreFiles modal = collectionArray.get(position);

        holder.tvNameCollection.setText(modal.getNameCollection());
        holder.tvTotalPhotoCollection.setText("Total: " + modal.getTotalPhotos());
        holder.tvOwnerCollection.setText("Owner: "+modal.getOwnerName());

        Picasso.get().load(modal.getImg1()).into(holder.ivCvphoto1);
        Picasso.get().load(modal.getImg2()).into(holder.ivCvphoto2);
        Picasso.get().load(modal.getImg3()).into(holder.ivCvphoto3);
    }

    @Override
    public int getItemCount() {
        return collectionArray.size();
    }

    public class CAVH extends RecyclerView.ViewHolder {
        public ImageView ivCvphoto1, ivCvphoto2, ivCvphoto3;
        public TextView tvNameCollection, tvTotalPhotoCollection, tvOwnerCollection;

        public CAVH(@NonNull View itemView) {
            super(itemView);
            ivCvphoto1 = itemView.findViewById(R.id.ivPreviewCollectionPhoto1);
            ivCvphoto2 = itemView.findViewById(R.id.ivPreviewCollection2);
            ivCvphoto3 = itemView.findViewById(R.id.ivPreviewCollection3);

            tvNameCollection = itemView.findViewById(R.id.tvNameCollection);
            tvTotalPhotoCollection = itemView.findViewById(R.id.tvTotalPhotoCollection);
            tvOwnerCollection = itemView.findViewById(R.id.tvOwnerCollection);
        }
    }

}
