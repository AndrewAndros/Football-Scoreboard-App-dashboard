package com.mycustom.dashboard.fragments;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mycustom.dashboard.DashboardActivity;
import com.mycustom.dashboard.R;

public class MenuFragment extends Fragment {

    TextView playBn;
    TextView tableBn;
    TextView resultsBn;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.menu_fragment, container, false);
        playBn = view.findViewById(R.id.play_game_bn);
        tableBn = view.findViewById(R.id.teams_table__bn);
        resultsBn = view.findViewById(R.id.games_table__bn);
        return view;
    }



    @Override
    public void onStart() {
        super.onStart();
        playBn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((DashboardActivity) getActivity()).setViewPager(1);
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
        });

        tableBn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((DashboardActivity) getActivity()).setViewPager(2);
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);
            }
        });

        resultsBn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((DashboardActivity) getActivity()).setViewPager(3);
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);
            }
        });
    }
}
