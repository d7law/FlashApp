package com.example.wallpaperunsplash_advancedandroid;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.wallpaperunsplash_advancedandroid.adapter.EditorialAdapter;
import com.example.wallpaperunsplash_advancedandroid.adapter.GridAdapter;
import com.example.wallpaperunsplash_advancedandroid.models.ImageFiles;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class CategoryDetailActivity extends AppCompatActivity {
    TextView txtTitle;
    RecyclerView rv;
    GridAdapter gridAdapter;
    ArrayList<ImageFiles> imgArrayList;
    String img, id, owner, idCollection;
    EditorialAdapter.OnItemClickListener listener;
    public int pageNum=2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_detail);
        txtTitle = findViewById(R.id.txtTitleCollection);
        imgArrayList = new ArrayList<>();
        rv = findViewById(R.id.rvPhotoCollectionDetail);
        listener = new EditorialAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ImageFiles imageFiles) {
                Intent intent = new Intent(CategoryDetailActivity.this, RawImageActivity.class);
                intent.putExtra("idOfImg",imageFiles.getId_img());
                startActivity(intent);
            }
        };
        //get ID of Collection
        gridAdapter = new GridAdapter(this, imgArrayList, listener);

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(gridAdapter);
        rv.setHasFixedSize(true);
        try{
            Intent intent = getIntent();
            idCollection = intent.getStringExtra("idOfCollection");
            txtTitle.setText(intent.getStringExtra("nameOfCollection"));
            getCollection(idCollection, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(!rv.canScrollVertically(1)){
                    getCollection(idCollection, pageNum++);
                }
            }
        });
    }
    public void getCollection(String idCollection, int pageNum){
        String url = "https://api.unsplash.com/collections/"+idCollection+"/photos?page="+pageNum+"&client_id=KO9uwqG9NK9c0Cei9MrxdsDunlzef96kWfqVH1S5tEs";
        RequestQueue requestQueue = Volley.newRequestQueue(CategoryDetailActivity.this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try{
                    for(int i=0;i<response.length(); i++){
                        JSONObject jsonObject = response.getJSONObject(i);
                        id = jsonObject.getString("id");
                        owner = jsonObject.getJSONObject("user").getString("username");
                        img = jsonObject.getJSONObject("urls").getString("regular");

                        imgArrayList.add(new ImageFiles(id, img, owner));
                    }
                    gridAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CategoryDetailActivity.this, "Can't get Photos", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonArrayRequest);
    }
}