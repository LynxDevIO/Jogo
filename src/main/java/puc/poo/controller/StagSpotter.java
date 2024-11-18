package puc.poo.controller;

import puc.poo.model.Stag;
import puc.poo.model.GameObject;
import puc.poo.model.Player;

/**
 * A classe StagSpotter monitora a presença de um veado (Stag) quando o jogador está na floresta
 * e possui um rifle no seu inventário. A classe implementa Runnable para permitir a sua execução em uma thread separada.
 */
public class StagSpotter implements Runnable {
    private Stag stag;
    private Player player;

    /**
     * Construtor para inicializar os objetos Stag e Player que serão monitorados.
     *
     * @param stag   O objeto Stag que representa o veado no jogo.
     * @param player O objeto Player que representa o jogador no jogo.
     */
    public StagSpotter(Stag stag, Player player) {
        this.stag = stag;
        this.player = player;
    }

    /**
     * Mét. run que será executado quando a thread iniciar. Verifica periodicamente (a cada 10 segundos)
     * se o veado está vivo e se o jogador está na floresta com um rifle no inventário.
     * Se todas as condições forem atendidas, exibe uma mensagem indicando a presença de um veado na proximidade.
     */
    @Override
    public void run() {
        try {
            while (true) {
                // Verifica se o veado está vivo e se o jogador está na floresta
                if (stag.isAlive() && player.getCurrentScenario().getName().equals("Floresta")) {
                    GameObject huntingRifle = player.getFromInventory("rifle");

                    // Verifica se o jogador possui um rifle no inventário
                    if (huntingRifle != null) {
                        System.out.println("(PERCEPÇÃO) Há um veado na proximidade.");
                    }
                }

                Thread.sleep(10000); // Espera por 10 segundos antes de verificar novamente
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}