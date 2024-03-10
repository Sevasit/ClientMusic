package com.example.musicyoungbysevasit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class AboutUsActivity extends AppCompatActivity {
    public BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.action_aboutus);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemmenu = item.getItemId();
                if ( itemmenu == R.id.action_home)
                {
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    overridePendingTransition(0,0);
                    finish();
                    return true;
                }
                else if ( itemmenu == R.id.action_search)
                {
                    startActivity(new Intent(getApplicationContext(),SearchActivity.class));
                    overridePendingTransition(0,0);
                    finish();
                    return true;
                }
                else if ( itemmenu == R.id.action_aboutus)
                {
                    startActivity(new Intent(getApplicationContext(),AboutUsActivity.class));
                    overridePendingTransition(0,0);
                    finish();
                    return true;
                }

                return false;
            }
        });

    }

    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }
}