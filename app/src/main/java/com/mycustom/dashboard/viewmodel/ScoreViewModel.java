package com.mycustom.dashboard.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.mycustom.dashboard.models.Game;
import com.mycustom.dashboard.models.Team;
import com.mycustom.dashboard.room_db.AppDatabase;
import com.mycustom.dashboard.room_db.GamesDao;
import com.mycustom.dashboard.room_db.InitialDataSource;
import com.mycustom.dashboard.room_db.TeamsDao;
import com.mycustom.dashboard.utils.AppExecutors;
import com.mycustom.dashboard.utils.CountDownTimer2;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ScoreViewModel extends AndroidViewModel {

    // Log
    private final String TAG = "I'm a test";

    // Tracks the score for Team A
    public int scoreTeamHost = 0;

    // Tracks the score for Team B
    public int scoreTeamRival = 0;

    // If game is active or not
    public boolean gameOnRun = false;

    // Determine if kick directions are shown or not
    public boolean dirVisibility = false;

    // Game timer
    public CountDownTimer2 timer;

    // Room DAO
    private TeamsDao teamsDao;
    private GamesDao gamesDao;

    // True when it's loaded from ROOM DB
    private MutableLiveData<Boolean> teamsLoadingComplete = new MutableLiveData();
    private MutableLiveData<Boolean> gamesLoadingComplete = new MutableLiveData();


    // LiveData
    public MutableLiveData<List<Team>> teams = new MutableLiveData();
    private MutableLiveData<List<Game>> games = new MutableLiveData();


    // Allows to change teams by touch on emblems (only before game starts)
    public boolean touchPermission = false;

    // which teams will play next game
    public int hostTeamIndex = 0;
    public int rivalTeamIndex = 0;

    // How long next game will last (5 options for user)
    // Current one is 5 minutes, index 0 for testing, it lasts 20 sec
    public int timeForGameSelection = 1;
    public long[] timeOptions = new long[5];

    // Called when a match is interrupted by user
    public boolean exitCommanded = false;


    // Auxiliary stuff
    private CompositeDisposable disposables = new CompositeDisposable();
    private AppExecutors appExecutors = AppExecutors.getInstance();
    public HashMap<String, Team> mappedTeams = new HashMap<>();



//    public MutableLiveData<Boolean> forceGameToStop = new MutableLiveData();



    public ScoreViewModel(@NonNull Application application) {
        super(application);
           teamsDao = AppDatabase.getInstance(application).teamsDao();
           gamesDao = AppDatabase.getInstance(application).gamesDao();
        timeOptions[0] = 20000;
        timeOptions[1] = 300000;
        timeOptions[2] = 600000;
        timeOptions[3] = 900000;
        timeOptions[4] = 1800000;
    }



    Observable<List<Team>> teamsLoader = Observable.fromCallable(new Callable<List<Team>>() {
        @Override
        public List<Team> call() throws Exception {
            return teamsDao.getAllTeams();
        }
    })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread());


    Observable<List<Game>> gamesLoader = Observable.fromCallable(new Callable<List<Game>>() {
        @Override
        public List<Game> call() throws Exception {
            return gamesDao.getAllGamesList();
        }
    })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread());


    public void loadAllPlayedGames() {
        gamesLoader.subscribe(new Observer<List<Game>>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposables.add(d);
            }

            @Override
            public void onNext(List<Game> loadedGames) {
              games.setValue(loadedGames);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
             gamesLoadingComplete.postValue(true);
            }
        });
    }





    public void startTeamsLoading() {
                teamsLoader.subscribe(new Observer<List<Team>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        if (teamsLoadingComplete.getValue() == null) {
                            teamsLoadingComplete.setValue(false);
                        }
                       // gamesLoadingComplete.postValue(0);
//                        forceGameToStop.postValue(false);
                        if (teams.getValue() != null) {
                            teams.getValue().clear();
                        }
                        disposables.add(d);
                    }

                    @Override
                    public void onNext(List<Team> teamsObtained) {
                    if (teamsObtained.isEmpty()) {
                        teams.setValue(InitialDataSource.createTeams());
                        appExecutors.diskIO().execute(new Runnable() {
                            @Override
                            public void run() {
                                Team[] teamsArray = new Team[teams.getValue().size()];
                                teamsDao.insertTeams((Team[])(teams.getValue().toArray(teamsArray)));
                            }
                        });

                        Log.d(TAG, "teams are taken from internal repository of the APP");
                    } else {
                        teams.setValue(teamsObtained);
                        Log.d(TAG, "teams are taken from AppDatabase");
                    }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "error occurred: " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "task is complete");
                        if(teamsLoadingComplete.getValue() == false) {
                            teamsLoadingComplete.postValue(true);
                            createMappedListOfTeams();
                        }
                        sortTeams();
                    }
                });
    }



    private void createMappedListOfTeams() {
        for (Team team : teams.getValue()) {
            mappedTeams.put(team.getName(), team);
        }
    }

    // I want to see teams' list sorted by their points in descending order
    private void sortTeams() {
        Collections.sort(teams.getValue(), Collections.<Team>reverseOrder());

    }

    public void clearDisposables() {
        disposables.clear();
    }

    public LiveData<Boolean> getTeamsLoadingStatus() {
        return teamsLoadingComplete;
    }

    public LiveData<Boolean> getGamesLoadingComplete() {
        return gamesLoadingComplete;
    }

    public LiveData<List<Team>> getTeams() {
        return teams;
    }

    public LiveData<List<Game>> getGames() {
        return games;
    }

//    public LiveData<Boolean> getForceGameToStop() {
//        return forceGameToStop;
//    }

}
