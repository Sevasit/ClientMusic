package com.example.musicyoungbysevasit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.musicyoungbysevasit.model.Commentmodel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;

public class CommentTrackActivity extends AppCompatActivity {
    // ประกาศตัวแปร java ที่ใต้ public class
//ส่วนของการแสดงความคิดเห็นและรับค่ามาแสดงผล
    TextView txtNameArt, txtTrackName;
    ImageView imgShowArtist;
    EditText comment_text;
    RecyclerView comment_recview;
    String trackId, trackName, artId, artName, txtUrl;
    SearchAdapter searchAdapter;
    DatabaseReference ref, userref, commentref;
    String artTest;
    RecyclerView recview;
    Button commentsubmit;

    MediaPlayer mediaPlayer;
    Button playtrack, pausetrack;

    String artistnameForTrack,artistidForTrack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_track);
// เขียน code ใน oncreate
//กำหนดค่าให้กับตัวแปรเครื่องมือที่ประกาศไว้
        txtNameArt = findViewById(R.id.txtNameArt);
        txtTrackName = findViewById(R.id.txtTrackName);
        imgShowArtist = findViewById(R.id.imgShowArtist);
        comment_text = findViewById(R.id.comment_text);
        comment_recview = findViewById(R.id.comment_recview);
        commentsubmit = (Button) findViewById(R.id.comment_submit);
        recview = (RecyclerView) findViewById(R.id.comment_recview);
        playtrack = findViewById(R.id.btnPlay);
        pausetrack = findViewById(R.id.btnPause);

//รับค่ารหัสศิลปิน รหัสเพลง ชื่อเพลง ที่ส่งมาและสั่งแสดงผลชื่อเพลง
        Intent intent = getIntent();
        trackId = intent.getStringExtra("trackId");
        trackName = intent.getStringExtra("trackName");
        txtUrl = intent.getStringExtra("trackUrl");
        artId = intent.getStringExtra("artId");
        artistnameForTrack = intent.getStringExtra("artistnameForTrack");
        artistidForTrack = intent.getStringExtra("artistidForTrack");
        txtTrackName.setText("เพลง : " + trackName);

//สั่งให้ปุ่ม play เล่นเพลง
        playtrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setDataSource(txtUrl);
                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mp.start();
                        }
                    });
                    mediaPlayer.prepareAsync();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

//สั่งให้ปุ่ม pasue หยุดเล่่นเพลง
        pausetrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.pause();
            }
        });

//ส่วนของการแสดงความคิดเห็น
        userref = FirebaseDatabase.getInstance().getReference().child("userprofile");
        commentref = FirebaseDatabase.getInstance().getReference().child("tracks").child(artId).child(trackId).child("comments");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String userId = user.getUid();
        recview.setLayoutManager(new LinearLayoutManager(this));

//เอารหัสศิลปินจากเพลงไปดึงชื่อกับภาพศิลปินมาแสดงผล
        ref = FirebaseDatabase.getInstance().getReference().child("artists");
        ref.child(artistidForTrack).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    artName = snapshot.child("name").getValue().toString();
                    txtNameArt.setText("ศิลปิน : " + artName);
                    Glide.with(getApplicationContext()).load(snapshot.child("url").getValue().toString())
                            .placeholder(R.mipmap.ic_launcher_round)
                            .centerCrop()
                            .error(R.mipmap.ic_launcher_round).into(imgShowArtist);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//เมื่อกดปุ่มเพิ่มการแสดงความคิดเห็น
        commentsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userref.child(userId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            String username = snapshot.child("uname").getValue().toString();
                            String uimage = snapshot.child("uimage").getValue().toString();
                            processcomment(username, uimage);
                        } else {
                            Toast.makeText(getApplicationContext(), "please update your profile", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            //สร้างเมธอดใหม่ ขือ processcomment สำหรับบันทึกการแสดงความคิดเห็นลงฐานข้อมูล
            private void processcomment(String username, String uimage) {
                String commentpost = comment_text.getText().toString();
                String randompostkey = userId + "" + new Random().nextInt(1000);
                Calendar datevalue = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
                String cdate = dateFormat.format(datevalue.getTime());
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                String ctime = timeFormat.format(datevalue.getTime());
                HashMap cmnt = new HashMap();
                cmnt.put("uid", userId);
                cmnt.put("username", username);
                cmnt.put("userimage", uimage);
                cmnt.put("usermsg", commentpost);
                cmnt.put("date", cdate);
                cmnt.put("time", ctime);
                commentref.child(randompostkey).updateChildren(cmnt)
                        .addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Comment Added", Toast.LENGTH_LONG).show();
                                    comment_text.setText("");
                                } else
                                    Toast.makeText(getApplicationContext(), task.toString(), Toast.LENGTH_LONG).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Toast.makeText(getApplicationContext(), "please update your profile", Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });
    }

//  สร้างเมธอดใหม่ ขือ onStart สำหรับดึงรายการแสดงความคิดเห็นทั้งหมดของหนังสือแต่ละเพลงมาแสดง และ Query ดึงข้อมูลภาพศิลปินและชื่อศิลปินมาแสดง

    protected void onStart() {
        super.onStart();
        //ดึงความคิดเห็นขึ้นมาแสดงผล
        FirebaseRecyclerOptions<Commentmodel> options =
                new FirebaseRecyclerOptions.Builder<Commentmodel>()
                        .setQuery(commentref, Commentmodel.class)
                        .build();
        FirebaseRecyclerAdapter<Commentmodel, Commentviewholder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Commentmodel, Commentviewholder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull Commentviewholder holder, int position, @NonNull Commentmodel model) {
                holder.cuname.setText(model.getUsername());
                holder.cumessage.setText(model.getUsermsg());
                holder.cudt.setText("Date :" + model.getDate() + " Time :" + model.getTime());
                Glide.with(holder.cuimage.getContext()).load(model.getUserimage()).into(holder.cuimage);
            }

            @NonNull
            @Override
            public Commentviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_single_row, parent, false);
                return new Commentviewholder(view);
            }
        };

        firebaseRecyclerAdapter.startListening();
        recview.setAdapter(firebaseRecyclerAdapter);

    }

    // สร้าง Method onBackPressed เมื่อกดปุ่มให้กลับหน้าค้นศิลปิน
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), SearchActivity.class));
        finish();
    }
}//ปิด java class
