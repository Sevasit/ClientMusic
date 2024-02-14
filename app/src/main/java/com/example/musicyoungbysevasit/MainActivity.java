package com.example.musicyoungbysevasit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    public FirebaseAuth registerAuth;
    public TextView showmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registerAuth = FirebaseAuth.getInstance();
        showmail = findViewById(R.id.txtEmail);
        //ผ้ใช้ที่กาลงั Login
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //เช็คว่ามีผ ้ใช้ก าลัง login อย ่หรือไม่
        if (user != null) {
            showmail.setText(user.getEmail());
        } else {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        }
    }

    public void Logout(View view) { //คาสั่งออกจากระบบ
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
    }
}