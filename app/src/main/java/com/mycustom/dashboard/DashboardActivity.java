package com.mycustom.dashboard;

import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.mycustom.dashboard.fragments.CustomViewPager;
import com.mycustom.dashboard.fragments.DashboardFragment;
import com.mycustom.dashboard.fragments.GamesFragment;
import com.mycustom.dashboard.fragments.MenuFragment;
import com.mycustom.dashboard.fragments.SectionsStatePagerAdapter;
import com.mycustom.dashboard.fragments.TableFragment;
import com.mycustom.dashboard.viewmodel.ScoreViewModel;

public class DashboardActivity extends AppCompatActivity  {


    ScoreViewModel mViewModel;

    private SectionsStatePagerAdapter mSectionsStatePagerAdapter;
    private CustomViewPager mViewPager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.main_activity);

        // Get a ScoreViewModel
        mViewModel = ViewModelProviders.of(this).get(ScoreViewModel.class);


        // Load all teams from database
        if(mViewModel.getTeamsLoadingStatus().getValue() == null) {
            mViewModel.startTeamsLoading();
            mViewModel.loadAllPlayedGames();
        }


        mSectionsStatePagerAdapter = new SectionsStatePagerAdapter(getSupportFragmentManager());

        mViewPager = (CustomViewPager) findViewById(R.id.containter);

        //setup the pager
        setupViewPager(mViewPager);
        mViewPager.setPagingEnabled(false);

    }

    private void setupViewPager(ViewPager viewPager){
        SectionsStatePagerAdapter adapter = new SectionsStatePagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new MenuFragment(), "Menu Fragment");
        adapter.addFragment(new DashboardFragment(), "Dashboard Fragment");
        adapter.addFragment(new TableFragment(), "TeamsTable Fragment");
        adapter.addFragment(new GamesFragment(), "GamesTable Fragment");
        viewPager.setAdapter(adapter);
    }

    public void setViewPager(int fragmentNumber){
        mViewPager.setCurrentItem(fragmentNumber);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mViewModel.clearDisposables();
    }

    @Override
    public void onBackPressed() {
        if(mViewModel.gameOnRun) {
            userWantToExit();
        } else if(mViewModel.exitCommanded) {
            setViewPager(0);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            mViewModel.exitCommanded = false;
        } else if (mViewPager.getCurrentItem() != 0) {
            setViewPager(0);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            super.onBackPressed();
        }

    }

    private void userWantToExit() {
        AlertDialog alertDialog = new AlertDialog.Builder(DashboardActivity.this).create();
        alertDialog.setTitle(getString(R.string.exit_title));
        alertDialog.setMessage(getString(R.string.exit_explanation));
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.exit_confirmation),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        mViewModel.exitCommanded = true;
                        mViewModel.gameOnRun = false;
                        mViewModel.scoreTeamHost = 0;
                        mViewModel.scoreTeamRival = 0;
                        mViewModel.hostTeamIndex = 0;
                        mViewModel.rivalTeamIndex = 0;
                        if (mViewModel.timer != null) {
                            mViewModel.timer.cancel();
                        }
                        onBackPressed();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.exit_cancel),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }
}
