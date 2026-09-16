package com.xvdereve.service;

import org.springframework.stereotype.Service;

import com.xvdereve.model.Game;
import com.xvdereve.model.GameState;
import com.xvdereve.model.TournamentResult;
import com.xvdereve.model.Simulation;

@Service
public class GameService {

    private Game game;

    public String getStatus() {
        return "GameService fonctionne";
    }

    public GameState startGame() {
        game = new Game();
        return game.getState();
    }

    public GameState getState() {
        if (game == null) {
            throw new IllegalStateException("Aucune partie n'a été lancée");
        }

        return game.getState();
    }

    public GameState respin() {

        if (game == null) {
            throw new IllegalStateException("Aucune partie en cours");
        }

        game.respin();

        return game.getState();
    }

    public GameState pickPlayer(int playerId) {
        if (game == null) {
            throw new IllegalStateException("Aucune partie en cours");
        }

        game.pickPlayer(playerId);

        return game.getState();
    }

    public GameState chooseKicker(int playerId) {

        if (game == null) {
            throw new IllegalStateException("Aucune partie en cours");
        }

        game.chooseKicker(playerId);

        return game.getState();
    }

    public TournamentResult simulateTournament() {
        if (game == null) {
            throw new IllegalStateException("Aucune partie en cours");
        }

        if (!game.isDraftFinished()) {
            throw new IllegalStateException("Le draft n'est pas terminé");
        }

        if (game.getMyTeam().getKicker() == null) {
            throw new IllegalStateException("Aucun buteur sélectionné");
        }

        return Simulation.PlayTournament(game.getMyTeam(), game.getTeams());
    }
}