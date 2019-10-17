package com.mycustom.dashboard.room_db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.mycustom.dashboard.models.Team;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.IGNORE;
import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface TeamsDao {

    @Insert(onConflict = IGNORE)
    void insertTeams (Team... teams);

    @Insert(onConflict = REPLACE)
    void insertNewTeam (Team team);

    @Delete
    void deleteTheTeam(Team team);

    @Query("SELECT * FROM teams")
    List<Team> getAllTeams();

    @Query("UPDATE teams SET played = :played, won = :won, drawn = :drawn, lost = :lost, GF = :GF, GA = :GA, points = :points WHERE name = :name ")
    void updateTeamResults(String name, int played, int won, int drawn, int lost, int GF, int GA, int points);

}
