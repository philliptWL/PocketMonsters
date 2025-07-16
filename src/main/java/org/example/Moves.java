package org.example;

public class Moves{
    String Moves;
    int Damage;
    double Accuracy;
    private final String Type;

    public Moves(String Moves, int Damage, double Accuracy, String Type){
        this.Moves = Moves;
        this.Damage = Damage;
        this.Accuracy = Accuracy;
        this.Type = Type;
    }

    public String getMoves() {
        return Moves;
    }

    public int getDamage() {
        return Damage;
    }

    public void setDamage(int damage) {
        Damage = damage;
    }

    public String getType() {
        return Type;
    }

    public double getAccuracy() {
        return Accuracy;
    }

}
