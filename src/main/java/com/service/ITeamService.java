package com.service;

import java.util.List;

import com.model.Team;

public interface ITeamService {
    public boolean addTeam(Team team);
    public Team getTeamById(Long id);
    public List<Team> getAllTeams();
    public void update(Team team);
    public void delete(Team team);
}
