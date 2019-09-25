package com.mycustom.dashboard.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mycustom.dashboard.R;

public class TeamViewHolder extends RecyclerView.ViewHolder {

    public ImageView teamImage;
    public TextView teamTitle;
    public TextView teamPlayed;
    public TextView teamGameResults;
    public TextView teamGoalDifference;
    public TextView teamPoints;


    public TeamViewHolder(@NonNull View itemView) {
        super(itemView);
        teamImage = itemView.findViewById(R.id.team_image);
        teamTitle = itemView.findViewById(R.id.team_title);
        teamPlayed = itemView.findViewById(R.id.team_played);
        teamGameResults = itemView.findViewById(R.id.team_outcomes);
        teamGoalDifference = itemView.findViewById(R.id.team_goalsDifference);
        teamPoints = itemView.findViewById(R.id.team_points);
    }


}
