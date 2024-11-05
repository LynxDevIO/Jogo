package puc.poo.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Player implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private Scenario currentScenario;
    private final List<GameObject> inventory;
    private final Stack<Scenario> previousScenarios; // PILHA para rastrear os cenários anteriores

    public Player() { // Inicializar o inventário e os cenários anteriores que o jogador passou
        inventory = new ArrayList<>();
        previousScenarios = new Stack<>();
    }

    public Scenario getCurrentScenario() {
        return currentScenario;
    }

    public void setCurrentScenario(Scenario scenario) {
        if (currentScenario != null) {
            previousScenarios.push(currentScenario); // Coloca o cenário atual na PILHA
        }
        this.currentScenario = scenario;
    }

    public void addToInventory(GameObject object) {
        inventory.add(object);
    }

    public void removeFromInventory(String name) {
        inventory.removeIf(obj -> obj.getName().equals(name));
    }

    public GameObject getFromInventory(String name) {
        for (GameObject obj : inventory) {
            if (obj.getName().equals(name)) {
                return obj;
            }
        }
        return null;
    }

    public boolean hasPreviousScenario() {
        return !previousScenarios.isEmpty(); // Checa se há um cenário anterior
    }

    public Scenario getPreviousScenario() {
        return previousScenarios.pop(); // Remove o cenário anterior da PILHA (e retorna esse cenário)
    }

    public List<GameObject> getInventory() {
        return inventory; // Retorna a lista de GameObjects que estão no inventário do jogador
    }
} 