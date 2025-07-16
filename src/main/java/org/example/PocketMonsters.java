package org.example;

import java.util.ArrayList;

interface Monster{
    void MonsterDescription(String Description);
    void MonsterLevel(int Level);
}

public class PocketMonsters implements Monster{
    private String pocketMonster;
    private int HP;
    private final String Type;
    private final ArrayList <Moves> Moveset;
    private String Description;
    private int Level;

    public PocketMonsters(String pocketMonster, int HP, String Type, ArrayList<Moves> Moveset,String Description,int Level){
        this.pocketMonster = pocketMonster;
        this.HP = HP;
        this.Type = Type;
        this.Moveset = Moveset;
        MonsterDescription(Description);
        MonsterLevel(Level);
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

    public String getDescription(){
        return Description;
    }

    public int getLevel(){
        return Level;
    }

    @Override
    public void MonsterDescription(String Description) {
        this.Description = Description;
    }

    @Override
    public void MonsterLevel(int Level) {
        this.Level = Level;
    }
}
