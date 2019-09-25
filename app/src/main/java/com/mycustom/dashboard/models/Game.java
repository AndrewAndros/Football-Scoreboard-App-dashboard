package com.mycustom.dashboard.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Game {

    @PrimaryKey (autoGenerate = true)
    private int gameId;

    @ColumnInfo
    private String hostTeam;

    @ColumnInfo
    private String rivalTeam;

    @ColumnInfo
    private long date;

    @ColumnInfo
    private long playTime;

    @ColumnInfo
    private int hostScore;


    @ColumnInfo
    private int rivalScore;

    public Game(String hostTeam, String rivalTeam, long date, long playTime, int hostScore, int rivalScore) {
        this.hostTeam = hostTeam;
        this.rivalTeam = rivalTeam;
        this.date = date;
        this.playTime = playTime;
        this.hostScore = hostScore;
        this.rivalScore = rivalScore;
    }


    public String getHostTeam() {
        return hostTeam;
    }

    public void setHostTeam(String hostTeam) {
        this.hostTeam = hostTeam;
    }

    public String getRivalTeam() {
        return rivalTeam;
    }

    public void setRivalTeam(String rivalTeam) {
        this.rivalTeam = rivalTeam;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getHostScore() {
        return hostScore;
    }

    public void setHostScore(int hostScore) {
        this.hostScore = hostScore;
    }

    public int getRivalScore() {
        return rivalScore;
    }

    public void setRivalScore(int rivalScore) {
        this.rivalScore = rivalScore;
    }

    public long getPlayTime() {
        return playTime;
    }

    public void setPlayTime(long playTime) {
        this.playTime = playTime;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }
}
