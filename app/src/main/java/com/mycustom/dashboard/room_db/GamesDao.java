package com.mycustom.dashboard.room_db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.mycustom.dashboard.models.Game;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface GamesDao {

    @Insert(onConflict = REPLACE)
    void insertNewResults(Game game);


    @Delete
    void deleteTheResults(Game game);

    @Query("SELECT * FROM games ORDER BY gameId ASC")
    List<Game> getAllGamesList();
}
