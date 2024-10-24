package service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.exception.DataAccessException;
import com.model.Player;
import com.model.Team;
import com.repository.impl.PlayerRepositoryImpl;
import com.service.impl.PlayerServiceImpl;

public class PlayerServiceImplTest {

    @Mock
    private PlayerRepositoryImpl playerRepositoryImpl;

    @InjectMocks
    private PlayerServiceImpl playerServiceImpl;

    private Player player;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        Team team = new Team();
        team.setId(1L);
        team.setName("FCB");
        player = new  Player();
        player.setName("Med.Jr");
        player.setAge(22);
        player.setTeam(team);

    }

    @Test
    public void testAddPlayer_Success(){
        when(playerRepositoryImpl.save(player)).thenReturn(player);

        boolean result = playerServiceImpl.addPlayer(player);

        assertTrue(result);
        verify(playerRepositoryImpl).save(player);
    }

    @Test
    public void testAddPlayer_DataAccessException() {
        doThrow(new DataAccessException("DB error")).when(playerRepositoryImpl).save(player);

        boolean result = playerServiceImpl.addPlayer(player);

        assertFalse(result);
        verify(playerRepositoryImpl).save(player);
    }

    @Test
    public void testGetPlayerById_Success(){
         when(playerRepositoryImpl.findById(1L)).thenReturn(Optional.of(player));

        Player result = playerServiceImpl.getPlayerById(1L);

        assertNotNull(result);
        assertEquals("Med.Jr", result.getName());
    }

    @Test
    void testGetPlayerById_DataAccessException() {

        when(playerRepositoryImpl.findById(1L)).thenThrow(new DataAccessException("DB error"));

        Player result = playerServiceImpl.getPlayerById(1L);

        assertNull(result);
    }

    @Test
     void testGetAllPlayers_Success() {

        List<Player> mockPlayers = new ArrayList<>();
        mockPlayers.add(player);
        when(playerRepositoryImpl.findAll()).thenReturn(mockPlayers);

        List<Player> result = playerServiceImpl.getAllPlayers();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Med.Jr", result.get(0).getName());
    }

    @Test
    void testGetAllPlayers_DataAccessException() {
        when(playerRepositoryImpl.findAll()).thenThrow(new DataAccessException("DB error"));

        List<Player> result = playerServiceImpl.getAllPlayers();

        assertNull(result);
    }

      @Test
    void testUpdatePlayer_Success() {
        playerServiceImpl.update(player);

        verify(playerRepositoryImpl).update(player);
    }

    @Test
    void testUpdate_DataAccessException() {

        doThrow(new DataAccessException("DB error")).when(playerRepositoryImpl).update(player);

        // Act & Assert
        assertThrows(DataAccessException.class, () -> {
            playerServiceImpl.update(player);
        });
    }

    @Test
    void testDeletePlayer_Success() {

        playerServiceImpl.delete(player);

        verify(playerRepositoryImpl).delete(player);
    }

    @Test
    void testDeletePlayer_DataAccessException() {

        doThrow(new DataAccessException("DB error")).when(playerRepositoryImpl).delete(player);

        assertThrows(DataAccessException.class, () -> {
            playerServiceImpl.delete(player);
        });
    }
}
