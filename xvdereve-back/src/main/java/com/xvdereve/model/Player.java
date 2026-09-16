
package com.xvdereve.model;
public class Player {
    private static int nextId = 1;
    private int id;
    String name ;
    Position position;
    Country country;
    int year;
    int overall;
    boolean canKick;

    public Player(String name , Position position , Country country , int year , int overall , boolean canKick){
        this.id = nextId++;
        this.name = name;
        this.position = position;
        this.country = country;
        this.year =year;
        this.overall = overall;
        this.canKick = canKick;

    }


        public String getName() {
        return name;
    }

    public Position getPosition() {
        return position;
    }

    public Country getCountry() {
        return country;
    }

    public int getYear() {
        return year;
    }

    public int getOverall() {
        return overall;
    }

    public boolean isCanKick() {
        return canKick;
    }

    public int getId() {
    return id;
    }
    
    @Override
    public String toString() {
        String text =  name + " - " + position + " - " + overall;
        if (canKick) {
            text += " - " + "buteur";
        }
        return text;
    }

}
