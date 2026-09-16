package com.xvdereve.model;
import java.util.ArrayList;


public class GameState {

    private Team currentTeam;
    private ArrayList<Player> availablePlayers;
    private ArrayList<Player> selectedPlayers;
    private ArrayList<Position> remainingPositions;
    private int remainingRespins;
    private boolean draftFinished;
    private Player kicker;

    public GameState(Game game) {
        this.currentTeam = game.getCurrentTeam();
        this.availablePlayers = game.getAvailablePlayers();
        this.selectedPlayers = game.getMyTeam().getPlayers();
        this.remainingPositions = game.getMyTeam().getRemainingPositions();
        this.remainingRespins = game.getRemainingRespins();
        this.draftFinished = game.isDraftFinished();
        this.kicker = game.getMyTeam().getKicker();
    }

    public Team getCurrentTeam() {
        return currentTeam;
    }

    public ArrayList<Player> getAvailablePlayers() {
        return availablePlayers;
    }

    public ArrayList<Player> getSelectedPlayers() {
        return selectedPlayers;
    }

    public ArrayList<Position> getRemainingPositions() {
        return remainingPositions;
    }

    public int getRemainingRespins() {
        return remainingRespins;
    }

    public boolean isDraftFinished() {
        return draftFinished;
    }

    public Player getKicker() {
        return kicker;
    }
}