package com.xvdereve.model;

public class PoolStanding {

    private final Team team;
    private final boolean playerTeam;

    private int played;
    private int wins;
    private int draws;
    private int losses;
    private int pointsFor;
    private int pointsAgainst;
    private int points;

    public PoolStanding(Team team, boolean playerTeam) {
        this.team = team;
        this.playerTeam = playerTeam;
    }

    public void addResult(int scored, int conceded, int rankingPoints) {
        played++;
        pointsFor += scored;
        pointsAgainst += conceded;
        points += rankingPoints;

        if (scored > conceded) {
            wins++;
        } else if (scored < conceded) {
            losses++;
        } else {
            draws++;
        }
    }

    public Team getTeam() {
        return team;
    }

    public boolean isPlayerTeam() {
        return playerTeam;
    }

    public int getPlayed() {
        return played;
    }

    public int getWins() {
        return wins;
    }

    public int getDraws() {
        return draws;
    }

    public int getLosses() {
        return losses;
    }

    public int getPointsFor() {
        return pointsFor;
    }

    public int getPointsAgainst() {
        return pointsAgainst;
    }

    public int getPointsDifference() {
        return pointsFor - pointsAgainst;
    }

    public int getPoints() {
        return points;
    }
}