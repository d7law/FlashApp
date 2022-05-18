package com.example.wallpaperunsplash_advancedandroid.fragmentOfHome;

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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.wallpaperunsplash_advancedandroid.R;
import com.example.wallpaperunsplash_advancedandroid.adapter.CollectionAdapter;
import com.example.wallpaperunsplash_advancedandroid.models.CollectionPreFiles;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CategoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CategoryFragment extends Fragment {
    private RecyclerView rv;
    private CollectionAdapter collectionAdapter;
    private ArrayList<CollectionPreFiles> collectionArrayList;
    private int valueLoad = 1;
    String img1, img2, img3, owner, nameCol, totalCol;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CategoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CategoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CategoryFragment newInstance(String param1, String param2) {
        CategoryFragment fragment = new CategoryFragment();
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
        collectionArrayList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_collection, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rv = view.findViewById(R.id.rvCollection);
        collectionAdapter = new CollectionAdapter(getContext(), collectionArrayList);
        rv.setAdapter(collectionAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(layoutManager);
        rv.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        rv.setHasFixedSize(true);

        getCollection(valueLoad);
        valueLoad++;
        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int arrayListsize = collectionArrayList.size();
                if(layoutManager.findLastVisibleItemPosition()==arrayListsize-1){
                    layoutManager.scrollToPosition(arrayListsize-1);
                    loadMore(valueLoad);
                }
            }
        });
    }
    public void loadMore(int val) {
        getCollection(val);
        valueLoad++;
        collectionAdapter.notifyDataSetChanged();
    }
    public void getCollection(int value) {
        String url = "https://api.unsplash.com/collections?page=" + value + "&client_id=KO9uwqG9NK9c0Cei9MrxdsDunlzef96kWfqVH1S5tEs";
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                //collectionArrayList.clear();
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = response.getJSONObject(i);
                        nameCol = jsonObject.getString("title");
                        totalCol = jsonObject.getString("total_photos");

                        JSONObject jsonObjectUser = jsonObject.getJSONObject("user");
                        owner = jsonObjectUser.getString("username");

                        //GET Image Preview
                        JSONArray jsonArrayPrePhotos = jsonObject.getJSONArray("preview_photos");
                        JSONObject jsonPre1 = jsonArrayPrePhotos.getJSONObject(0);
                        img1 = jsonPre1.getJSONObject("urls").getString("small");

                        JSONObject jsonPre2 = jsonArrayPrePhotos.getJSONObject(1);
                        img2 = jsonPre2.getJSONObject("urls").getString("small");

                        JSONObject jsonPre3 = jsonArrayPrePhotos.getJSONObject(2);
                        img3 = jsonPre3.getJSONObject("urls").getString("small");

                        collectionArrayList.add(new CollectionPreFiles(img1, img2, img3, nameCol, owner, totalCol));
                        collectionArrayList.size();
                    }
                    collectionAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Cant get collections", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(arrayRequest);
    }
}