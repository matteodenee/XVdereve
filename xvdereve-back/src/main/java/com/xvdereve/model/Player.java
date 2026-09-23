package com.xvdereve.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "player")
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Position position;

    @Column(nullable = false)
    private int overall;

    @Column(name = "can_kick", nullable = false)
    private boolean canKick;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    @JsonIgnore
    private Team team;


    protected Player() {
        // Obligatoire pour JPA
    }


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Position getPosition() {
        return position;
    }

    public int getOverall() {
        return overall;
    }

    public boolean isCanKick() {
        return canKick;
    }

    public Team getTeam() {
        return team;
    }

    public String getCountry() {
        return team != null ? team.getCountry() : null;
    }

    public int getYear() {
        return team != null ? team.getYear() : 0;
    }


    @Override
    public String toString() {
        String text = name + " - " + position + " - " + overall;

        if (canKick) {
            text += " - buteur";
        }

        return text;
    }
}