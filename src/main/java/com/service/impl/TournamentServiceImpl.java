package com.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.exception.DataAccessException;
import com.model.Tournament;
import com.repository.impl.RepositoryImpl;
import com.service.ITournamentService;

public class TournamentServiceImpl implements ITournamentService {
    private final Logger logger = LoggerFactory.getLogger(TournamentServiceImpl.class);
    private final RepositoryImpl<Tournament, Long> repositoryImpl;

    public TournamentServiceImpl(RepositoryImpl<Tournament, Long> repositoryImpl){
        this.repositoryImpl = repositoryImpl;
    }



    @Override
    public boolean addTournament(Tournament tournament) {
        try {
            Tournament t = this.repositoryImpl.save(tournament);
            return t.getId() != null;
        } catch (DataAccessException e) {
            logger.error("Error in adding tournament", e);
            return false; 
        }
    }

    @Override
    public Tournament getTournamentById(Long id) {
        try {
            return repositoryImpl.findById(id).orElse(null);

        } catch (DataAccessException e) {
            logger.error("Error in retrieving tournament with id: {}", id, e);
            return null; 
        }
    }

    @Override
    public List<Tournament> getAllTournaments() {
         List<Tournament> tournaments = null;
        try {
            tournaments =repositoryImpl.findAll();
        } catch (DataAccessException e) {
            logger.error("Error in retrieving all tournaments", e);
        }

        return tournaments;
    }

    @Override
    public void update(Tournament tournament) {
        try {
            repositoryImpl.update(tournament);
        } catch (DataAccessException e) {
            logger.error("Error in updating tournament", e);
            throw e; 
        }
    }

    @Override
    public void delete(Tournament tournament) {
        try {
            repositoryImpl.delete(tournament);
        } catch (DataAccessException e) {
            logger.error("Error in deleting tournament", e);
            throw e;
        }
    }
    
}
