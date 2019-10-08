package com.mycustom.dashboard.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mycustom.dashboard.R;
import com.mycustom.dashboard.models.Game;
import com.mycustom.dashboard.models.Team;
import com.mycustom.dashboard.room_db.AppDatabase;
import com.mycustom.dashboard.room_db.GamesDao;
import com.mycustom.dashboard.room_db.TeamsDao;
import com.mycustom.dashboard.utils.AppExecutors;
import com.mycustom.dashboard.utils.Constants;
import com.mycustom.dashboard.utils.CountDownTimer2;
import com.mycustom.dashboard.viewmodel.ScoreViewModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.mycustom.dashboard.utils.Constants.CLICK;
import static com.mycustom.dashboard.utils.Constants.GOAL;
import static com.mycustom.dashboard.utils.Constants.KICK;
import static com.mycustom.dashboard.utils.Constants.WHISTLE;
import static com.mycustom.dashboard.utils.Constants.WRONG;


public class DashboardFragment extends Fragment implements View.OnClickListener {


    TeamsDao teamsDao = AppDatabase.getInstance(getActivity()).teamsDao();
    GamesDao gamesDao = AppDatabase.getInstance(getActivity()).gamesDao();
    private AppExecutors appExecutors = AppExecutors.getInstance();
    ScoreViewModel mViewModel;
    TextView scoreA;
    TextView scoreB;
    public TextView timeView;
    TextView timeForGameView;

    ImageView hostEmblem;
    ImageView rivalEmblem;

    TextView hostName;
    TextView rivalName;


    Animation anim = null;



