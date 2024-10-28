package com.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import com.exception.DataAccessException;
import com.model.Team;
import com.repository.impl.TeamRepositoryImpl;
import com.service.ITeamService;

public class TeamServiceImpl implements ITeamService {
    private final Logger logger = LoggerFactory.getLogger(TeamServiceImpl.class);
    private final TeamRepositoryImpl teamRepositoryImpl;

    public TeamServiceImpl(TeamRepositoryImpl teamRepositoryImpl){
        this.teamRepositoryImpl = teamRepositoryImpl;
    }

    @Override
    @Transactional
    public boolean addTeam(Team team) {
       try {
        teamRepositoryImpl.save(team);
            return true; 
        } catch (DataAccessException e) {
            logger.error("Error in adding team", e);
            return false; 
        }
    }

    @Override
    public Team getTeamById(Long id) {
        try {
            return teamRepositoryImpl.findById(id).orElse(null);

        } catch (DataAccessException e) {
            logger.error("Error in retrieving team with id: {}", id, e);
            return null; 
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Team> getAllTeams() {
        List<Team> teams = null;
        try {
            teams =teamRepositoryImpl.findAll();
        } catch (DataAccessException e) {
            logger.error("Error in retrieving all players", e);
        }

        return teams;
    }

    @Override
    @Transactional
    public void update(Team team) {
        try {
            teamRepositoryImpl.update(team);
        } catch (DataAccessException e) {
            logger.error("Error in updating team", e);
            throw e; 
        }
    }

    @Override
    @Transactional
    public void delete(Team team) {
        try {
            teamRepositoryImpl.delete(team);
        } catch (DataAccessException e) {
            logger.error("Error in deleting team", e);
            throw e;
        }
    }

}
