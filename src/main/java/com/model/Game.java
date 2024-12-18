package com.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(
    name = "game",
    uniqueConstraints = @UniqueConstraint(columnNames = {"name", "tournament_id"})
)
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 3)
    @Column(name = "name", nullable = false, length = 75)
    private String name;


     @Column(name = "difficulty")
    @Min(value = 1, message = "Difficulty must be at least 1")
    @Max(value = 6, message = "Difficulty cannot exceed 6")
    private int difficulty;

    @Column(name = "duration_average_match")
    @Min(value = 1, message = "Average match duration must be at least 1 minute")
    @Max(value = 180, message = "Average match duration cannot exceed 180 minutes")
    private int durationAverageMatch;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "tournament_id" , nullable = true)
    private Tournament tournament;



    // Constructors
    public Game() {
    }

    public Game(String name) {
        this.name = name;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotNull @Size(min = 3, max = 75) String getName() {
        return name;
    }

    public void setName(@NotNull @Size(min = 3, max = 75) String name) {
        this.name = name;
    }

    public int getDifficulty(){
        return this.difficulty;
    }

    public void setDifficulty(int difficulty){
        this.difficulty = difficulty;
    }

    public void setDurationAverageMatch(int durationAverageMatch){
        this.durationAverageMatch = durationAverageMatch;
    }

    public int getDurationAverageMatch(){
       return this.durationAverageMatch;
    }
    public void setTournament(Tournament tournament){
        this.tournament = tournament;
    }

    public Tournament getTournament(){
        return this.tournament;
    }

   
}