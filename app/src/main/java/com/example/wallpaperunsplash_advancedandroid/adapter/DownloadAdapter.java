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
import com.example.wallpaperunsplash_advancedandroid.adapter.roomDatabase.PathDownload;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

public class DownloadAdapter extends RecyclerView.Adapter<DownloadAdapter.DAVH> {
    public DownloadAdapter(Context context, List<PathDownload> arrayList, OnItemClickListener listener) {
        this.context = context;
        this.arrayList = arrayList;
        this.listener = listener;
    }

    public Context context;
    public List<PathDownload> arrayList;
    public OnItemClickListener listener;
    @NonNull
    @Override
    public DAVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.grid_photo_item, parent, false);
        return new DAVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DAVH holder, int position) {
        holder.bind(arrayList.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class DAVH extends RecyclerView.ViewHolder {
        public ImageView img;
        public TextView ownerName, idImg;
        public DAVH(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.ivRawImg);
            img.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ownerName = itemView.findViewById(R.id.tvRawUsername);
            idImg = itemView.findViewById(R.id.tvRawId);
        }

        public void bind(final PathDownload paths, final OnItemClickListener listener) {
            ownerName.setText("");
            idImg.setText("");
            File file = new File(paths.getPath());
            Picasso.get().load(file).into(img);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(paths);
                }
            });
        }
    }
    public interface OnItemClickListener{
        void onItemClick(PathDownload pathDownload);
    }
}
