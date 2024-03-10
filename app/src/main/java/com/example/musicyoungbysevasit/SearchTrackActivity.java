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
import android.widget.TextView;

import com.example.musicyoungbysevasit.model.TrackClass;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.FirebaseDatabase;

public class SearchTrackActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    SearchTrackAdapter searchAdapter;
    String artistname,artistid;
    TextView    ShowArtName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_track);


        //Receive ArtistName and id send from another activity
        ShowArtName = (TextView) findViewById(R.id.txtShowArtName);
        Intent intent = getIntent();
        artistname = intent.getStringExtra("artName");
        artistid = intent.getStringExtra("artId");

        ShowArtName.setText("Playlist of :  " + artistname);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        recyclerView = findViewById(R.id.recyclerviewId);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//ชื่อตารางกับชื่อฟิลด์ต้งตรงให้ตรงกับ firebase
        FirebaseRecyclerOptions<TrackClass> options =
                new FirebaseRecyclerOptions.Builder<TrackClass>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("tracks").child(artistid),TrackClass.class)
                        .build();

        searchAdapter = new SearchTrackAdapter(options,artistname,artistid);
        recyclerView.setAdapter(searchAdapter);

    }
    //3
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
        FirebaseRecyclerOptions<TrackClass> options =
                new FirebaseRecyclerOptions.Builder<TrackClass>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("tracks").child(artistid).orderByChild("name").startAt(str).endAt(str+"\uf8ff"),TrackClass.class)
                        .build();

        searchAdapter = new SearchTrackAdapter(options,artistname,artistid);
        searchAdapter.startListening();
        recyclerView.setAdapter(searchAdapter);
    }
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), SearchActivity.class));
        finish();
    }
}
