package com.example.wallpaperunsplash_advancedandroid;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.wallpaperunsplash_advancedandroid.fragmentMain.AccountFragment;
import com.example.wallpaperunsplash_advancedandroid.fragmentMain.DownloadedFragment;
import com.example.wallpaperunsplash_advancedandroid.fragmentMain.HomeFragment;
import com.example.wallpaperunsplash_advancedandroid.fragmentMain.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    public Uri imgUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        display(R.id.mnHome);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                display(item.getItemId());
                return true;
            }
        });
    }

    @Override
    public void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }
    void display(int id) {
        Fragment fragment = null;
        switch (id) {
            case R.id.mnHome:
                fragment = new HomeFragment();
                break;
            case R.id.mnSearch:
                fragment = new SearchFragment();
                break;
            case R.id.mnUpgrade:
                fragment = new DownloadedFragment();
                break;
            case R.id.mnAcc:
                fragment = new AccountFragment();
                break;
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, fragment);
        ft.commit();

    }
}