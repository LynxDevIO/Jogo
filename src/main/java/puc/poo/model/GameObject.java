package puc.poo.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GameObject implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final String name;
    private final String description;
    private final int x;
    private final int y;
    private boolean openable;
    private boolean open;
    private final boolean storable;
    private final List<GameObject> containedObjects; // Lista para guardar objetos contidos em outros.

    public GameObject(String name, String description, int x, int y, boolean storable) {
        this.name = name;
        this.description = description;
        this.x = x;
        this.y = y;
        this.openable = false; // Padrão: não abrível
        this.open = false; // Padrão: fechado
        this.storable = storable;
        this.containedObjects = new ArrayList<>(); // Inicializa a lista dos objetos contidos...
    }

    public GameObject(String name, String description, int x, int y, boolean openable, boolean storable) {
        this(name, description, x, y, storable);
        this.openable = openable;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description + (openable && open ? " (aberto)" : "");
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isOpenable() {
        return openable;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public boolean isStorable() {
        return storable;
    }

    public void addContainedObject(GameObject object) {
        containedObjects.add(object);
    }

    public List<GameObject> getContainedObjects() {
        return containedObjects;
    }

    public String interact(String action) {
        if (action.equals("pegar") && !containedObjects.isEmpty()) {
            GameObject item = containedObjects.removeFirst(); // Obtém o primeiro objeto contido.
            return "Você encontrou \"" + item.getName() + "\" dentro de \"" + name + "\"!";
        }
        return "Você não pode interagir com isso.";
    }
} 