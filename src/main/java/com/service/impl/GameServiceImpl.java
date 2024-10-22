package com.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import com.exception.DataAccessException;
import com.model.Game;
import com.repository.impl.GameRepositoryImpl;
import com.service.IGameService;

public class GameServiceImpl implements IGameService {

    private final GameRepositoryImpl gameRepositoryImpl;
    private final Logger logger = LoggerFactory.getLogger(GameServiceImpl.class);

    public GameServiceImpl(GameRepositoryImpl gameRepositoryImpl) {
        this.gameRepositoryImpl = gameRepositoryImpl;
    }

    @Override
    @Transactional
    public boolean addGame(Game game) {
        try {
            this.gameRepositoryImpl.save(game);
            return true;
        } catch (DataAccessException e) {
            logger.error("Error in adding game", e);
            return false;

        }
    }

    @Override
    @Transactional(readOnly = true)
    public Game getGameById(Long id) {
        try {
            return this.gameRepositoryImpl.findById(id).orElse(null);
        } catch (DataAccessException e) {
            logger.error("Error in getting  game by id", e);
            return null;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Game> getAllGames() {
        List<Game> games = null;
        try {

            games = this.gameRepositoryImpl.findAll();
        } catch (DataAccessException e) {
            logger.error("Error in getting  all games", e);

        }
        return games;
    }

    @Override
    public void update(Game game) {
        try {
            this.gameRepositoryImpl.update(game);
        } catch (DataAccessException e) {
            logger.error("Error in updating game", e);
            throw e; 
        }
    }

    @Override
    public void delete(Game game) {
        try {
            this.gameRepositoryImpl.delete(game);
        } catch (DataAccessException e) {
            logger.error("Error in deleting game", e);
            throw e; 
        }
    }

}
