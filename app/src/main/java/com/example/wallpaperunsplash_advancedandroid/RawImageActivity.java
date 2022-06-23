package com.example.wallpaperunsplash_advancedandroid;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.Manifest;
import android.app.Activity;
import android.app.WallpaperManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
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
import com.example.wallpaperunsplash_advancedandroid.adapter.roomDatabase.PathDownload;
import com.example.wallpaperunsplash_advancedandroid.adapter.roomDatabase.PathRepository;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

public class RawImageActivity extends AppCompatActivity {
    public ImageView ivImg;
    public ProgressBar progressBar;
    public ExtendedFloatingActionButton fab;
    public FloatingActionButton fabDown, fabLove, fabWallpaper;
    public TextView txtFabDown, txtFabLove, txtFabWallpaper;
    private Boolean isAllFabsVisible;
    private String idOfImg, uriOfUserPhotos, pathOfDownloadPhoto, urlImg;
    public String des, owner;
    public boolean IS_WALLPAPER, IS_FAVORITE;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private PathRepository pathRepository;
    private FirebaseDatabase fDb;
    private FirebaseAuth fAuth;
    private FirebaseUser fUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raw_image);

        fDb = FirebaseDatabase.getInstance();
        fAuth = FirebaseAuth.getInstance();
        fUser = fAuth.getCurrentUser();
        pathRepository = new PathRepository(getApplication());
        progressBar = findViewById(R.id.progress_circular);
        ivImg = findViewById(R.id.ivRawImg);
        fab = findViewById(R.id.fabRawImg);
        fabDown = findViewById(R.id.fabDownload);
        txtFabDown = findViewById(R.id.fabDownload_text);
        fabLove = findViewById(R.id.fabLove);
        txtFabLove = findViewById(R.id.fabLove_text);
        fabWallpaper = findViewById(R.id.fabWallpaper);
        txtFabWallpaper = findViewById(R.id.fabWallpaper_text);

        try {
            Intent intent = getIntent();
            idOfImg = intent.getStringExtra("idOfImg");
            IS_FAVORITE = intent.getBooleanExtra("IsFavorite", false);
            IS_WALLPAPER = intent.getBooleanExtra("isWallpaper", false);
            uriOfUserPhotos = intent.getStringExtra("uriOfUserPhotos");
            pathOfDownloadPhoto = intent.getStringExtra("pathOfDownloadPhotos");
            if (idOfImg != null) {
                getPhoto(idOfImg);
            } else if (uriOfUserPhotos != null) {
                getPhotoOfUser(uriOfUserPhotos);
            } else {
                getPhotoDownloaded(pathOfDownloadPhoto);
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
        fabWallpaper.setVisibility(GONE);
        txtFabWallpaper.setVisibility(GONE);
        isAllFabsVisible = false;
        fab.shrink();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isAllFabsVisible) {
                    if (IS_FAVORITE != true) {
                        fabLove.show();
                        txtFabLove.setVisibility(VISIBLE);
                    } else{
                        fabLove.hide();
                        txtFabLove.setVisibility(GONE);
                    }
                    fabDown.show();
                    fabWallpaper.show();
                    txtFabDown.setVisibility(VISIBLE);
                    txtFabWallpaper.setVisibility(VISIBLE);
                    fab.extend();
                    isAllFabsVisible = true;
                } else {
                    fabDown.hide();
                    fabLove.hide();
                    fabWallpaper.hide();
                    txtFabLove.setVisibility(GONE);
                    txtFabDown.setVisibility(GONE);
                    txtFabWallpaper.setVisibility(GONE);
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
        fabWallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWallpaper(urlImg);
                Toast.makeText(RawImageActivity.this, "Set Wallpaper done", Toast.LENGTH_SHORT).show();
            }
        });
        fabLove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fUser != null) {
                    lovePhoto(fUser.getUid());
                } else
                    Toast.makeText(RawImageActivity.this, "You need to login to add this photo to Favorite", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getPhotoOfUser(String uriOfUserPhotos) {
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

    private void getPhotoDownloaded(String paths) {
        File file = new File(paths);
        Picasso.get().load(file).into(ivImg, new Callback() {
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
        ivImg.setDrawingCacheEnabled(true);
        Bitmap bitmap = ivImg.getDrawingCache();
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
        try {
            wallpaperManager.setBitmap(bitmap);
            Toast.makeText(RawImageActivity.this, "Set Okkk", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("d", "cant set wall");
        }
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

    private void lovePhoto(String uId) {
        DatabaseReference refDb = fDb.getReference().child("users").child(uId).child("photoLove");
        refDb.child(idOfImg).setValue(urlImg);
        Toast.makeText(this, "Added to Favorite", Toast.LENGTH_SHORT).show();
    }

    private void saveToGallery() {
        ivImg.setDrawingCacheEnabled(true);
        Bitmap bitmap = ivImg.getDrawingCache();
        verifyStoragePermissions(RawImageActivity.this);
        MediaStore.Images.Media.insertImage(
                RawImageActivity.this.getContentResolver(), bitmap, owner, des);

        findTheLastPhoto();
    }

    private void findTheLastPhoto() {
        String[] projection = new String[]{
                MediaStore.Images.ImageColumns._ID,
                MediaStore.Images.ImageColumns.DATA,
                MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
                MediaStore.Images.ImageColumns.DATE_TAKEN,
                MediaStore.Images.ImageColumns.MIME_TYPE
        };
        final Cursor cursor = this.getContentResolver()
                .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null,
                        null, MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC");

        if (cursor.moveToLast()) {
            String imageLocation = cursor.getString(1);
            pathRepository.insert(new PathDownload(imageLocation));
        }
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