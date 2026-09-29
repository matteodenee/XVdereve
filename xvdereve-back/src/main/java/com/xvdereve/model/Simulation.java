package com.xvdereve.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;


public class  Simulation {
    private static final Random RANDOM = new Random();
   
   
    private int CalculatemyTeamCote(Team myTeam, Team adverseTeam){
        double coefRegulator = 0.7;
        int myTeamNote = myTeam.GetGeneralNote();
        int adverseTeamNote = adverseTeam.GetGeneralNote();

        int res = myTeamNote - adverseTeamNote;
        double coteMyTeam = 50 + res*coefRegulator;
        return ((int) Math.round(coteMyTeam));
    }
    private int CalculateAdverseTeamCote(int coteMyTeam){
        return (100 - coteMyTeam);
    }

    private int CalculateTryOccasion(int cote){
        double coefRegulator = 0.1;
        return (int) Math.round( cote * coefRegulator);
    }
    private int CalaculatePenaltyOccasion(int cote){
        double coefRegulator = 0.05;
        return (int) Math.round( cote * coefRegulator);
    }

    private boolean IsTransform(Player player){
        boolean isTransform = false;
        double regulatorCoef = 0.75;
        if (!player.isCanKick()) {
            regulatorCoef = 0.1;
        }
        double rate = player.getOverall() * regulatorCoef ;
        int randomInt = RANDOM.nextInt(0,100);
        if (randomInt <= rate) {
            isTransform = true;
        }
        return isTransform;
    }

    private int TryEfficacity(Team team) {
        double backsPlayerCoef = 0.5;
        double forwardsPlayerCoef = 0.25; 
        double generalNoteCoef = 0.25;  
        double coefRegulator = 0.7;
        int backs = team.GetBacksNote();      // 9 à 15
        int forwards = team.GetForwardsNote(); // 1 à 8
        int general = team.GetGeneralNote();

        double efficacity = 
                backs * backsPlayerCoef +      // les arrières concrétisent beaucoup
                forwards * forwardsPlayerCoef +   // les avants créent/finissent aussi
                general * generalNoteCoef;     // niveau global de l’équipe

        return (int) Math.round(efficacity * coefRegulator);
    }

    private boolean IsScored(int efficacity){
        int randomInt = RANDOM.nextInt(0,100);
        return (randomInt <= efficacity); 
    }

    private int GetBonusPoint(int scoreMyTeam , int scoreAdverseTeam , int nbTry){
        int bonusDefMin = 5;
        int bonusDefPoint = 1;
        int bonusOffPoint = 1;
        int bonusOffTryNb = 1;
        int winPoint = 4;
        int egalityPoint =1; 
        int bonus = 0;
        if (nbTry >= bonusOffTryNb ){
            bonus += bonusOffPoint;
        }
        if (scoreMyTeam > scoreAdverseTeam){
            bonus += winPoint;
        }
        else if (scoreAdverseTeam > scoreMyTeam) {
            if ((scoreAdverseTeam - scoreMyTeam) <= bonusDefMin) {
                bonus += bonusDefPoint;
            }
        }
        else{
            bonus  += egalityPoint;
        }
        return bonus;
    }


    private MatchResult SimulateMatch(Team myTeam, Team adverseTeam, Player mykicker) {
        return SimulateMatch(myTeam, adverseTeam, mykicker, 1.0);
    }