    private AudioManager mAudioManager;
    private MediaPlayer mMediaPlayer;
    private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            releaseMediaPlayer();
        }
    };

    private AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||
                    focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                mMediaPlayer.pause();
            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                mMediaPlayer.start();
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                releaseMediaPlayer();
            }
        }
    };




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view  = inflater.inflate(R.layout.fragment_dashboard, container, false);

        scoreA = view.findViewById(R.id.score_teamA);
        scoreB = view.findViewById(R.id.score_teamB);

        hostEmblem = view.findViewById(R.id.host_team_emblem);
        rivalEmblem = view.findViewById(R.id.rival_team_emblem);

        hostName = view.findViewById(R.id.host_team_name);
        rivalName = view.findViewById(R.id.rival_team_name);

        timeView = view.findViewById(R.id.timeLeft);

        timeForGameView = view.findViewById(R.id.time_for_game);

        if (mViewModel.timeForGameSelection == 0) {
            timeForGameView.setText("20 сек.");
        } else {
            timeForGameView.setText((mViewModel.timeOptions[mViewModel.timeForGameSelection] / 60000) + " мин.");
        }



        view.findViewById(R.id.leftDir).setVisibility(View.INVISIBLE);
        view.findViewById(R.id.rightDir).setVisibility(View.INVISIBLE);
        view.findViewById(R.id.timeLeft).setVisibility(View.INVISIBLE);


        // set up a click listener for everything we care about
        for (int id : CLICKABLES) {
            view.findViewById(id).setOnClickListener(this);
        }

        mViewModel.getTeamsLoadingStatus().removeObservers(getViewLifecycleOwner());
        mViewModel.getTeamsLoadingStatus().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean status) {
                if (status != null && status) {
                        mViewModel.touchPermission = true;
                        switchHostTeam(0);
                        switchRivalTeam(0);

                }
            }
        });

        return view;
    }

    private void displayScores(int scoreOfteamA, int scoreOfteamB) {
        scoreA.setText("" + scoreOfteamA);
        scoreB.setText("" + scoreOfteamB);
    }

    final static int[] CLICKABLES = {
            R.id.ball, R.id.rightDir,
            R.id.leftDir, R.id.buttonPlay, R.id.host_team_field, R.id.rival_team_field, R.id.time_for_game
    };


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Get a ScoreViewModel
        mViewModel = ViewModelProviders.of(getActivity()).get(ScoreViewModel.class);


        // Create and setup the {@link AudioManager} to request audio focus
        mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.time_for_game:
                makeSoundEffect(CLICK);
                mViewModel.timeForGameSelection++;
                if(mViewModel.timeForGameSelection == mViewModel.timeOptions.length) {
                    mViewModel.timeForGameSelection = 0;
                }
                if (mViewModel.timeForGameSelection == 0) {
                    timeForGameView.setText("20 сек.");
                } else {
                    timeForGameView.setText((mViewModel.timeOptions[mViewModel.timeForGameSelection] / 60000) + " мин.");
                }
                break;
            case R.id.ball:
                if (!mViewModel.gameOnRun) {
                    return;
                }
                if (!mViewModel.dirVisibility) {
                    getView().findViewById(R.id.leftDir).setVisibility(View.VISIBLE);
                    getView().findViewById(R.id.rightDir).setVisibility(View.VISIBLE);
                    mViewModel.dirVisibility = true;
                } else {
                    getView().findViewById(R.id.leftDir).setVisibility(View.INVISIBLE);
                    getView().findViewById(R.id.rightDir).setVisibility(View.INVISIBLE);
                    mViewModel.dirVisibility = false;
                }
                break;
            case R.id.rightDir:
                makeSoundEffect(KICK);
                startGoalAnimation(1);
                break;
            case R.id.leftDir:
                makeSoundEffect(KICK);
                startGoalAnimation(2);
                break;
            case R.id.buttonPlay:
                if (teamsAreTheSameCheck()) {
                    return;
                }
                getView().findViewById(R.id.buttonPlay).setVisibility(View.GONE);
                getView().findViewById(R.id.timeLeft).setVisibility(View.VISIBLE);
                startGame();
                break;
            case R.id.host_team_field:
                if(mViewModel.touchPermission && !mViewModel.gameOnRun) {
                    makeSoundEffect(CLICK);
                    mViewModel.hostTeamIndex ++;
                    if(mViewModel.hostTeamIndex == mViewModel.getTeams().getValue().size()) {
                        mViewModel.hostTeamIndex = 0;
                    }
                    switchHostTeam(mViewModel.hostTeamIndex);
                }
                break;
            case R.id.rival_team_field:
                if(mViewModel.touchPermission && !mViewModel.gameOnRun) {
                    makeSoundEffect(CLICK);
                    mViewModel.rivalTeamIndex ++;
                    if(mViewModel.rivalTeamIndex == mViewModel.getTeams().getValue().size()) {
                        mViewModel.rivalTeamIndex = 0;
                    }
                    switchRivalTeam(mViewModel.rivalTeamIndex);
                }
                break;
        }
    }

    private void switchRivalTeam(int index) {
        rivalName.setText(mViewModel.getTeams().getValue().get(index).getName());
        loadTeamEmblem(mViewModel.getTeams().getValue().get(index).getImage_uri(), rivalEmblem);
    }


    private void switchHostTeam(int index) {
        hostName.setText(mViewModel.getTeams().getValue().get(index).getName());
        loadTeamEmblem(mViewModel.getTeams().getValue().get(index).getImage_uri(), hostEmblem);
    }

    private void loadTeamEmblem(String image_uri, View view) {
        Uri uri = Uri.parse("android.resource://com.mycustom.dashboard/drawable/" + image_uri);
        Glide
                .with(this)
                .load(uri)
                .placeholder(R.color.medium_green)
                .into((ImageView) view);
    }

    private void startGoalAnimation(int direction) {
        getView().findViewById(R.id.leftDir).setVisibility(View.INVISIBLE);
        getView().findViewById(R.id.rightDir).setVisibility(View.INVISIBLE);
        mViewModel.dirVisibility = false;
        if (direction == 1) {
            anim = AnimationUtils.loadAnimation(getActivity(), R.anim.transright);
        } else {
            anim = AnimationUtils.loadAnimation(getActivity(), R.anim.transleft);
        }
        getView().findViewById(R.id.ball).startAnimation(anim);
        makeGoal(direction);
    }

    private void makeGoal(final int gate) {
        if (!mViewModel.gameOnRun) {
            return;
        }
        Handler handler = new Handler();
        Runnable bork = new Runnable() {
            @Override
            public void run() {
                if (gate == 1) {
                    mViewModel.scoreTeamHost++;
                } else {
                    mViewModel.scoreTeamRival++;
                }
                makeSoundEffect(GOAL);
                displayScores(mViewModel.scoreTeamHost, mViewModel.scoreTeamRival);
            }
        };
        handler.postDelayed(bork, 900);
    }

    private void startGame() {
        timeForGameView.setVisibility(View.GONE);
        makeSoundEffect(WHISTLE);
        mViewModel.gameOnRun = true;

        mViewModel.timer = new CountDownTimer2(mViewModel.timeOptions[mViewModel.timeForGameSelection], 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Date date = new Date(millisUntilFinished);
                DateFormat formatter = new SimpleDateFormat("mm:ss");
                String timeFormatted = formatter.format(date);
                timeView.setText(timeFormatted);
            }

            @Override
            public void onFinish() {
                makeSoundEffect(WHISTLE);
                mViewModel.gameOnRun = false;
                timeView.setText(getString(R.string.time_is_over));
                logResults();
                mViewModel.scoreTeamHost = 0;
                mViewModel.scoreTeamRival = 0;
                mViewModel.hostTeamIndex = 0;
                mViewModel.rivalTeamIndex = 0;
                mViewModel.touchPermission = false;
                commandResetUI();
            }
        };
        mViewModel.timer.start();


    }

    private void commandResetUI() {
        appExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    appExecutors.mainThread().execute(new Runnable() {
                        @Override
                        public void run() {
                            resetUI();
                        }
                    });
                }
            }
        });
    }

    private void resetUI() {
        if (mMediaPlayer != null) {
            makeSoundEffect(WHISTLE);
        }
        if(getView() == null) {
            return;
        }
        mViewModel.touchPermission = true;
        switchHostTeam(mViewModel.hostTeamIndex);
        switchRivalTeam(mViewModel.rivalTeamIndex);
        displayScores(mViewModel.scoreTeamHost, mViewModel.scoreTeamRival);
        getView().findViewById(R.id.buttonPlay).setVisibility(View.VISIBLE);
        getView().findViewById(R.id.timeLeft).setVisibility(View.GONE);
        timeForGameView.setVisibility(View.VISIBLE);
    }


    private void logResults() {
        final Team hostTeam = mViewModel.getTeams().getValue().get(mViewModel.hostTeamIndex);
        final Team guestTeam = mViewModel.getTeams().getValue().get(mViewModel.rivalTeamIndex);

        hostTeam.setGF(hostTeam.getGF() + mViewModel.scoreTeamHost);
        hostTeam.setGA(hostTeam.getGA() + mViewModel.scoreTeamRival);

        guestTeam.setGF(guestTeam.getGF() + mViewModel.scoreTeamRival);
        guestTeam.setGA(guestTeam.getGA() + mViewModel.scoreTeamHost);

        guestTeam.setPlayed(guestTeam.getPlayed() + 1);
        hostTeam.setPlayed(hostTeam.getPlayed() + 1);

        if(mViewModel.scoreTeamHost == mViewModel.scoreTeamRival) {
            hostTeam.setDrawn(hostTeam.getDrawn() + 1);
            guestTeam.setDrawn(guestTeam.getDrawn() + 1);
            hostTeam.setPoints(hostTeam.getPoints() + 1);
            guestTeam.setPoints(guestTeam.getPoints() + 1);
        } else if (mViewModel.scoreTeamHost > mViewModel.scoreTeamRival) {
            hostTeam.setWon(hostTeam.getWon() + 1);
            guestTeam.setLost(guestTeam.getLost() + 1);
            hostTeam.setPoints(hostTeam.getPoints() + 3);
        } else {
            hostTeam.setLost(hostTeam.getLost() + 1);
            guestTeam.setWon(guestTeam.getWon() + 1);
            guestTeam.setPoints(guestTeam.getPoints() + 3);
        }

        final Game theGame = new Game(hostTeam.getName(), guestTeam.getName(), System.currentTimeMillis(),
                mViewModel.timeOptions[mViewModel.timeForGameSelection], mViewModel.scoreTeamHost, mViewModel.scoreTeamRival);

        appExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                 teamsDao.updateTeamResults(hostTeam.getName(), hostTeam.getPlayed(), hostTeam.getWon(), hostTeam.getDrawn(),
                         hostTeam.getLost(), hostTeam.getGF(), hostTeam.getGA(), hostTeam.getPoints());
                teamsDao.updateTeamResults(guestTeam.getName(), guestTeam.getPlayed(), guestTeam.getWon(), guestTeam.getDrawn(),
                        guestTeam.getLost(), guestTeam.getGF(), guestTeam.getGA(), guestTeam.getPoints());
                gamesDao.insertNewResults(theGame);
                mViewModel.startTeamsLoading();
                mViewModel.loadAllPlayedGames();
            }
        });
    }

    private boolean teamsAreTheSameCheck() {
        if (mViewModel.hostTeamIndex == mViewModel.rivalTeamIndex) {
            Toast.makeText(getActivity(), getString(R.string.different_teams_requirement), Toast.LENGTH_LONG).show();
            makeSoundEffect(Constants.WRONG);
            return true;
        }
        return false;
    }


    private void releaseMediaPlayer() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
            mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
        }
    }

    private void makeSoundEffect (String nameOfSound) {
        releaseMediaPlayer();
        int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener,
                AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
        if (result != AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            return;
        }
        switch (nameOfSound) {
            case CLICK:
                mMediaPlayer = MediaPlayer.create(getActivity(), R.raw.click);
                break;
            case WRONG:
                mMediaPlayer = MediaPlayer.create(getActivity(), R.raw.wrong);
                break;
            case WHISTLE:
                mMediaPlayer = MediaPlayer.create(getActivity(), R.raw.whistle);
                break;
            case KICK:
                mMediaPlayer = MediaPlayer.create(getActivity(), R.raw.kick);
                break;
            case GOAL:
                mMediaPlayer = MediaPlayer.create(getActivity(), R.raw.goal);
                break;
        }
        if (mMediaPlayer != null) {
            mMediaPlayer.start();
            mMediaPlayer.setOnCompletionListener(mCompletionListener);
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        releaseMediaPlayer();
    }





}
