package com.xvdereve.model;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;



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
        if (!player.canKick) {
            regulatorCoef = 0.1;
        }
        double rate = player.overall * regulatorCoef ;
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

    private int GetBonusPoint(int scoreMyTeam , int scoreAdverseTeam){
        int bonusDefMin = 5;
        int bonusDefPoint = 1;
        int bonusOffMin = 15;
        int bonusOffPoint = 1;
        int winPoint = 4;
        int egalityPoint =1; 
        int bonus = 0;
        if (scoreMyTeam > scoreAdverseTeam){
            bonus += winPoint;
            if ((scoreMyTeam - scoreAdverseTeam) >= bonusOffMin) {
                bonus += bonusOffPoint;
            }
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



    private  MatchResult  SimulateMatch(Team myTeam , Team adverseTeam , Player mykicker){
        Player adverseKicker = GetAdverseKicker(adverseTeam) ;
        ArrayList<MatchEvent> events = new ArrayList<>();
        int tryPoint = 5;
        int transformPoint = 2;
        int penalitePoint = 3;
        int dropPoint = 3;
        int scoreMyTeam = 0;
        int scoreAdverseTeam = 0;
        int coteMyTeam = CalculatemyTeamCote(myTeam, adverseTeam);
        int coteAdverseTeam = CalculateAdverseTeamCote(coteMyTeam);
        int nbMyTeamTryOccasion = CalculateTryOccasion(coteMyTeam);
        int nbAdverseTeamTryOccasion =  CalculateTryOccasion(coteAdverseTeam);
        int nbMyTeamPenaltyOccasion =  CalaculatePenaltyOccasion(coteMyTeam);
        int nbAdverseTeamPenaltyOccasion =  CalaculatePenaltyOccasion(coteAdverseTeam);
        int adverseTeamEfficacity = TryEfficacity(adverseTeam);
        int myTeamEfficacity = TryEfficacity(myTeam);
        while ( nbAdverseTeamTryOccasion > 0 ||
            nbMyTeamTryOccasion > 0 ||
            nbMyTeamPenaltyOccasion > 0 ||
            nbAdverseTeamPenaltyOccasion > 0 ) {
            if (nbMyTeamTryOccasion > 0) {
                if ( IsScored(myTeamEfficacity)) {
                    scoreMyTeam += tryPoint;
                    events.add(new MatchEvent(EventType.ESSAI, WhoScoredTry(myTeam), tryPoint, scoreMyTeam, scoreAdverseTeam, true));
                    if ( IsTransform(mykicker)) {
                        scoreMyTeam += transformPoint;
                        events.add(new MatchEvent(EventType.TRANSFORMATION, mykicker, transformPoint, scoreMyTeam, scoreAdverseTeam, true));
                    }
                }
                nbMyTeamTryOccasion -= 1 ;    
            }
            if (nbAdverseTeamTryOccasion > 0) {
                if ( IsScored(adverseTeamEfficacity)) {
                    scoreAdverseTeam += tryPoint;
                    events.add(new MatchEvent(EventType.ESSAI, WhoScoredTry(adverseTeam), tryPoint, scoreMyTeam, scoreAdverseTeam, false));
                    if ( IsTransform(adverseKicker)) {
                        scoreAdverseTeam += transformPoint;
                        events.add(new MatchEvent(EventType.TRANSFORMATION, adverseKicker, transformPoint, scoreMyTeam, scoreAdverseTeam, false));
                    }
                }
                nbAdverseTeamTryOccasion -= 1 ;
            }
            if (nbMyTeamPenaltyOccasion > 0) {
                    if ( IsTransform(mykicker)) {
                        scoreMyTeam += penalitePoint;
                        events.add(new MatchEvent(EventType.PENALITE, mykicker, penalitePoint, scoreMyTeam, scoreAdverseTeam, true));
                    }
                nbMyTeamPenaltyOccasion -= 1 ;    
            }
            if (nbAdverseTeamPenaltyOccasion > 0) {

                    if ( IsTransform(adverseKicker)) {
                        scoreAdverseTeam += penalitePoint;
                        events.add(new MatchEvent(EventType.PENALITE, adverseKicker, penalitePoint, scoreMyTeam, scoreAdverseTeam, false));
                    }
                nbAdverseTeamPenaltyOccasion -= 1 ;
            }

        }
        Team dropTeam = WhoTryDrop(myTeam, adverseTeam, scoreMyTeam, scoreAdverseTeam, tryPoint, coteAdverseTeam, coteMyTeam);
        if (dropTeam != null) {
            if (dropTeam.equals(myTeam)) {
                if ( TryDrop(mykicker)) {
                    scoreMyTeam += dropPoint;
                    events.add(new MatchEvent(EventType.DROP, mykicker, dropPoint, scoreMyTeam, scoreAdverseTeam, true));
                }
            }else{
                    if ( TryDrop(adverseKicker)) {
                    scoreAdverseTeam += dropPoint;
                    events.add(new MatchEvent(EventType.DROP, adverseKicker, dropPoint, scoreMyTeam, scoreAdverseTeam, false));
                }
            }
            
        }
        

        int bonusPoint = GetBonusPoint(scoreMyTeam,scoreAdverseTeam);
        MatchResult res = new MatchResult(myTeam, adverseTeam , scoreMyTeam, scoreAdverseTeam, bonusPoint,events) ;
        return res;
    }

    private Player WhoScoredTry(Team team) {

        ArrayList<Player> playersPool = new ArrayList<>();

        for (Player player : team.players) {

            int tickets = 1;

            switch (player.position) {
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
        if (!player.canKick) {
            dropRegulatorCoef = 0.1;
        }
        double rate = player.overall * dropRegulatorCoef ;
        int randomInt = RANDOM.nextInt(0,100);
        if (randomInt <= rate) {
            isTransform = true;
        }
        return isTransform;
    }

    private Player GetAdverseKicker(Team adverseTeam){
        Player kicPlayer = null;
        Position kickerPosition = Position.DemiOuverture;
        for (Player player : adverseTeam.players) {
            if (player.position.equals(kickerPosition)) {
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
            for (Player player : randomTeam.players) {
                if (player.position == position) {
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
            randomTeam.country,
            randomTeam.year,
            selectedPlayers
        );
    }


    public static TournamentResult PlayTournament(YourTeam myTeam , ArrayList<Team> teams){
        boolean win = true;
        boolean qualified = false;
        boolean champion = false;
        ArrayList<MatchResult> matchs = new ArrayList<>();
        Simulation sim   = new  Simulation();
        ArrayList<MatchName> finalsName = new ArrayList<>();
        finalsName.add(MatchName.QUART_DE_FINALE);
        finalsName.add(MatchName.DEMI_FINALE);
        finalsName.add(MatchName.FINALE);
        int myScore = 0;
        int minScoreToGoInPlayOff = 14;
        int nbPlayOffGame = finalsName.size();
        int nbGroupGame = 5;
        int myWinRate = 0;
        int adverseWinRate = 0;
        for(int i = 0 ; i < nbGroupGame ; i++){
                Team randTeam = YourTeam.GetRandomTeam(teams);
                Team adverseteam =  sim.CreateBestAdverseTeam(randTeam);
                MatchResult res =  sim.SimulateMatch(myTeam.team, adverseteam,myTeam.kicker);
                res.setMatchName(MatchName.POULE,i+1);
                matchs.add(res);
                myScore += res.getBonusPoints();
                if (res.getScoreMyTeam() > res.getScoreAdverseTeam()) {
                    myWinRate += 1;
                }else if (res.getScoreAdverseTeam() > res.getScoreMyTeam()){
                    adverseWinRate += 1;
                }
                }

        if (myScore >= minScoreToGoInPlayOff) {
            int i = 0;
            qualified = true;
            while (win && nbPlayOffGame > 0) {
                
                Team adverseteam =  sim.CreateBestAdverseTeam(YourTeam.GetRandomTeam(teams));
                MatchResult res =  sim.SimulateMatch(myTeam.team, adverseteam,myTeam.kicker);
                res.setMatchName(finalsName.get(i));
                matchs.add(res);
                myScore += res.getBonusPoints();
                if (res.getScoreMyTeam() < res.getScoreAdverseTeam()){
                    win = false;
                    adverseWinRate += 1;
                }
                else if (res.getScoreAdverseTeam() < res.getScoreMyTeam()){
                    myWinRate += 1;
                }
                nbPlayOffGame -=1;
                i +=1;
            }
            if (nbPlayOffGame <= 0 && win) {
                champion = true;
            }
        } 
        TournamentResult tournamentResult = new TournamentResult(qualified, champion, myScore, myWinRate, adverseWinRate, matchs);
        return tournamentResult;
    }



}
