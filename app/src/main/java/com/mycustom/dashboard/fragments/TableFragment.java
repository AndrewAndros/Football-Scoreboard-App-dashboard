package com.mycustom.dashboard.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mycustom.dashboard.R;
import com.mycustom.dashboard.adapters.RecyclerTeamAdapter;
import com.mycustom.dashboard.models.Team;
import com.mycustom.dashboard.utils.VerticalSpacingItemDecorator;
import com.mycustom.dashboard.viewmodel.ScoreViewModel;

import java.util.List;

public class TableFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerTeamAdapter mAdapter;
    ScoreViewModel mViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_table, container, false);
        mRecyclerView = view.findViewById(R.id.teams_recycler_view);
        initRecyclerView();
        subscribeObservers();
        return view;
    }

    private void subscribeObservers() {
        mViewModel.getTeams().removeObservers(getViewLifecycleOwner());
        mViewModel.getTeams().observe(getViewLifecycleOwner(), new Observer<List<Team>>() {
            @Override
            public void onChanged(@Nullable List<Team> teams) {
                mAdapter.setTeams(mViewModel.teams.getValue());
            }
        });
    }



    private void initRecyclerView() {
        mAdapter = new RecyclerTeamAdapter();
        VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(20);
        mRecyclerView.addItemDecoration(itemDecorator);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //        Get a ScoreViewModel
        mViewModel = ViewModelProviders.of(getActivity()).get(ScoreViewModel.class);
    }
}
