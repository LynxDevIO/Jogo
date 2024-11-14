package puc.poo.controller;

import puc.poo.model.Dove;
import puc.poo.model.GameObject;
import puc.poo.model.Player;

public class DoveSpotter implements Runnable {
    private Dove dove;
    private Player player;

    public DoveSpotter(Dove dove, Player player) {
        this.dove = dove;
        this.player = player;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Thread.sleep(10000); // 10 segundos
                if (dove.isAlive() && player.getCurrentScenario().getName().equals("Floresta")) {
                    GameObject huntingRifle = player.getFromInventory("rifle");
                    if (huntingRifle != null) {
                        System.out.println("(PERCEPÇÃO) Há um veado na proximidade.");
                    }
                }
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
