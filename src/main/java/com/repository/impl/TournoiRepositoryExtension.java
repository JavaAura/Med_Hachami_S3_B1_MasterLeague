package com.repository.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.SessionFactory;

import com.model.Game;
import com.model.Team;
import com.model.Tournament;

public class TournoiRepositoryExtension extends RepositoryImpl<Tournament, Long> {

    public TournoiRepositoryExtension(SessionFactory sessionFactory) {
        super(Tournament.class);
        this.setSessionFactory(sessionFactory); 
    }

    public int estimatedDuration(Long tournamentId){
        Tournament t = this.findById(tournamentId).orElse(null);
        int nbTeams = t.getTeams().size();
        List<Team> teams = t.getTeams();
        int teamsSize = teams.size();
        List<Game> games = t.getGames();

        int estimated_duration = 0;
        List<Integer> durations = games.stream().map(g->g.getDurationAverageMatch()).collect(Collectors.toList());

        int t2 = t.getTimePause() + t.getTimeCeremony();

        for (int i=0; i<=games.size(); i++) {
            int t1 = teamsSize*durations.get(i)*games.get(0).getDifficulty();

            estimated_duration += t1;
        }

        return estimated_duration+t2;

    }
}
