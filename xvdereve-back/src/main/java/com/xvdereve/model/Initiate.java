package com.xvdereve.model;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class Initiate {


    private ArrayList<Team> AddPlayer(ArrayList<Team> teams , ArrayList<Player> players){
        for (Team team : teams) {
            for (Player player : players) {
             if(player.year == team.year && player.country == team.country){
                team.players.add(player);
             }
            }
        }
        return teams;
    }

    private ArrayList<Team> CreateTeams(String csvPath) {
        ArrayList<Team> teams = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvPath))) {

            String line;

            while ((line = br.readLine()) != null) {

                // Ignore lignes vides + en-tête
                if (line.isBlank() || line.toLowerCase().startsWith("pays")) {
                    continue;
                }

                String[] values = line.split(",");

                Country country = Country.valueOf(values[0].trim());
                int year = Integer.parseInt(values[1].trim());

                boolean alreadyExists = false;

                for (Team team : teams) {

                    if (team.country == country && team.year == year) {
                        alreadyExists = true;
                        break;
                    }
                }

                if (!alreadyExists) {
                    teams.add(new Team(
                        country,
                        year,
                        new ArrayList<Player>()
                    ));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return teams;
    }

  private ArrayList<Player> CreatePlayers(String csvPath) {
    ArrayList<Player> players = new ArrayList<>();

    try (BufferedReader br = new BufferedReader(new FileReader(csvPath))) {
        String line;

        while ((line = br.readLine()) != null) {

            if (line.isBlank() || line.startsWith("year")) {
                continue;
            }

            String[] values = line.split(",");

            int year = Integer.parseInt(values[0].trim());
            Country country = Country.valueOf(values[1].trim());
            String name = values[2].trim();
            Position position = Position.valueOf(values[3].trim());
            int overall = Integer.parseInt(values[4].trim());
            boolean canKick = Boolean.parseBoolean(values[5].trim());

            Player player = new Player(
                name,
                position,
                country,
                year,
                overall,
                canKick
            );

            players.add(player);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return players;
}
    



    public static ArrayList<Team> InitiateGame(){
        Initiate in = new Initiate();
        String playerCsvPath = "csv/Player.csv";
        String TeamCsvPath = "csv/Team.csv";
        ArrayList<Player> players = in.CreatePlayers(playerCsvPath);
        ArrayList<Team> teams = in.CreateTeams(TeamCsvPath);
        teams = in.AddPlayer(teams,players);
        return teams;
    }

    
}
