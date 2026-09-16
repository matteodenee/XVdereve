package com.xvdereve.exception;

public class PlayerNotFoundException extends RuntimeException {

    public PlayerNotFoundException(int playerId) {
        super("Aucun joueur avec l'id : " + playerId);
    }

}