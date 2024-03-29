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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.wallpaperunsplash_advancedandroid.R;
import com.example.wallpaperunsplash_advancedandroid.RawImageActivity;
import com.example.wallpaperunsplash_advancedandroid.adapter.EditorialAdapter;
import com.example.wallpaperunsplash_advancedandroid.models.ImageFiles;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditorialFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditorialFragment extends Fragment {
    //declare
    private RecyclerView rv;
    private EditorialAdapter editorialAdapter;
    private ArrayList<ImageFiles> imageFilesArrayList;

    private boolean isLoading = false;
    private final int THREAT_SHOT = 5;
    private int pageNum =2;
    String img, id, owner;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EditorialFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditorialFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditorialFragment newInstance(String param1, String param2) {
        EditorialFragment fragment = new EditorialFragment();
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
        return inflater.inflate(R.layout.fragment_editorial, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EditorialAdapter.OnItemClickListener onItemClickListener = new EditorialAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ImageFiles imageFiles) {
                Intent intent = new Intent(getActivity(), RawImageActivity.class);
                intent.putExtra("idOfImg",imageFiles.getId_img());
                startActivity(intent);
                Toast.makeText(getContext(), "Mở ảnh đã chọn", Toast.LENGTH_SHORT).show();
            }
        };
        rv = view.findViewById(R.id.rvEditorial);
        editorialAdapter = new EditorialAdapter(getContext(), imageFilesArrayList, onItemClickListener);
        rv.setAdapter(editorialAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(layoutManager);
        rv.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        rv.setHasFixedSize(true);
        //Load data
        getImage(1);

        //Load more data
        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(!rv.canScrollVertically(1)){
                    getImage(pageNum++);
                }
            }
        });
        imageFilesArrayList.size();
    }


    public void getImage(int value) {
        String url = "https://api.unsplash.com/photos/?page=" + pageNum + "&client_id=KO9uwqG9NK9c0Cei9MrxdsDunlzef96kWfqVH1S5tEs";
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                //imageFilesArrayList.clear();
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = response.getJSONObject(i);
                        id = jsonObject.getString("id");
                        JSONObject jsonObjectUrl = jsonObject.getJSONObject("urls");
                        img = jsonObjectUrl.getString("regular");
                        JSONObject jsonObjectUser = jsonObject.getJSONObject("user");
                        owner = jsonObjectUser.getString("username");
                        imageFilesArrayList.add(new ImageFiles(id, img, owner));
                    }
                    editorialAdapter.notifyDataSetChanged();
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
        requestQueue.add(arrayRequest);
    }
}