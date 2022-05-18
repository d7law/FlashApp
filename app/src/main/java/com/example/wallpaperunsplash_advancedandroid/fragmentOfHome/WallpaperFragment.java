package com.example.wallpaperunsplash_advancedandroid.fragmentOfHome;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.wallpaperunsplash_advancedandroid.R;
import com.example.wallpaperunsplash_advancedandroid.RawImageActivity;
import com.example.wallpaperunsplash_advancedandroid.adapter.EditorialAdapter;
import com.example.wallpaperunsplash_advancedandroid.adapter.WallpaperAdapter;
import com.example.wallpaperunsplash_advancedandroid.models.ImageFiles;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WallpaperFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WallpaperFragment extends Fragment {
    private RecyclerView rv;
    private WallpaperAdapter wallpaperAdapter;
    private ArrayList<ImageFiles> imageFilesArrayList;

    private int valueLoad = 1;
    String img, id, owner;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public WallpaperFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WallpaperFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WallpaperFragment newInstance(String param1, String param2) {
        WallpaperFragment fragment = new WallpaperFragment();
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
        imageFilesArrayList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wallpaper, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EditorialAdapter.OnItemClickListener onItemClickListener = new EditorialAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ImageFiles imageFiles) {
                Intent intent = new Intent(getActivity(), RawImageActivity.class);
                intent.putExtra("idOfImg", imageFiles.getId_img());
                intent.putExtra("isWallpaper", true);
                startActivity(intent);
                Toast.makeText(getContext(), "Mở Wallpaper", Toast.LENGTH_SHORT).show();
            }
        };
        rv = view.findViewById(R.id.rvWallpaper);
        wallpaperAdapter = new WallpaperAdapter(getContext(), imageFilesArrayList, onItemClickListener);
        rv.setAdapter(wallpaperAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(layoutManager);
        rv.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        rv.setHasFixedSize(true);

        getImage(valueLoad);
        valueLoad++;

        //loadmoredata
        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int arrSize = imageFilesArrayList.size();
                if (layoutManager.findLastVisibleItemPosition() == arrSize - 1) {
                    layoutManager.scrollToPosition(layoutManager.findFirstCompletelyVisibleItemPosition());
                    loadMore(valueLoad);
                }
            }
        });
    }
    public void loadMore(int val){
        getImage(val);
        valueLoad++;
        wallpaperAdapter.notifyDataSetChanged();
    }
    public void getImage(int value){
        String url = "https://api.unsplash.com/search/photos?page="+value+"&orientation=portrait&query=phonewallpaper&client_id=KO9uwqG9NK9c0Cei9MrxdsDunlzef96kWfqVH1S5tEs";
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //imageFilesArrayList.clear();
                try {
                    JSONArray jsonArray = response.getJSONArray("results");

                    //GET id
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        id = jsonObject.getString("id");
                        img = jsonObject.getJSONObject("urls").getString("regular");
                        owner = jsonObject.getJSONObject("user").getString("username");

                        imageFilesArrayList.add(new ImageFiles(id, img, owner));
                    }
                    wallpaperAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Cant get image", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(objectRequest);
    }

}