package com.xvdereve.controller;

import com.xvdereve.model.GameState;
import com.xvdereve.service.GameService;
import com.xvdereve.dto.ChooseKickerRequest;
import com.xvdereve.dto.PickPlayerRequest;
import org.springframework.web.bind.annotation.RequestBody;
import com.xvdereve.model.TournamentResult;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/api/game/status")
    public String getStatus() {
        return gameService.getStatus();
    }

    @PostMapping("/api/game/start")
    public GameState startGame() {
        return gameService.startGame();
    }

    @GetMapping("/api/game/state")
    public GameState getState() {
        return gameService.getState();
    }

    @PostMapping("/api/game/respin")
    public GameState respin() {
        return gameService.respin();
    }
    
    @PostMapping("/api/game/pick")
    public GameState pickPlayer(@RequestBody PickPlayerRequest request) {
        return gameService.pickPlayer(request.getPlayerId());
    }

    @PostMapping("/api/game/kicker")
    public GameState chooseKicker(
            @RequestBody ChooseKickerRequest request) {

        return gameService.chooseKicker(request.getPlayerId());
    }

    @PostMapping("/api/game/simulate")
    public TournamentResult simulateTournament() {
        return gameService.simulateTournament();
    }

}