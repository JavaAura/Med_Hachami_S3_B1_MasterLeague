
package com.service;

import java.util.List;

import com.model.Player;

public interface IPlayerService {
    public boolean addPlayer(Player player);
    public Player getPlayerById(Long id);
    public List<Player> getAllPlayers();
    public void update(Player player);
    public void delete(Player player);

}
