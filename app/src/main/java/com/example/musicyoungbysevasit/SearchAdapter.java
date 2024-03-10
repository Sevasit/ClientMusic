package com.example.musicyoungbysevasit;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.musicyoungbysevasit.model.ArtistClass;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class SearchAdapter extends FirebaseRecyclerAdapter<ArtistClass, SearchAdapter.myViewHolder> {


    String nameart;
    DatabaseReference likereference;
    Boolean testclick = false;
    public ImageView like_btn;
    public TextView like_text;


    public SearchAdapter(@NonNull FirebaseRecyclerOptions<ArtistClass> options) {

        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull ArtistClass model) {

        nameart = model.getName();
        holder.name.setText(model.getName());
        holder.course.setText(model.getGenre());
        // holder.email.setText(model.getEmail());

        Glide.with(holder.imageView.getContext())
                .load(model.getUrl())
                .placeholder(R.mipmap.ic_launcher_round)
                .centerCrop()
                .error(R.mipmap.ic_launcher_round)
                .into(holder.imageView);

        likereference = FirebaseDatabase.getInstance().getReference("likes");

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final String userid = firebaseUser.getUid();
        final String postkey = model.getArtistId();

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


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openTrack = new Intent(view.getContext(), SearchTrackActivity.class);
                openTrack.putExtra("artId", model.getArtistId());
                openTrack.putExtra("artName", model.getName());
                view.getContext().startActivity(openTrack);
//                Intent searchTrackAdapter = new Intent(view.getContext(), SearchTrackAdapter.class);
//                searchTrackAdapter.putExtra("artId", model.getArtistId());
//                searchTrackAdapter.putExtra("artName", model.getName());
//                view.getContext().startActivity(searchTrackAdapter);
                Toast.makeText(view.getContext(), model.getName(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);

        return new myViewHolder(view);

    }

    class myViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView name, course, email;

        public ImageView like_btn;
        public TextView like_text;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            like_btn = itemView.findViewById(R.id.like_btn);
            like_text = itemView.findViewById(R.id.like_text);

            imageView = itemView.findViewById(R.id.imgId);
            name = itemView.findViewById(R.id.nameId);
            course = itemView.findViewById(R.id.CourseName);
        }

        public void getlikebuttonstatus(String postkey, String userid) {
            likereference = FirebaseDatabase.getInstance().getReference("likes");
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

    }

}