    private MatchResult SimulateMatch(Team myTeam, Team adverseTeam, Player mykicker, double opportunityFactor) {
            Player adverseKicker = GetAdverseKicker(adverseTeam);
            ArrayList<MatchEvent> events = new ArrayList<>();

            int tryPoint = 5;
            int transformPoint = 2;
            int penalitePoint = 3;
            int dropPoint = 3;
            int nbTryMyTeam = 0;
            int nbTryAdverseTeam = 0;
            int scoreMyTeam = 0;
            int scoreAdverseTeam = 0;

            int coteMyTeam = CalculatemyTeamCote(myTeam, adverseTeam);
            int coteAdverseTeam = CalculateAdverseTeamCote(coteMyTeam);

            int nbMyTeamTryOccasion = (int) Math.round(CalculateTryOccasion(coteMyTeam) * opportunityFactor);
            int nbAdverseTeamTryOccasion = (int) Math.round(CalculateTryOccasion(coteAdverseTeam) * opportunityFactor);
            int nbMyTeamPenaltyOccasion = (int) Math.round(CalaculatePenaltyOccasion(coteMyTeam) * opportunityFactor);
            int nbAdverseTeamPenaltyOccasion = (int) Math.round(CalaculatePenaltyOccasion(coteAdverseTeam) * opportunityFactor);

            int adverseTeamEfficacity = TryEfficacity(adverseTeam);
            int myTeamEfficacity = TryEfficacity(myTeam);

            while (nbAdverseTeamTryOccasion > 0 || nbMyTeamTryOccasion > 0 || nbMyTeamPenaltyOccasion > 0 || nbAdverseTeamPenaltyOccasion > 0) {
                if (nbMyTeamTryOccasion > 0) {
                    if (IsScored(myTeamEfficacity)) {
                        scoreMyTeam += tryPoint;
                        nbTryMyTeam += 1;
                        events.add(new MatchEvent(EventType.ESSAI, WhoScoredTry(myTeam), tryPoint, scoreMyTeam, scoreAdverseTeam, true));

                        if (IsTransform(mykicker)) {
                            scoreMyTeam += transformPoint;
                            events.add(new MatchEvent(EventType.TRANSFORMATION, mykicker, transformPoint, scoreMyTeam, scoreAdverseTeam, true));
                        }
                    }
                    nbMyTeamTryOccasion -= 1;
                }

                if (nbAdverseTeamTryOccasion > 0) {
                    if (IsScored(adverseTeamEfficacity)) {
                        nbTryAdverseTeam += 1;
                        scoreAdverseTeam += tryPoint;
                        events.add(new MatchEvent(EventType.ESSAI, WhoScoredTry(adverseTeam), tryPoint, scoreMyTeam, scoreAdverseTeam, false));

                        if (IsTransform(adverseKicker)) {
                            scoreAdverseTeam += transformPoint;
                            events.add(new MatchEvent(EventType.TRANSFORMATION, adverseKicker, transformPoint, scoreMyTeam, scoreAdverseTeam, false));
                        }
                    }
                    nbAdverseTeamTryOccasion -= 1;
                }

                if (nbMyTeamPenaltyOccasion > 0) {
                    if (IsTransform(mykicker)) {
                        scoreMyTeam += penalitePoint;
                        events.add(new MatchEvent(EventType.PENALITE, mykicker, penalitePoint, scoreMyTeam, scoreAdverseTeam, true));
                    }
                    nbMyTeamPenaltyOccasion -= 1;
                }

                if (nbAdverseTeamPenaltyOccasion > 0) {
                    if (IsTransform(adverseKicker)) {
                        scoreAdverseTeam += penalitePoint;
                        events.add(new MatchEvent(EventType.PENALITE, adverseKicker, penalitePoint, scoreMyTeam, scoreAdverseTeam, false));
                    }
                    nbAdverseTeamPenaltyOccasion -= 1;
                }
            }

            Team dropTeam = WhoTryDrop(myTeam, adverseTeam, scoreMyTeam, scoreAdverseTeam, dropPoint, coteAdverseTeam, coteMyTeam);

            if (dropTeam != null) {
                if (dropTeam.equals(myTeam)) {
                    if (TryDrop(mykicker)) {
                        scoreMyTeam += dropPoint;
                        events.add(new MatchEvent(EventType.DROP, mykicker, dropPoint, scoreMyTeam, scoreAdverseTeam, true));
                    }
                } else {
                    if (TryDrop(adverseKicker)) {
                        scoreAdverseTeam += dropPoint;
                        events.add(new MatchEvent(EventType.DROP, adverseKicker, dropPoint, scoreMyTeam, scoreAdverseTeam, false));
                    }
                }
            }

            int bonusPoint = GetBonusPoint(scoreMyTeam, scoreAdverseTeam, nbTryMyTeam);
            return new MatchResult(myTeam, adverseTeam, scoreMyTeam, scoreAdverseTeam, bonusPoint, events);
        }

    private Player WhoScoredTry(Team team) {

        ArrayList<Player> playersPool = new ArrayList<>();

        for (Player player : team.getPlayers()) {

            int tickets = 1;

            switch (player.getPosition()) {
                case Ailier:
                    tickets = 25;
                    break;

                case Centre:
                    tickets = 20;
                    break;

                case Arriere:
                    tickets = 15;
                    break;

                case DemiOuverture:
                    tickets = 10;
                    break;

                case DemiDeMelee:
                    tickets = 8;
                    break;

                case TroisiemeLigneCentre:
                    tickets = 8;
                    break;

                case TroisiemeLigneAile:
                    tickets = 5;
                    break;

                case Talonneur:
                    tickets = 4;
                    break;

                case DeuxiemeLigne:
                    tickets = 3;
                    break;

                case Pilier:
                    tickets = 2;
                    break;
            }

            for (int i = 0; i < tickets; i++) {
                playersPool.add(player);
            }
        }

    return playersPool.get(RANDOM.nextInt(playersPool.size()));
}

