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
import com.model.Team;
import com.repository.impl.TeamRepositoryImpl;
import com.service.impl.TeamServiceImpl;

public class TeamServiceImplTest {

    @Mock
    private TeamRepositoryImpl teamRepositoryImpl;

    @InjectMocks
    private TeamServiceImpl teamServiceImpl;

    private Team team;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        team = new Team();
        team.setId(1L);
        team.setName("Team 1");
        team.setRanking(1);
    }

    @Test
    void testAddTeam_Success() {
        
        when(teamRepositoryImpl.save(team)).thenReturn(team);

        boolean result = teamServiceImpl.addTeam(team);

        assertTrue(result);
        verify(teamRepositoryImpl).save(team);
    }

    @Test
    void testAddTeam_DataAccessException() {

        doThrow(new DataAccessException("DB error")).when(teamRepositoryImpl).save(team);

        boolean result = teamServiceImpl.addTeam(team);

        assertFalse(result);
        verify(teamRepositoryImpl).save(team);
    }

    @Test
    void testGetTeamById_Success() {

        when(teamRepositoryImpl.findById(1L)).thenReturn(Optional.of(team));

        Team result = teamServiceImpl.getTeamById(1L);

        assertNotNull(result);
        assertEquals("Team 1", result.getName());
    }

    @Test
    void testGetTeamById_DataAccessException() {

        when(teamRepositoryImpl.findById(1L)).thenThrow(new DataAccessException("DB error"));

        Team result = teamServiceImpl.getTeamById(1L);

        assertNull(result);
    }

    @Test
    void testGetAllTeams_Success() {

        List<Team> mockTeams = new ArrayList<>();
        mockTeams.add(team);
        when(teamRepositoryImpl.findAll()).thenReturn(mockTeams);

        List<Team> result = teamServiceImpl.getAllTeams();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Team 1", result.get(0).getName());
    }

    @Test
    void testGetAllTeams_DataAccessException() {
        when(teamRepositoryImpl.findAll()).thenThrow(new DataAccessException("DB error"));

        List<Team> result = teamServiceImpl.getAllTeams();

        assertNull(result);
    }

    @Test
    void testUpdate_Success() {
        teamServiceImpl.update(team);

        verify(teamRepositoryImpl).update(team);
    }

    @Test
    void testUpdate_DataAccessException() {

        doThrow(new DataAccessException("DB error")).when(teamRepositoryImpl).update(team);

        // Act & Assert
        assertThrows(DataAccessException.class, () -> {
            teamServiceImpl.update(team);
        });
    }

    @Test
    void testDelete_Success() {

        teamServiceImpl.delete(team);

        verify(teamRepositoryImpl).delete(team);
    }

    @Test
    void testDelete_DataAccessException() {

        doThrow(new DataAccessException("DB error")).when(teamRepositoryImpl).delete(team);

        assertThrows(DataAccessException.class, () -> {
            teamServiceImpl.delete(team);
        });
    }
}
