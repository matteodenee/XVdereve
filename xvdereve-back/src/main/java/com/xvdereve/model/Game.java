package com.xvdereve.model;

import java.util.ArrayList;

import com.xvdereve.exception.PlayerNotFoundException;

public class Game {

    private ArrayList<Team> teams;
    private YourTeam myTeam;
    private Team currentTeam;
    private int remainingRespins;

    public Game(ArrayList<Team> teams) {
        this.teams = teams;
        this.myTeam = YourTeam.startEmptyTeam();
        this.remainingRespins = 3;
        this.currentTeam = YourTeam.GetRandomTeam(teams);
    }

    public Team getCurrentTeam() {
        return currentTeam;
    }

    public YourTeam getMyTeam() {
        return myTeam;
    }

    public int getRemainingRespins() {
        return remainingRespins;
    }

    public ArrayList<Player> getAvailablePlayers() {
        return myTeam.getAvailablePlayers(currentTeam);
    }

    public void respin() {
        if (remainingRespins <= 0) {
            throw new IllegalStateException("Tu n'as plus de respin");
        }

        currentTeam = YourTeam.GetRandomTeam(teams);
        remainingRespins--;
    }

    public Player findPlayerById(int playerId) {
        for (Team team : teams) {
            for (Player player : team.getPlayers()) {
                if (player.getId() == playerId) {
                    return player;
                }
            }
        }

        throw new PlayerNotFoundException(playerId);
    }

    public void chooseKicker(int playerId) {
        Player player = findPlayerById(playerId);
        myTeam.ChooseKicker(player);
    }

    public void pickPlayer(int playerId) {
        Player player = findPlayerById(playerId);

        if (!getAvailablePlayers().contains(player)) {
            throw new IllegalArgumentException("Ce joueur n'est pas sélectionnable maintenant");
        }

        myTeam.AddPlayer(player);

        if (!myTeam.IsComplete()) {
            currentTeam = YourTeam.GetRandomTeam(teams);
        } else {
            myTeam.team = new Team("France", 0, myTeam.getPlayers());
        }
    }

    public boolean isDraftFinished() {
        return myTeam.IsComplete();
    }

    public ArrayList<Team> getTeams() {
    return teams;
    }

    public GameState getState() {
    return new GameState(this);
    }
}