package com.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import com.exception.DataAccessException;
import com.model.Player;
import com.repository.impl.PlayerRepositoryImpl;
import com.service.IPlayerService;

public class PlayerServiceImpl implements IPlayerService {

    private final PlayerRepositoryImpl playerRepository;
    private final Logger logger = LoggerFactory.getLogger(PlayerServiceImpl.class);

    public PlayerServiceImpl(PlayerRepositoryImpl playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Override
    @Transactional
    public boolean addPlayer(Player player) {
        try {
            playerRepository.save(player);
            return true; 
        } catch (DataAccessException e) {
            logger.error("Error in adding player", e);
            return false; 
        }
    }

    @Override
    public Player getPlayerById(Long id) {
        try {
            return playerRepository.findById(id).orElse(null);
        } catch (DataAccessException e) {
            logger.error("Error in retrieving player with id: {}", id, e);
            return null;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Player> getAllPlayers() {
        List<Player> players = null;
        try {
             players =playerRepository.findAll();
        } catch (DataAccessException e) {
            logger.error("Error in retrieving all players", e);
        }

        return players;
    }

    @Override
    public void update(Player player) {
        try {
            playerRepository.update(player);
        } catch (DataAccessException e) {
            logger.error("Error in updating player", e);
            throw e; 
        }
    }

    @Override
    public void delete(Player player) {
        try {
            playerRepository.delete(player);
        } catch (DataAccessException e) {
            logger.error("Error in deleting player", e);
            throw e;
        }
    }
}
