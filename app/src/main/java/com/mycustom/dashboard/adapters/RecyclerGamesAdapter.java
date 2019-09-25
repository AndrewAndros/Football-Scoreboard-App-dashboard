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
import com.mycustom.dashboard.models.Game;
import com.mycustom.dashboard.models.Team;

import java.util.HashMap;
import java.util.List;

public class RecyclerGamesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

     private List<Game> mGames;
     private HashMap<String, Team> mTeams;




    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.game_item_view, viewGroup, false);
        return new GameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        RequestOptions options = new RequestOptions()
                .error(R.drawable.ic_launcher_background);


        Uri hostUri = Uri.parse("android.resource://com.mycustom.dashboard/drawable/" + mTeams.get(mGames.get(i).getHostTeam()).getImage_uri());
        Glide.with(viewHolder.itemView)
                .setDefaultRequestOptions(options)
                .load(hostUri)
                .into(((GameViewHolder) viewHolder).hostImage);

        Uri guestUri = Uri.parse("android.resource://com.mycustom.dashboard/drawable/" + mTeams.get(mGames.get(i).getRivalTeam()).getImage_uri());
        Glide.with(viewHolder.itemView)
                .setDefaultRequestOptions(options)
                .load(guestUri)
                .into(((GameViewHolder) viewHolder).guestImage);





        ((GameViewHolder) viewHolder).hostName.setText(mGames.get(i).getHostTeam());
        ((GameViewHolder) viewHolder).guestName.setText(mGames.get(i).getRivalTeam());
        ((GameViewHolder) viewHolder).hostScore.setText("" + mGames.get(i).getHostScore());
        ((GameViewHolder) viewHolder).guestScore.setText("" + mGames.get(i).getRivalScore());


    }

    @Override
    public int getItemCount() {
        if (mGames != null) {
            return mGames.size();
        }
        return 0;
    }


    public void setGames(List<Game> games){
        if (mGames != null) {
            mGames.clear();
        }
        mGames = games;
        notifyDataSetChanged();
    }

    public void setTeams(HashMap<String, Team> teams){
        if (mTeams != null) {
            mTeams.clear();
        }
        mTeams = teams;
    }
}
