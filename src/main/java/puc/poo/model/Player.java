package puc.poo.model;

import java.util.ArrayList;
import java.util.Stack;

/**
 * A classe Player é estruturada para gerenciar o estado do jogador
 * em termos de localização no cenário, inventário de objetos e rastreamento dos cenários anteriores.
 * Os métodos incluem adicionar, remover e acessar objetos no inventário,
 * bem como alterar e verificar o cenário atual e os cenários anteriores.
 */
public class Player {

    private Scenario currentScenario;
    private final ArrayList<GameObject> inventory;
    private final Stack<Scenario> previousScenarios;

    /**
     * Construtor para inicializar o inventário e os cenários anteriores que o jogador passou.
     */
    public Player() {
        inventory = new ArrayList<>();
        previousScenarios = new Stack<>();
    }

    /**
     * Retorna o cenário atual do jogador.
     *
     * @return O cenário atual.
     */
    public Scenario getCurrentScenario() {
        return currentScenario;
    }

    /**
     * Define o cenário atual do jogador.
     * Se já houver um cenário atual, ele será adicionado à pilha de cenários anteriores.
     *
     * @param scenario O novo cenário atual.
     */
    public void setCurrentScenario(Scenario scenario) {
        if (currentScenario != null) {
            previousScenarios.push(currentScenario);
        }
        this.currentScenario = scenario;
    }

    /**
     * Adiciona um objeto ao inventário do jogador.
     *
     * @param object O objeto a ser adicionado.
     */
    public void addToInventory(GameObject object) {
        inventory.add(object);
    }

    /**
     * Remove um objeto do inventário do jogador com base no nome.
     *
     * @param name O nome do objeto a ser removido.
     */
    public void removeFromInventory(String name) {
        inventory.removeIf(obj -> obj.getName().toLowerCase().contains(name.toLowerCase()));
    }

    /**
     * Remove um objeto específico do inventário do jogador.
     *
     * @param object O objeto a ser removido.
     */
    public void removeFromInventory(GameObject object) {
        inventory.remove(object);
    }

    /**
     * Retorna um objeto do inventário com base no nome.
     *
     * @param name O nome do objeto a ser procurado.
     * @return O objeto correspondente ou null se não encontrado.
     */
    public GameObject getFromInventory(String name) {
        name = name.toLowerCase();
        for (GameObject obj : inventory) {
            if (obj.getName().contains(name)) {
                return obj;
            }
        }
        return null;
    }

    /**
     * Verifica se há um cenário anterior.
     *
     * @return Verdadeiro se houver um cenário anterior, falso caso contrário.
     */
    public boolean hasPreviousScenario() {
        return !previousScenarios.isEmpty();
    }

    /**
     * Retorna o cenário anterior do jogador.
     * Remove o cenário anterior da pilha e o retorna.
     *
     * @return O cenário anterior.
     */
    public Scenario getPreviousScenario() {
        return previousScenarios.pop();
    }

    /**
     * Retorna a lista de GameObjects que estão no inventário do jogador.
     *
     * @return A lista de objetos no inventário.
     */
    public ArrayList<GameObject> getInventory() {
        return inventory;
    }
}