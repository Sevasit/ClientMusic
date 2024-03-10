package com.example.musicyoungbysevasit;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Commentviewholder extends RecyclerView.ViewHolder {
    ImageView cuimage;
    TextView cuname, cumessage, cudt;

    public Commentviewholder(@NonNull View itemView) {
        super(itemView);
        cuimage = itemView.findViewById(R.id.cuimage);
        cuname = itemView.findViewById(R.id.cuname);
        cumessage = itemView.findViewById(R.id.cumessage);
        cudt = itemView.findViewById(R.id.cudt);
    }
}

