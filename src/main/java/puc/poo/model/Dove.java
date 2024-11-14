package puc.poo.model;

import java.util.Random;

public class Dove {
    private final String nome = "veado";
    private boolean alive; // NPC vivo?

    public Dove() {
        alive = true;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    // Método para tentar matar o veado
    public boolean tryToKill() {
        Random random = new Random();
        // 60% de chance de morrer
        return random.nextDouble() < 0.6;
    }
}
