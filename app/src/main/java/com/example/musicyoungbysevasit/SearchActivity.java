package com.example.musicyoungbysevasit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.musicyoungbysevasit.model.ArtistClass;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.FirebaseDatabase;

public class SearchActivity extends AppCompatActivity {

    public BottomNavigationView bottomNavigationView;
    RecyclerView recyclerView;
    SearchAdapter searchAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        recyclerView = findViewById(R.id.recyclerviewId);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FirebaseRecyclerOptions<ArtistClass> options =
                new FirebaseRecyclerOptions.Builder<ArtistClass>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("artists"),ArtistClass.class)
                        .build();

        searchAdapter = new SearchAdapter(options);
        recyclerView.setAdapter(searchAdapter);



        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.action_search);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemmenu = item.getItemId();
                if (itemmenu == R.id.action_home) {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    overridePendingTransition(0, 0);
                    finish();
                    return true;
                } else if (itemmenu == R.id.action_search) {
                    startActivity(new Intent(getApplicationContext(), SearchActivity.class));
                    overridePendingTransition(0, 0);
                    finish();
                    return true;
                } else if (itemmenu == R.id.action_aboutus) {
                    startActivity(new Intent(getApplicationContext(), AboutUsActivity.class));
                    overridePendingTransition(0, 0);
                    finish();
                    return true;
                }

                return false;
            }
        });
    }

    protected void onStart() {
        super.onStart();
        searchAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        searchAdapter.stopListening();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_item,menu);
        MenuItem item = menu.findItem(R.id.searchId);
        androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String newText) {
                mysearch(newText);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mysearch(newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void mysearch(String str) {

        FirebaseRecyclerOptions<ArtistClass> options =
                new FirebaseRecyclerOptions.Builder<ArtistClass>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("artists").orderByChild("name").startAt(str).endAt(str+"\uf8ff"),ArtistClass.class)
                        .build();

        searchAdapter = new SearchAdapter(options);
        searchAdapter.startListening();
        recyclerView.setAdapter( searchAdapter );
    }

    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }


}