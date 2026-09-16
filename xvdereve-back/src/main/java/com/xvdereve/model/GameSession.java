package com.xvdereve.model;
import java.util.UUID;

public class GameSession {

    private UUID id;

    private GameState gameState;

    private TournamentResult tournamentResult;

    public GameSession() {
    this.id = UUID.randomUUID();
    }

    public UUID getId() {
        return id;
    }

    public GameState getGameState() {
        return gameState;
    }

    public TournamentResult getTournamentResult() {
        return tournamentResult;
    }
}