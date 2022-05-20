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
    public CollectionAdapter.OnItemClickListener listener;

    public CollectionAdapter(){}
    public CollectionAdapter(Context context, ArrayList<CollectionPreFiles> collectionArray, CollectionAdapter.OnItemClickListener listener) {
        this.context = context;
        this.collectionArray = collectionArray;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CAVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.wcate_item, parent, false);
        return new CAVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CAVH holder, int position) {
        holder.bind(collectionArray.get(position), listener);
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
        public void bind(final CollectionPreFiles collectionPreFiles, final CollectionAdapter.OnItemClickListener listener){
            tvNameCollection.setText(collectionPreFiles.getNameCollection());
            tvTotalPhotoCollection.setText("Total: "+collectionPreFiles.getTotalPhotos());
            tvOwnerCollection.setText("Owner: "+collectionPreFiles.getOwnerName());
            Picasso.get().load(collectionPreFiles.getImg1()).into(ivCvphoto1);
            Picasso.get().load(collectionPreFiles.getImg2()).into(ivCvphoto2);
            Picasso.get().load(collectionPreFiles.getImg3()).into(ivCvphoto3);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(collectionPreFiles);
                 }
            });
        }
    }
    public interface OnItemClickListener{
        void onItemClick(CollectionPreFiles collectionPreFiles);
    }
}
