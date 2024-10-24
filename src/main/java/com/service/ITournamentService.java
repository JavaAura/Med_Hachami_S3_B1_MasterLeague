package com.service;

import java.util.List;

import com.model.Tournament;

public interface ITournamentService {
    public boolean addTournament(Tournament tournament);
    public Tournament getTournamentById(Long id);
    public List<Tournament> getAllTournaments();
    public void update(Tournament tournament);
    public void delete(Tournament tournament);
    // public double 
}
