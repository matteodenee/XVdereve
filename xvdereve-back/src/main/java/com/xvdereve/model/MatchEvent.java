package com.xvdereve.model;
public class MatchEvent {

    private EventType type;
    private Player player;
    private int points;
    private int scoreMyTeam;
    private int scoreAdverseTeam;
    private boolean myTeamEvent;

    public MatchEvent(
            EventType type,
            Player player,
            int points,
            int scoreMyTeam,
            int scoreAdverseTeam,
            boolean myTeamEvent
    ) {
        this.type = type;
        this.player = player;
        this.points = points;
        this.scoreMyTeam = scoreMyTeam;
        this.scoreAdverseTeam = scoreAdverseTeam;
        this.myTeamEvent = myTeamEvent;
    }

    public EventType getType() {
        return type;
    }

    public Player getPlayer() {
        return player;
    }

    public int getPoints() {
        return points;
    }

    public int getScoreMyTeam() {
        return scoreMyTeam;
    }

    public int getScoreAdverseTeam() {
        return scoreAdverseTeam;
    }

    public boolean isMyTeamEvent() {
        return myTeamEvent;
    }
}