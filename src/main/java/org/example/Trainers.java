package org.example;

import java.util.ArrayList;

public class Trainers{
    private final String trainer;
    private ArrayList<PocketMonsters> team;

    public Trainers(String trainer){
        this.trainer = trainer;
    }

    public String getTrainer() {
        return trainer;
    }

    public ArrayList<PocketMonsters> getTeam() {
        return team;
    }

    public void setTeam(ArrayList<PocketMonsters> team) {
        this.team = team;
    }
}
