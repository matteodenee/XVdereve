package com.xvdereve.model;

import java.util.ArrayList;

public class TournamentResult {

    private boolean qualified;
    private boolean champion;

    private int points;
    private int wins;
    private int losses;

    private ArrayList<MatchResult> matches;
    private ArrayList<PoolStanding> poolStandings = new ArrayList<>();
    private ArrayList<MatchResult> poolMatches = new ArrayList<>();

    public TournamentResult(
            boolean qualified,
            boolean champion,
            int points,
            int wins,
            int losses,
            ArrayList<MatchResult> matches
    ) {
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

    public ArrayList<PoolStanding> getPoolStandings() {
        return poolStandings;
    }

    public void setPoolStandings(ArrayList<PoolStanding> poolStandings) {
        this.poolStandings = poolStandings;
    }

    public ArrayList<MatchResult> getPoolMatches() {
        return poolMatches;
    }

    public void setPoolMatches(ArrayList<MatchResult> poolMatches) {
        this.poolMatches = poolMatches;
    }
}