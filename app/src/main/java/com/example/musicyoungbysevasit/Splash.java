package com.example.musicyoungbysevasit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

public class Splash extends AppCompatActivity {
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Initialize MediaPlayer with the sound file
        mediaPlayer = MediaPlayer.create(this, R.raw.loadingsound);
        // Start playing the sound
        mediaPlayer.start();


        Thread thread = new Thread(){
            public void run(){
                try{
                    sleep(6000);
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    startActivity(new Intent(Splash.this,LoginActivity.class));
                    finish();
                    mediaPlayer.stop();
                    mediaPlayer.release();
                }
            }
        };thread.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Release MediaPlayer resources when the activity is destroyed
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }
}