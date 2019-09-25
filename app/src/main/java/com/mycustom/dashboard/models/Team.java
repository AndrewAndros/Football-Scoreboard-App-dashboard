package com.mycustom.dashboard.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class Team implements Comparable<Team> {

    @PrimaryKey
    @NonNull
    private String name;

    @ColumnInfo
    private String image_uri;

    @ColumnInfo
    private int played;

    @ColumnInfo
    private int won;

    @ColumnInfo
    private int lost;

    @ColumnInfo
    private int drawn;

    @ColumnInfo
    private int GF; // GF - goals for

    @ColumnInfo
    private int GA; // GA - goals against





    @ColumnInfo
    private int points;

    public Team(String name, String image_uri) {
        this.name = name;
        this.image_uri = image_uri;
    }




    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage_uri() {
        return image_uri;
    }

    public void setImage_uri(String image_uri) {
        this.image_uri = image_uri;
    }

    public int getPlayed() {
        return played;
    }

    public void setPlayed(int played) {
        this.played = played;
    }

    public int getWon() {
        return won;
    }

    public void setWon(int won) {
        this.won = won;
    }

    public int getLost() {
        return lost;
    }

    public void setLost(int lost) {
        this.lost = lost;
    }

    public int getDrawn() {
        return drawn;
    }

    public void setDrawn(int drawn) {
        this.drawn = drawn;
    }

    public int getGF() {
        return GF;
    }

    public void setGF(int GF) {
        this.GF = GF;
    }

    public int getGA() {
        return GA;
    }

    public void setGA(int GA) {
        this.GA = GA;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }


    private Long getComparedScore() {
        long comparedScore = (long) this.points * 1000;
        comparedScore = comparedScore + this.getGF() - this.getGA();
        return comparedScore;
    }



    @Override
    public int compareTo(Team team) {
        return this.getComparedScore().compareTo(team.getComparedScore());
    }
}
