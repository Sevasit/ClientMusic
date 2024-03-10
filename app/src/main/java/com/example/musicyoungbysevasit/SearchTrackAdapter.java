package com.example.musicyoungbysevasit;

import static android.content.Intent.getIntent;


import android.content.Intent;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.musicyoungbysevasit.model.TrackClass;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import java.io.IOException;


public class SearchTrackAdapter extends FirebaseRecyclerAdapter<TrackClass, SearchTrackAdapter.myViewHolder> {
    //1.ประกาศตัวแปรเพื่ออ้างถึงปุ่ม ไลค์ และ firebase เพื่อเก็บจำนวนการการกดไลค์
    String postkey;
    DatabaseReference likereference;
    MediaPlayer mediaPlayer;
    String nametrack;
    String urltrack;
    Boolean testclick = false;
    String artistid;
    String artistnameForTrack,artistidForTrack;


    public SearchTrackAdapter(@NonNull FirebaseRecyclerOptions<TrackClass> options, String artistnameForTrack, String artistidForTrack) {
        super(options);
        this.artistnameForTrack = artistnameForTrack;
        this.artistidForTrack = artistidForTrack;
    }


    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull TrackClass model) {

        urltrack = model.getUrl();
        nametrack = model.getName();


        holder.name.setText(model.getName());



//5. เพิ่มส่วนการกดคลิกlike
        artistid = model.getId();
        likereference = FirebaseDatabase.getInstance().getReference("likestrack").child(artistid);


        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final String userid = firebaseUser.getUid();
        final String postkey = model.getId();


        holder.getlikebuttonstatus(postkey, userid);


        holder.like_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                testclick = true;


                likereference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (testclick == true) {
                            if (snapshot.child(postkey).hasChild(userid)) {
                                likereference.child(postkey).child(userid).removeValue();
                                testclick = false;
                            } else {
                                likereference.child(postkey).child(userid).setValue(true);
                                testclick = false;
                            }


                        }
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {


                    }
                });


            }
        });
        holder.Play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //   TrackClass track = tracks.get(position);
                mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setDataSource(model.getUrl());
                    mediaPlayer.setOnPreparedListener(new
                                                              MediaPlayer.OnPreparedListener() {
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


        holder.Pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                mediaPlayer.pause();
            }
        });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openTrack= new Intent (view.getContext(), CommentTrackActivity.class);
                openTrack.putExtra("trackId",postkey);
                openTrack.putExtra("trackName",model.getName());
                openTrack.putExtra("trackUrl",model.getUrl());
                openTrack.putExtra("artId",model.getId() );
                openTrack.putExtra("artistnameForTrack",artistnameForTrack);
                openTrack.putExtra("artistidForTrack",artistidForTrack);
                view.getContext().startActivity(openTrack);
            }
        });
    }


    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.searchtrack_item, parent, false);


        return new myViewHolder(view);
    }


    class myViewHolder extends RecyclerView.ViewHolder {


        //2. ประกาศตัวแปรเพื่ออ้างถึงเครื่องมือรูปหัวใจกับtextview แสดงจำนวนไลค์ที่วาดเพิ่ม


        public ImageView like_btn;
        public TextView like_text;
        TextView name, course, email;
        Button Play, Pause;


        public myViewHolder(@NonNull View itemView) {
            super(itemView);


            name = itemView.findViewById(R.id.nameId);
            Play = itemView.findViewById(R.id.playbutton);
            Pause = itemView.findViewById(R.id.pausebutton);
// 3.อ้างถึงปุ่มไลค์กับข้อความแสดงจำนวนไลค์
            like_btn = itemView.findViewById(R.id.like_btntrack);
            like_text = itemView.findViewById(R.id.like_texttrack);


        }


        //4. สร้างเมธอด getlikebuttonstatus
        public void getlikebuttonstatus(String postkey, String userid) {

            likereference = FirebaseDatabase.getInstance().getReference("likestrack").child(artistid);
            likereference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.child(postkey).hasChild(userid)) {


                        int likecount = (int) snapshot.child(postkey).getChildrenCount();
                        like_text.setText(likecount + " likes");
                        like_btn.setImageResource(R.drawable.baseline_favorite_24);
                    } else {
                        int likecount = (int) snapshot.child(postkey).getChildrenCount();
                        like_text.setText(likecount + " likes");
                        like_btn.setImageResource(R.drawable.baseline_favorite_border_24);
                    }
                }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {


                }
            });
        }


    }//


}//ปิด java class

