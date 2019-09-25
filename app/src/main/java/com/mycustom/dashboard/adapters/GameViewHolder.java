package com.mycustom.dashboard.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mycustom.dashboard.R;

public class GameViewHolder extends RecyclerView.ViewHolder {


    ImageView hostImage;
    ImageView guestImage;
    TextView hostScore;
    TextView guestScore;
    TextView hostName;
    TextView guestName;



    public GameViewHolder(@NonNull View itemView) {
        super(itemView);
        hostName = itemView.findViewById(R.id.team_host_name);
        guestName = itemView.findViewById(R.id.team_guest_name);
        hostImage = itemView.findViewById(R.id.team_host_image);
        guestImage = itemView.findViewById(R.id.team_guest_image);
        hostScore = itemView.findViewById(R.id.team_host_score);
        guestScore = itemView.findViewById(R.id.team_guest_score);
    }
}
