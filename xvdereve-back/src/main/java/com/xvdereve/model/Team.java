package com.xvdereve.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "team")
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String country;

    @Column(nullable = false)
    private int year;

    private String description;

    @Column(name = "logo_url")
    private String logoUrl;

    @OneToMany(mappedBy = "team")
    private List<Player> players = new ArrayList<>();


    protected Team() {
        // Obligatoire pour JPA
    }


    // Ce constructeur reste utile pour les équipes temporaires
    // créées pendant les simulations.
    public Team(String country, int year, ArrayList<Player> players) {
        this.country = country;
        this.year = year;
        this.players = players;
    }


    public int GetGeneralNote() {
        if (players.isEmpty()) {
            return 0;
        }

        int total = 0;

        for (Player player : players) {
            total += player.getOverall();
        }

        return total / players.size();
    }


    public int GetBacksNote() {
        int total = 0;
        int count = 0;

        for (Player player : players) {
            if (
                player.getPosition() == Position.DemiDeMelee ||
                player.getPosition() == Position.DemiOuverture ||
                player.getPosition() == Position.Centre ||
                player.getPosition() == Position.Ailier ||
                player.getPosition() == Position.Arriere
            ) {
                total += player.getOverall();
                count++;
            }
        }

        return count == 0 ? 0 : total / count;
    }


    public int GetForwardsNote() {
        int total = 0;
        int count = 0;

        for (Player player : players) {
            if (
                player.getPosition() == Position.Pilier ||
                player.getPosition() == Position.Talonneur ||
                player.getPosition() == Position.DeuxiemeLigne ||
                player.getPosition() == Position.TroisiemeLigneAile ||
                player.getPosition() == Position.TroisiemeLigneCentre
            ) {
                total += player.getOverall();
                count++;
            }
        }

        return count == 0 ? 0 : total / count;
    }


    public int getId() {
        return id;
    }

    public String getCountry() {
        return country;
    }

    public int getYear() {
        return year;
    }

    public String getDescription() {
        return description;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public List<Player> getPlayers() {
        return players;
    }


    @Override
    public String toString() {
        return country + " " + year + " (" + players.size() + " joueurs)";
    }
}