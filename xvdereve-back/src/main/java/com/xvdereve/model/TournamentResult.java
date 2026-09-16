package com.xvdereve.model;
import java.util.ArrayList;

public class TournamentResult {

    private boolean qualified;
    private boolean champion;

    private int points;
    private int wins;
    private int losses;

    private ArrayList<MatchResult> matches;

    public TournamentResult(
            boolean qualified,
            boolean champion,
            int points,
            int wins,
            int losses,
            ArrayList<MatchResult> matches) {

        this.qualified = qualified;
        this.champion = champion;
        this.points = points;
        this.wins = wins;
        this.losses = losses;
        this.matches = matches;
    }

    public boolean isQualified() {
        return qualified;
    }

    public boolean isChampion() {
        return champion;
    }

    public int getPoints() {
        return points;
    }

    public int getWins() {
        return wins;
    }

    public int getLosses() {
        return losses;
    }

    public ArrayList<MatchResult> getMatches() {
        return matches;
    }
}