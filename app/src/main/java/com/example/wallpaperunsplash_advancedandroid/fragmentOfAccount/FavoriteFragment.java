package com.example.wallpaperunsplash_advancedandroid.fragmentOfAccount;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.wallpaperunsplash_advancedandroid.R;
import com.example.wallpaperunsplash_advancedandroid.RawImageActivity;
import com.example.wallpaperunsplash_advancedandroid.adapter.EditorialAdapter;
import com.example.wallpaperunsplash_advancedandroid.adapter.GridAdapter;
import com.example.wallpaperunsplash_advancedandroid.fragmentMain.AccountFragment;
import com.example.wallpaperunsplash_advancedandroid.models.ImageFiles;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FavoriteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavoriteFragment extends Fragment {
    RecyclerView rv;
    GridAdapter adapter;
    ArrayList<ImageFiles> arrayList;
    String img, id, owner;
    FirebaseStorage fStorage;
    StorageReference refStorage;
    FirebaseDatabase fData;
    DatabaseReference refData;
    TextView tvNull;
    int i = 1;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FavoriteFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FavoriteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FavoriteFragment newInstance(String param1, String param2) {
        FavoriteFragment fragment = new FavoriteFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        arrayList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        id = AccountFragment.userID;

        tvNull = view.findViewById(R.id.tvNonImg);
        rv = view.findViewById(R.id.rvUploaded);
        EditorialAdapter.OnItemClickListener listener;
        listener = new EditorialAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ImageFiles imageFiles) {
                Intent intent = new Intent(getContext(), RawImageActivity.class);
                intent.putExtra("uriOfUserPhotos", imageFiles.getImage());
                intent.putExtra("IsFavorite", true);
                startActivity(intent);
            }
        };
        EditorialAdapter.OnItemLongClickListener listenerLong;
        listenerLong = new EditorialAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(ImageFiles imageFiles) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Do you want to remove this image from the Favorite");
                builder.setTitle("Remove");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String idImg = imageFiles.getId_img();
                        deletePhoto(idImg);
                        //Toast.makeText(getContext(), "Deleted!!!", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        };
        adapter = new GridAdapter(getContext(), arrayList, listener, listenerLong);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        rv.setAdapter(adapter);
        rv.setLayoutManager(layoutManager);
        rv.setHasFixedSize(true);

        fStorage = FirebaseStorage.getInstance();
        refStorage = fStorage.getReference();
        fData = FirebaseDatabase.getInstance();
        refData = fData.getReference();
        loadImage();
        adapter.notifyDataSetChanged();
    }

    private void loadImage() {
        Query qPhoto = refData.child("users").child(id).child("photoLove");
        qPhoto.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String imgLink = dataSnapshot.getValue().toString();
                    String idImg = dataSnapshot.getKey();
                    String count = String.valueOf(i);
                    arrayList.add(new ImageFiles(idImg, imgLink, count));
                    i++;
                }
                i = 1;
                adapter.notifyDataSetChanged();
                if (arrayList.size() != 0) tvNull.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void deletePhoto(String idImg) {
        refData.child("users").child(id).child("photoLove").child(idImg).removeValue();
        Toast.makeText(getContext(), "UnLoved!!!", Toast.LENGTH_SHORT).show();
    }
}
