package puc.poo.model;

import java.util.ArrayList;

// TODO: concluir!

/// Classe responsável pela criação e gerenciamento de condições de objetos do jogo.
public class ObjectCondition {
    private ArrayList<GameObject> requiredObjects;
    private boolean isSatisfied;

    public ObjectCondition() {}

    public ArrayList<GameObject> getRequiredObjects() {
        return requiredObjects;
    }

    public void setRequiredObjects(ArrayList<GameObject> requiredObjects) {
        this.requiredObjects = requiredObjects;
    }

    public boolean isSatisfied() {
        return isSatisfied;
    }

    public void setSatisfied(boolean satisfied) {
        isSatisfied = satisfied;
    }

    public boolean checkObjListInventory (ArrayList<GameObject> inventory) {
        for (GameObject gameObject : requiredObjects) {
            if (!inventory.contains(gameObject)) {
                return false;
            }
        }
        return true;
    }
}
