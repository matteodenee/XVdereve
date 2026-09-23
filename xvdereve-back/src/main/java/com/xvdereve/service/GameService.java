package com.xvdereve.service;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.xvdereve.model.Game;
import com.xvdereve.model.GameState;
import com.xvdereve.model.TournamentResult;
import com.xvdereve.model.Simulation;
import com.xvdereve.model.Team;
import com.xvdereve.repository.TeamRepository;

@Service
public class GameService {

    private Game game;
    private final TeamRepository teamRepository;
    
    public GameService(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
        }      

    public String getStatus() {
        return "GameService fonctionne";
    }

    public GameState startGame() {
        ArrayList<Team> teams =
            new ArrayList<>(teamRepository.findAllWithPlayers());
        if (teams.isEmpty()) {
            throw new IllegalStateException(
                "Aucune équipe disponible dans la base de données"
            );
        }
        game = new Game(teams);
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