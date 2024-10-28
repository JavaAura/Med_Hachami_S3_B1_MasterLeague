package com.model;

import java.util.Date;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.*;
import com.model.enums.TournamentStatut;

@Entity
@Table(name = "tournament")
public class Tournament {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Tournament name should not be null")
    @Size(min = 3, max = 100, message = "Tournament name must be between 3 and 100 characters")
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull(message = "Start date should not be null")
    @Future(message = "Start date should be a future date")
    @Temporal(TemporalType.DATE)
    @Column(name = "date_start", nullable = false)
    private Date dateStart;

    @NotNull(message = "End date should not be null")
    @Temporal(TemporalType.DATE)
    @Column(name = "end_date", nullable = false)
    private Date endDate;

    @Min(value = 0, message = "Number of spectators must be zero or a positive integer")
    @Column(name = "number_spectators")
    private int numberSpectators;

    @ManyToMany(mappedBy = "tournaments")
    private List<Team> teams;

    @Min(value = 1, message = "Estimated duration must be at least 1 minute")
    @Column(name = "estimated_duration")
    private int estimatedDuration;

    @Min(value = 0, message = "Time pause must be zero or a positive integer")
    @Column(name = "time_pause")
    private int timePause;

    @Min(value = 0, message = "Time ceremony must be zero or a positive integer")
    @Column(name = "time_ceremony")
    private int timeCeremony;

    @NotNull(message = "Tournament status must be specified")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TournamentStatut statut;

    @OneToMany(mappedBy = "tournament", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Game> games;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Date getDateStart() { return dateStart; }
    public void setDateStart(Date dateStart) { this.dateStart = dateStart; }

    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }

    public int getNumberSpectators() { return numberSpectators; }
    public void setNumberSpectators(int numberSpectators) { this.numberSpectators = numberSpectators; }

    public List<Team> getTeams() { return teams; }
    public void setTeams(List<Team> teams) { this.teams = teams; }

    public int getEstimatedDuration() { return estimatedDuration; }
    public void setEstimatedDuration(int estimatedDuration) { this.estimatedDuration = estimatedDuration; }

    public int getTimePause() { return timePause; }
    public void setTimePause(int timePause) { this.timePause = timePause; }

    public int getTimeCeremony() { return timeCeremony; }
    public void setTimeCeremony(int timeCeremony) { this.timeCeremony = timeCeremony; }

    public List<Game> getGames() { return games; }
    public void setGames(List<Game> games) { this.games = games; }

    public TournamentStatut getStatut() { return statut; }
    public void setStatut(TournamentStatut statut) { this.statut = statut; }

    // toString method for debugging
    @Override
    public String toString() {
        return "Tournament{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", dateStart=" + dateStart +
                ", endDate=" + endDate +
                ", numberSpectators=" + numberSpectators +
                ", estimatedDuration=" + estimatedDuration +
                ", timePause=" + timePause +
                ", timeCeremony=" + timeCeremony +
                ", statut=" + statut +
                '}';
    }
}
