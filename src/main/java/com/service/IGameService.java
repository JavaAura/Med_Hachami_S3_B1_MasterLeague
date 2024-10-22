package com.service;

import java.util.List;

import com.model.Game;

public interface IGameService {
    public boolean addGame(Game game);
    public Game getGameById(Long id);
    public List<Game> getAllGames();
    public void update(Game game);
    public void delete(Game game);
}
