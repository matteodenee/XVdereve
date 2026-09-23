package com.xvdereve.model;
import java.util.ArrayList;
import java.util.Random;

public class YourTeam {

    Team team;
    ArrayList<Player> players;
    ArrayList<Position> remainingPositions;
    Player kicker;

    private static final Random RANDOM = new Random();
    

    public YourTeam(Team team , ArrayList<Position> remainingPositions) {
        this.team = team;
        this.remainingPositions = remainingPositions;
    }
    

    public static Team GetRandomTeam(ArrayList<Team> teams) {
        if (teams == null || teams.isEmpty()) {
            throw new IllegalArgumentException("Aucune équipe chargée");
        }
        int randomIndex = RANDOM.nextInt(teams.size());

        Team randomTeam = teams.get(randomIndex);
        return randomTeam;
    }

    private YourTeam() {
        this.players = new ArrayList<>();
        this.remainingPositions = new ArrayList<>();

        // XV classique rugby
        remainingPositions.add(Position.Pilier);
        remainingPositions.add(Position.Pilier);

        remainingPositions.add(Position.Talonneur);

        remainingPositions.add(Position.DeuxiemeLigne);
        remainingPositions.add(Position.DeuxiemeLigne);

        remainingPositions.add(Position.TroisiemeLigneAile);
        remainingPositions.add(Position.TroisiemeLigneCentre);
        remainingPositions.add(Position.TroisiemeLigneAile);

        remainingPositions.add(Position.DemiDeMelee);
        remainingPositions.add(Position.DemiOuverture);

        remainingPositions.add(Position.Centre);
        remainingPositions.add(Position.Centre);

        remainingPositions.add(Position.Ailier);
        remainingPositions.add(Position.Ailier);

        remainingPositions.add(Position.Arriere);
    }


    public ArrayList<Player> getPlayers() {
    return players;
    }

    public ArrayList<Position> getRemainingPositions() {
    return remainingPositions;
    }

    public static YourTeam startEmptyTeam() {
    return new YourTeam();
    }

    public ArrayList<Player> getAvailablePlayers(Team randomTeam) {
    ArrayList<Player> availablePlayers = new ArrayList<>();

    for (Player player : randomTeam.getPlayers()) {
        if (remainingPositions.contains(player.getPosition()) && !players.contains(player)) {
            availablePlayers.add(player);
        }
    }

    return availablePlayers;
}

    public void ChooseKicker(Player player) {
        if (!players.contains(player)) {
            throw new IllegalArgumentException("Ce joueur n'est pas dans ton équipe");
        }

        this.kicker = player;
    }
    public void AddPlayer(Player player) {
        if (!remainingPositions.contains(player.getPosition())) {
            throw new IllegalArgumentException("Poste déjà rempli : " + player.getPosition());
        }

        players.add(player);
        remainingPositions.remove(player.getPosition());
    }


    public boolean IsComplete() {
        return remainingPositions.isEmpty();
    }
    
    public void setKicker(Player kicker) {
        this.kicker = kicker;
    }

    public Player getKicker() {
        return kicker;
    }


}