package puc.poo.model;

import java.util.Random;

/**
 * Classe Stag representa um NPC (personagem não jogável) veado no jogo.
 * A classe gerencia o estado de vida do veado e se os chifres foram coletados.
 */
public class Stag {
    private final String nome = "veado";
    private boolean alive; // Indica se o veado está vivo.
    private boolean antlersCollected; // Indica se os chifres foram coletados.

    /**
     * Construtor padrão que inicializa o estado do veado como vivo e chifres não coletados.
     */
    public Stag() {
        alive = true;
        antlersCollected = false;
    }

    /**
     * Verifica se o veado está vivo.
     *
     * @return Verdadeiro se o veado estiver vivo, falso caso contrário.
     */
    public boolean isAlive() {
        return alive;
    }

    /**
     * Define o estado de vida do veado.
     *
     * @param alive Verdadeiro para definir o veado como vivo, falso caso contrário.
     */
    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    /**
     * Verifica se os chifres do veado foram coletados.
     *
     * @return Verdadeiro se os chifres foram coletados, falso caso contrário.
     */
    public boolean areAntlersCollected() {
        return antlersCollected;
    }

    /**
     * Marca os chifres do veado como coletados.
     */
    public void collectAntlers() {
        this.antlersCollected = true;
    }

    /**
     * Tenta matar o veado com uma chance de 60%.
     *
     * @return Verdadeiro se o veado for morto, falso caso contrário.
     */
    public boolean tryToKill() {
        Random random = new Random();
        // 60% de chance de morrer
        return random.nextDouble() < 0.6;
    }
}