package com.xvdereve.model;
import java.util.ArrayList;

public class MatchResult {

    private Team myTeam;
    private Team adverseTeam;

    private int scoreMyTeam;
    private int scoreAdverseTeam;

    private boolean victory;
    private int bonusPoints;

    private MatchName matchName;
    private int idMatch;

    private ArrayList<MatchEvent> events;


    public MatchResult(
            Team myTeam,
            Team adverseTeam,
            int scoreMyTeam,
            int scoreAdverseTeam,
            int bonusPoints,
            MatchName matchName,
            int idMatch,
            ArrayList<MatchEvent> events) {

        this.myTeam = myTeam;
        this.adverseTeam = adverseTeam;
        this.scoreMyTeam = scoreMyTeam;
        this.scoreAdverseTeam = scoreAdverseTeam;
        this.bonusPoints = bonusPoints;
        this.matchName = matchName;
        this.idMatch = idMatch;
        this.events = events;

        this.victory = scoreMyTeam > scoreAdverseTeam;
    }

    public MatchResult(
            Team myTeam,
            Team adverseTeam,
            int scoreMyTeam,
            int scoreAdverseTeam,
            int bonusPoints,
            ArrayList<MatchEvent> events) {

        this.myTeam = myTeam;
        this.adverseTeam = adverseTeam;
        this.scoreMyTeam = scoreMyTeam;
        this.scoreAdverseTeam = scoreAdverseTeam;
        this.bonusPoints = bonusPoints;
        this.matchName = null;
        this.idMatch = 0;
        this.events = events;

        this.victory = scoreMyTeam > scoreAdverseTeam;
    }

    public Team getMyTeam() {
        return myTeam;
    }

    public Team getAdverseTeam() {
        return adverseTeam;
    }

    public int getScoreMyTeam() {
        return scoreMyTeam;
    }

    public int getScoreAdverseTeam() {
        return scoreAdverseTeam;
    }

    public boolean isVictory() {
        return victory;
    }

    public int getBonusPoints() {
        return bonusPoints;
    }

    public MatchName getMatchName(){
        return matchName;
    }
    public int getidMatch(){
        return idMatch;
    }
    public ArrayList<MatchEvent> getEvents() {
        return events;
    }

    public void setMatchName(MatchName newMatchName , int newIdMatch){
        matchName = newMatchName;
        idMatch = newIdMatch;
    }
        public void setMatchName(MatchName newMatchName){
        matchName = newMatchName;
    }
}