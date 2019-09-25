package com.mycustom.dashboard.room_db;

import com.mycustom.dashboard.models.Team;

import java.util.ArrayList;
import java.util.List;

public class InitialDataSource {


    public static List<Team> createTeams() {
        List<Team> teams = new ArrayList();
        teams.add(new Team("Топтун", "bear"));
        teams.add(new Team("Лисёнок", "fox"));
        teams.add(new Team("Стража", "guard"));
        teams.add(new Team("Короли", "king"));
        teams.add(new Team("Бородач", "lion"));
        teams.add(new Team("Тени", "ninja"));
        teams.add(new Team("Космос", "rocket"));
        teams.add(new Team("Звездочка", "star"));
        return teams;
    }


}
