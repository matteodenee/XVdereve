package com.xvdereve.model;
import java.util.ArrayList;

public class Team {
    private static int nextId = 1;
    private int id;
    Country country;
    int year;
    ArrayList<Player> players;
    int GeneralNote;
    
    public Team(Country country , int year , ArrayList<Player> players ){
        this.id = nextId++;
        this.country = country;
        this.year = year;
        this.players = players;
    }

    public int GetGeneralNote(){
        int total = 0;
        for (Player player : players) {
            total += player.getOverall();
        }
        return (total / players.size());
    }

    public int GetBacksNote() {
        int total = 0;
        int count = 0;

        for (Player player : players) {
            if (
                player.position == Position.DemiDeMelee ||
                player.position == Position.DemiOuverture ||
                player.position == Position.Centre ||
                player.position == Position.Ailier ||
                player.position == Position.Arriere
            ) {
                total += player.getOverall();
                count++;
            }
        }

        if (count == 0) {
            return 0;
        }

        return total / count;
    }
    public int GetForwardsNote() {
        int total = 0;
        int count = 0;

        for (Player player : players) {
            if (
                player.position == Position.Pilier ||
                player.position == Position.Talonneur ||
                player.position == Position.DeuxiemeLigne ||
                player.position == Position.TroisiemeLigneAile ||
                player.position == Position.TroisiemeLigneCentre
            ) {
                total += player.getOverall();
                count++;
            }
        }

        if (count == 0) {
            return 0;
        }

        return total / count;
    }
    @Override
    public String toString() {
        String text =  country + " " + year + " (" + players.size() + " joueurs)" + "\n";
        for (Player player : players) {
                    text += player + "\n";
                }
        return text;
    }

    public Country getCountry() {
    return country;
    }

    public int getYear() {
        return year;
    }
    public int getId() {
        return id;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }
    
}