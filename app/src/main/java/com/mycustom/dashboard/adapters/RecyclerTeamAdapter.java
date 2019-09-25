package com.mycustom.dashboard.adapters;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mycustom.dashboard.R;
import com.mycustom.dashboard.models.Team;

import java.util.List;

public class RecyclerTeamAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Team> mTeams;


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.team_item_view, viewGroup, false);
        return new TeamViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        Uri uri = Uri.parse("android.resource://com.mycustom.dashboard/drawable/" + mTeams.get(i).getImage_uri());

        RequestOptions options = new RequestOptions()
                .error(R.drawable.ic_launcher_background);


        Glide.with(viewHolder.itemView)
                .setDefaultRequestOptions(options)
                .load(uri)
                .into(((TeamViewHolder) viewHolder).teamImage);

        ((TeamViewHolder) viewHolder).teamTitle.setText(mTeams.get(i).getName());
        ((TeamViewHolder) viewHolder).teamPlayed.setText(String.valueOf(mTeams.get(i).getPlayed()));
        ((TeamViewHolder) viewHolder).teamGameResults.setText(mTeams.get(i).getWon() +
               "/" + mTeams.get(i).getDrawn() +
                "/" + mTeams.get(i).getLost());
        ((TeamViewHolder) viewHolder).teamGoalDifference.setText(mTeams.get(i).getGF() + "-" + mTeams.get(i).getGA());
        ((TeamViewHolder) viewHolder).teamPoints.setText(String.valueOf(mTeams.get(i).getPoints()));


    }

    @Override
    public int getItemCount() {
        if (mTeams != null) {
            return mTeams.size();
        }
        return 0;
    }

    public void setTeams(List<Team> teams){
        if (mTeams != null) {
            mTeams.clear();
        }
        mTeams = teams;
        notifyDataSetChanged();
    }

}





