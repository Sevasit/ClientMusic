package com.example.musicyoungbysevasit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    public BottomNavigationView bottomNavigationView;
    DrawerLayout layDL;
    NavigationView vNV;
    Toolbar toolbar;

    private WebView webView;

    public FirebaseAuth registerAuth;
    public TextView showmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = findViewById(R.id.webView);


        registerAuth = FirebaseAuth.getInstance();
//        showmail = findViewById(R.id.txtEmail);
        //ผ้ใช้ที่กาลงั Login
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //เช็คว่ามีผ ้ใช้ก าลัง login อย ่หรือไม่
        if (user != null) {webView.getSettings().setJavaScriptEnabled(true);
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    // WebView has finished loading, now set its visibility
                    webView.setVisibility(View.VISIBLE);
                }
            });
            webView.loadUrl("https://www.billboard.com/charts/hot-100/");
        } else {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        }

        layDL = findViewById(R.id.layDL);
        vNV = findViewById(R.id.vNV);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, layDL, toolbar, R.string.open_drawer, R.string.close_drawer);

        layDL.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            vNV.setCheckedItem(R.id.row_home);
        }
        NavClick();

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.action_home);
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

    private void NavClick() {
        vNV.setNavigationItemSelectedListener(item -> {
            Fragment frag = null;
            int itemmenu = item.getItemId();
            if (itemmenu == R.id.row_home) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            } else if (itemmenu == R.id.row_profile) {
                startActivity(new Intent(getApplicationContext(), UpdateProfile.class));
                finish();
            } else if (itemmenu == R.id.row_logout) {
                FirebaseAuth.getInstance().signOut();
                finish();
            }

            layDL.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    @Override
    public void onBackPressed() {
        Fragment currFrag = getSupportFragmentManager().findFragmentById(R.id.layFL);
        if (layDL.isDrawerOpen(GravityCompat.START)) {
            layDL.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}