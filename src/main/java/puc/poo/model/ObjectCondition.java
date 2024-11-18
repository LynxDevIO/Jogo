package puc.poo.model;

import java.util.ArrayList;

/**
 * Esta classe representa uma condição de objeto que verifica se determinados objetos
 * necessários estão presentes no inventário do jogador e se uma condição específica
 * no cenário do jogo é satisfeita.
 */
public class ObjectCondition {
    private ArrayList<Integer> requiredObjects;
    private boolean isSatisfied;

    /**
     * Construtor padrão para a classe ObjectCondition.
     */
    public ObjectCondition() {}

    /**
     * Retorna a lista de objetos necessários.
     *
     * @return Lista de IDs dos objetos necessários.
     */
    public ArrayList<Integer> getRequiredObjects() {
        return requiredObjects;
    }

    /**
     * Define a lista de objetos necessários.
     *
     * @param requiredObjects Lista de IDs dos objetos necessários.
     */
    public void setRequiredObjects(ArrayList<Integer> requiredObjects) {
        this.requiredObjects = requiredObjects;
    }

    /**
     * Verifica se a condição está satisfeita.
     *
     * @return Verdadeiro se a condição estiver satisfeita, falso caso contrário.
     */
    public boolean isSatisfied() {
        return isSatisfied;
    }

    /**
     * Define se a condição está satisfeita.
     *
     * @param satisfied Verdadeiro para configurar a condição como satisfeita, falso caso contrário.
     */
    public void setSatisfied(boolean satisfied) {
        isSatisfied = satisfied;
    }

    /**
     * Verifica se determinados objetos estão no inventário do jogador.
     * Se algum objeto necessário estiver presente, a condição é marcada como satisfeita.
     *
     * @param inventory Lista de objetos no inventário do jogador.
     */
    public void checkObjListInventory(ArrayList<GameObject> inventory) {
        for (Integer gameObjectID : requiredObjects) {
            if (inventory.stream().anyMatch(obj -> gameObjectID.equals(obj.getId()))) {
                isSatisfied = true;
            }
        }
    }

    /**
     * Verifica se a condição é satisfeita com base no cenário atual do jogador.
     *
     * @param player O jogador cujo cenário atual será verificado.
     * @return Verdadeiro se a condição for satisfeita, falso caso contrário.
     */
    public boolean checkCondition(Player player) {
        // Verifica se o jogador está no cenário da floresta
        return player.getCurrentScenario().getName().equals("Floresta");
    }
}