    private boolean TryDrop(Player player){
        boolean isTransform = false;
        double dropRegulatorCoef = 0.5;
        if (!player.isCanKick()) {
            dropRegulatorCoef = 0.1;
        }
        double rate = player.getOverall() * dropRegulatorCoef ;
        int randomInt = RANDOM.nextInt(0,100);
        if (randomInt <= rate) {
            isTransform = true;
        }
        return isTransform;
    }

    private Player GetAdverseKicker(Team adverseTeam){
        Player kicPlayer = null;
        Position kickerPosition = Position.DemiOuverture;
        for (Player player : adverseTeam.getPlayers()) {
            if (player.getPosition().equals(kickerPosition)) {
                kicPlayer = player;
            }
        }
        return kicPlayer;
    }

    private Team WhoTryDrop(Team myTeam , Team adverseTeam , int scoreMyTeam , int scoreAdverseTeam , int dropPoint , int coteAdverseTeam , int coteMyTeam){
        Team team = null;
        if (scoreAdverseTeam > scoreMyTeam && (scoreAdverseTeam - scoreMyTeam) < dropPoint){
            team = myTeam;
        }
        else if (scoreMyTeam > scoreAdverseTeam && (scoreMyTeam - scoreAdverseTeam) < dropPoint){
            team = adverseTeam;
        }
        else if (scoreAdverseTeam == scoreMyTeam){
            int randomAdverse = RANDOM.nextInt(0,coteAdverseTeam);
            int randomInt = RANDOM.nextInt(0,coteMyTeam);
            if (randomAdverse > randomInt) {
                team = adverseTeam;
            }
            else {
                team = myTeam;
            }
        }
        return team;
    }
    private MatchResult simulateExtraTime(
            Team myTeam,
            Team adverseTeam,
            Player kicker
    ) {
        double extraTimeFactor = 0.25;

        return SimulateMatch(
                myTeam,
                adverseTeam,
                kicker,
                extraTimeFactor
        );
    }

    private MatchResult simulatePenaltyShootout(Team myTeam, Team adverseTeam, Player myKicker, int scoreMyTeam, int scoreAdverseTeam, ArrayList<MatchEvent> events) {
        int penaltyShootoutKicks = 5;
        Player adverseKicker = GetAdverseKicker(adverseTeam);

        int mySuccessfulKicks = 0;
        int adverseSuccessfulKicks = 0;

        for (int i = 0; i < penaltyShootoutKicks; i++) {
            if (isKickSuccessful(myKicker)) mySuccessfulKicks++;
            if (isKickSuccessful(adverseKicker)) adverseSuccessfulKicks++;
        }

        while (mySuccessfulKicks == adverseSuccessfulKicks) {
            boolean myKick = isKickSuccessful(myKicker);
            boolean adverseKick = isKickSuccessful(adverseKicker);

            if (myKick && !adverseKick) mySuccessfulKicks++;
            else if (!myKick && adverseKick) adverseSuccessfulKicks++;
        }

        int nbTryMyTeam = 0;
        for (MatchEvent event : events) {
            if (event.getType() == EventType.ESSAI && event.isMyTeamEvent()) nbTryMyTeam++;
        }

        int bonusPoint = GetBonusPoint(scoreMyTeam, scoreAdverseTeam, nbTryMyTeam);

        MatchResult result = new MatchResult(myTeam, adverseTeam, scoreMyTeam, scoreAdverseTeam, bonusPoint, events);
        result.setVictory(mySuccessfulKicks > adverseSuccessfulKicks);

        return result;
    }

