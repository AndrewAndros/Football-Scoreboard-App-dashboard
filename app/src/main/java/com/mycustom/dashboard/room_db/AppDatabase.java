package com.mycustom.dashboard.room_db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.mycustom.dashboard.models.Game;
import com.mycustom.dashboard.models.Team;

import static com.mycustom.dashboard.utils.Constants.DATABASE_NAME;

@Database(entities = {Team.class, Game.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {



    private static AppDatabase instance;

    public static AppDatabase getInstance(final Context context){
        if(instance == null){
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    AppDatabase.class,
                    DATABASE_NAME
            ).build();
        }
        return instance;
    }

    public abstract TeamsDao teamsDao();
    public abstract GamesDao gamesDao();


}


