package com.repository.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.SessionFactory;

import com.model.Game;
import com.model.Team;
import com.model.Tournament;

public class TournamentRepositoryImpl extends RepositoryImpl<Tournament, Long>  {

    public TournamentRepositoryImpl(SessionFactory sessionFactory) {
        super(Tournament.class);
        this.setSessionFactory(sessionFactory); 
    }

    public int estimatedDuration(Long tournamentId){
        Tournament t = this.findById(tournamentId).orElse(null);
        int nbTeams = t.getTeams().size();
        List<Team> teams = t.getTeams();
        List<Game> games = t.getGames();


         // duration of every match
        List<Integer> durations = games.stream().map(g->g.getDurationAverageMatch()).collect(Collectors.toList());

        return
         (durations.stream()
        .reduce(1, (a, b) -> a * b) * nbTeams)
        + t.getTimePause();

    }

    

   
}