    private MatchResult simulateFinalsMatch(Team myTeam, Team adverseTeam, Player kicker) {
        MatchResult result = SimulateMatch(myTeam, adverseTeam, kicker);

        if (result.getScoreMyTeam() != result.getScoreAdverseTeam()) {
            return result;
        }

        MatchResult extraTimeResult = simulateExtraTime(myTeam, adverseTeam, kicker);

        int finalMyScore = result.getScoreMyTeam() + extraTimeResult.getScoreMyTeam();
        int finalAdverseScore = result.getScoreAdverseTeam() + extraTimeResult.getScoreAdverseTeam();

        ArrayList<MatchEvent> events = new ArrayList<>(result.getEvents());

        for (MatchEvent event : extraTimeResult.getEvents()) {
            events.add(new MatchEvent(
                    event.getType(),
                    event.getPlayer(),
                    event.getPoints(),
                    result.getScoreMyTeam() + event.getScoreMyTeam(),
                    result.getScoreAdverseTeam() + event.getScoreAdverseTeam(),
                    event.isMyTeamEvent()
            ));
        }

        if (finalMyScore != finalAdverseScore) {
            int nbTryMyTeam = 0;

            for (MatchEvent event : events) {
                if (event.getType() == EventType.ESSAI && event.isMyTeamEvent()) {
                    nbTryMyTeam++;
                }
            }

            int bonusPoint = GetBonusPoint(finalMyScore, finalAdverseScore, nbTryMyTeam);

            return new MatchResult(myTeam, adverseTeam, finalMyScore, finalAdverseScore, bonusPoint, events);
        }

        return simulatePenaltyShootout(myTeam, adverseTeam, kicker, finalMyScore, finalAdverseScore, events);
}

    private boolean isKickSuccessful(Player kicker) {

        if (kicker == null) {
            return Math.random() < 0.5;
        }

        double probability =
                0.45 + (kicker.getOverall() / 200.0);

        probability =
                Math.max(0.50, Math.min(0.95, probability));

        return Math.random() < probability;
    }

    private void updatePoolStandings(
            MatchResult match,
            PoolStanding first,
            PoolStanding second
    ) {
        int firstTries = 0;
        int secondTries = 0;

        for (MatchEvent event : match.getEvents()) {
            if (event.getType() == EventType.ESSAI) {
                if (event.isMyTeamEvent()) {
                    firstTries++;
                } else {
                    secondTries++;
                }
            }
        }

        int firstScore = match.getScoreMyTeam();
        int secondScore = match.getScoreAdverseTeam();

        // Même barème pour les deux équipes.
        int firstPoints = GetBonusPoint(
                firstScore, secondScore, firstTries
        );

        int secondPoints = GetBonusPoint(
                secondScore, firstScore, secondTries
        );

        first.addResult(firstScore, secondScore, firstPoints);
        second.addResult(secondScore, firstScore, secondPoints);
    }

    private Team CreateBestAdverseTeam(Team randomTeam) {

        ArrayList<Player> selectedPlayers = new ArrayList<>();

        HashMap<Position, Integer> neededPositions = new HashMap<>();

        neededPositions.put(Position.Pilier, 2);
        neededPositions.put(Position.Talonneur, 1);
        neededPositions.put(Position.DeuxiemeLigne, 2);
        neededPositions.put(Position.TroisiemeLigneAile, 2);
        neededPositions.put(Position.TroisiemeLigneCentre, 1);
        neededPositions.put(Position.DemiDeMelee, 1);
        neededPositions.put(Position.DemiOuverture, 1);
        neededPositions.put(Position.Centre, 2);
        neededPositions.put(Position.Ailier, 2);
        neededPositions.put(Position.Arriere, 1);

        for (Position position : neededPositions.keySet()) {

            int numberNeeded = neededPositions.get(position);

            ArrayList<Player> playersAtPosition = new ArrayList<>();

            // récupère les joueurs du poste
            for (Player player : randomTeam.getPlayers()) {
                if (player.getPosition() == position) {
                    playersAtPosition.add(player);
                }
            }

            // trie du meilleur au pire
            playersAtPosition.sort((p1, p2) -> 
                Integer.compare(p2.getOverall(), p1.getOverall())
            );

            // prend les meilleurs
            for (int i = 0; i < numberNeeded && i < playersAtPosition.size(); i++) {
                selectedPlayers.add(playersAtPosition.get(i));
            }
        }

        return new Team(
            randomTeam.getCountry(),
            randomTeam.getYear(),
            selectedPlayers
        );
    }


