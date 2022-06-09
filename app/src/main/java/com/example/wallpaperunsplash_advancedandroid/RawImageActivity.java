package com.example.wallpaperunsplash_advancedandroid;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.Manifest;
import android.app.Activity;
import android.app.WallpaperManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class RawImageActivity extends AppCompatActivity {
    public ImageView ivImg;
    public TextView txtFabDown, txtFabLove;
    public ProgressBar progressBar;
    public ExtendedFloatingActionButton fab;
    public FloatingActionButton fabDown, fabLove;
    private Boolean isAllFabsVisible;
    private String uriOfUserPhotos;

    public String urlImg, des, owner;
    public boolean IS_WALLPAPER;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raw_image);

        progressBar = findViewById(R.id.progress_circular);
        ivImg = findViewById(R.id.ivRawImg);
        fab = findViewById(R.id.fabRawImg);
        fabDown = findViewById(R.id.fabDownload);
        txtFabDown = findViewById(R.id.fabDownload_text);
        fabLove = findViewById(R.id.fabLove);
        txtFabLove = findViewById(R.id.fabLove_text);
        try {
            Intent intent = getIntent();
            String id = intent.getStringExtra("idOfImg");
            IS_WALLPAPER = intent.getBooleanExtra("isWallpaper", false);
            uriOfUserPhotos = intent.getStringExtra("uriOfUserPhotos");
            if(id!=null){
                getPhoto(id);
            }
            else{
                getPhotOfUser(uriOfUserPhotos);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        ivImg.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                downLoadPhoto();
                return true;
            }
        });

        //FAB handle
        fabDown.setVisibility(GONE);
        txtFabDown.setVisibility(GONE);
        fabLove.setVisibility(GONE);
        txtFabLove.setVisibility(GONE);
        isAllFabsVisible = false;
        fab.shrink();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isAllFabsVisible) {
                    fabLove.show();
                    fabDown.show();
                    txtFabDown.setVisibility(VISIBLE);
                    txtFabLove.setVisibility(VISIBLE);
                    fab.extend();
                    isAllFabsVisible = true;
                } else {
                    fabDown.hide();
                    fabLove.hide();
                    txtFabLove.setVisibility(GONE);
                    txtFabDown.setVisibility(GONE);
                    fab.shrink();
                    isAllFabsVisible = false;
                }
            }
        });
        fabDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downLoadPhoto();
            }
        });
    }

    private void getPhotOfUser(String uriOfUserPhotos) {
        Picasso.get().load(uriOfUserPhotos).into(ivImg, new Callback() {
            @Override
            public void onSuccess() {
                progressBar.setVisibility(GONE);
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    public void getPhoto(String id) {
        String url = "https://api.unsplash.com/photos/" + id + "?client_id=KO9uwqG9NK9c0Cei9MrxdsDunlzef96kWfqVH1S5tEs";
        RequestQueue requestQueue = Volley.newRequestQueue(RawImageActivity.this);
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    urlImg = response.getJSONObject("urls").getString("regular");
                    Picasso.get().load(urlImg).into(ivImg, new Callback() {
                        @Override
                        public void onSuccess() {
                            progressBar.setVisibility(GONE);
                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });
                    des = response.getString("description");
                    JSONObject jsonObjectUser = response.getJSONObject("user");
                    owner = "" + jsonObjectUser.getString("first_name") + jsonObjectUser.getString("last_name");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        requestQueue.add(objectRequest);
    }

    public void getWallpaper(String url) {
        Picasso.get().load(url).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                WallpaperManager wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
                try {
                    wallpaperManager.setBitmap(bitmap);
                    Toast.makeText(RawImageActivity.this, "Set Okkk", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("d", "cant set wall");
                }
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });
    }

    public void downLoadPhoto() {
        AlertDialog.Builder builder = new AlertDialog.Builder(RawImageActivity.this);
        builder.setMessage("Do you want to download this photo?");
        builder.setTitle("Download");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveToGallery();
                Toast.makeText(RawImageActivity.this, "Downloaded", Toast.LENGTH_SHORT).show();
            }
        });
        if (IS_WALLPAPER) {
            builder.setNeutralButton("Set Wallpaper", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (urlImg != null) {
                        getWallpaper(urlImg);
                    } else
                        Toast.makeText(RawImageActivity.this, "Cant get photo url", Toast.LENGTH_SHORT).show();
                }
            });
        }
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private void saveToGallery() {
        ivImg.setDrawingCacheEnabled(true);
        Bitmap bitmap = ivImg.getDrawingCache();
        verifyStoragePermissions(RawImageActivity.this);
        MediaStore.Images.Media.insertImage(
                RawImageActivity.this.getContentResolver(), bitmap, owner, des);

    }
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
}