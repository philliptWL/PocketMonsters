package org.example;

import java.util.ArrayList;

public class PocketMonsters{
    private String pocketMonster;
    private int HP;
    private final String Type;
    private final ArrayList <Moves> Moveset;

    public PocketMonsters(String pocketMonster, int HP, String Type, ArrayList<Moves> Moveset){
        this.pocketMonster = pocketMonster;
        this.HP = HP;
        this.Type = Type;
        this.Moveset = Moveset;
    }

    public String getPocketMonster() {
        return pocketMonster;
    }

    public void setPocketMonster(String pocketMonster) {
        this.pocketMonster = pocketMonster;
    }

    public int getHP() {
        return HP;
    }

    public void setHP(int HP) {
        this.HP = HP;
    }

    public ArrayList<Moves> getMoveset() {
        return Moveset;
    }

    public String getType() {
        return Type;
    }
}