    public static TournamentResult PlayTournament(
            YourTeam myTeam,
            ArrayList<Team> teams
    ) {
        Simulation sim = new Simulation();

        ArrayList<MatchResult> matches = new ArrayList<>();
        ArrayList<MatchResult> poolMatches = new ArrayList<>();
        ArrayList<PoolStanding> standings = new ArrayList<>();

        // 1. Tirer quatre adversaires différents.
        // On mélange une copie pour ne pas modifier la liste du jeu.
        ArrayList<Team> candidates = new ArrayList<>(teams);
        Collections.shuffle(candidates, RANDOM);

        ArrayList<Team> opponents = new ArrayList<>();
        Set<String> selectedTeams = new HashSet<>();

        for (Team candidate : candidates) {
            String key = candidate.getCountry() + ":" + candidate.getYear();

            if (!selectedTeams.add(key)) {
                continue;
            }

            Team opponent = sim.CreateBestAdverseTeam(candidate);

            // Une équipe doit pouvoir aligner un XV complet.
            if (opponent.getPlayers().size() != 15) {
                continue;
            }

            opponents.add(opponent);

            if (opponents.size() == 4) {
                break;
            }
        }

        if (opponents.size() < 4) {
            throw new IllegalStateException(
                    "Il faut au moins quatre équipes adverses complètes."
            );
        }

        // 2. Créer les cinq lignes du classement.
        PoolStanding playerStanding = new PoolStanding(myTeam.team, true);
        standings.add(playerStanding);

        for (Team opponent : opponents) {
            standings.add(new PoolStanding(opponent, false));
        }

        // 3. Jouer les quatre matchs du joueur.
        for (int i = 0; i < opponents.size(); i++) {
            Team opponent = opponents.get(i);

            MatchResult match = sim.SimulateMatch(
                    myTeam.team,
                    opponent,
                    myTeam.kicker
            );

            match.setMatchName(MatchName.POULE, i + 1);

            matches.add(match);
            poolMatches.add(match);

            sim.updatePoolStandings(
                    match,
                    playerStanding,
                    standings.get(i + 1)
            );
        }

        // 4. Jouer les six rencontres entre les quatre adversaires.
        for (int i = 0; i < opponents.size(); i++) {
            for (int j = i + 1; j < opponents.size(); j++) {
                Team firstTeam = opponents.get(i);
                Team secondTeam = opponents.get(j);

                MatchResult match = sim.SimulateMatch(
                        firstTeam,
                        secondTeam,
                        sim.GetAdverseKicker(firstTeam)
                );

                match.setMatchName(
                        MatchName.POULE,
                        poolMatches.size() + 1
                );

                poolMatches.add(match);

                sim.updatePoolStandings(
                        match,
                        standings.get(i + 1),
                        standings.get(j + 1)
                );
            }
        }

        // 5. Classer les équipes :
        // points, différence de score, puis points marqués.
        //
        // En cas d'égalité parfaite, l'ordre aléatoire initial
        // départage les équipes sans favoriser le joueur.
        Collections.shuffle(standings, RANDOM);

        standings.sort(
                Comparator.comparingInt(PoolStanding::getPoints)
                        .reversed()
                        .thenComparing(
                                Comparator.comparingInt(
                                        PoolStanding::getPointsDifference
                                ).reversed()
                        )
                        .thenComparing(
                                Comparator.comparingInt(
                                        PoolStanding::getPointsFor
                                ).reversed()
                        )
        );

        // 6. Les deux premiers se qualifient.
        boolean qualified = standings.indexOf(playerStanding) < 2;
        boolean champion = false;

        // Statistiques du joueur.
        int points = playerStanding.getPoints();
        int wins = playerStanding.getWins();
        int losses = playerStanding.getLosses();

        // 7. Phases finales : fonctionnement actuel conservé.
        MatchName[] finalRounds = {
                MatchName.QUART_DE_FINALE,
                MatchName.DEMI_FINALE,
                MatchName.FINALE
        };

        if (qualified) {
            for (MatchName round : finalRounds) {
                Team opponent = sim.CreateBestAdverseTeam(
                        YourTeam.GetRandomTeam(teams)
                );

                MatchResult match = sim.simulateFinalsMatch(
                        myTeam.team,
                        opponent,
                        myTeam.kicker
                );

                match.setMatchName(round);
                matches.add(match);

                // On conserve le cumul historique du bilan actuel.
                // Le classement de poule, lui, ne change plus.
                points += match.getBonusPoints();

                if (!match.isVictory()) {
                    losses++;
                    break;
                }

                wins++;

                if (round == MatchName.FINALE) {
                    champion = true;
                }
            }
        }

        TournamentResult result = new TournamentResult(
                qualified,
                champion,
                points,
                wins,
                losses,
                matches
        );

        result.setPoolStandings(standings);
        result.setPoolMatches(poolMatches);

        return result;
    }



}
