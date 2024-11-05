package puc.poo.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Scenario implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final String description;
    private final String imagePath;
    private final Map<String, GameObject> objects;
    private final String name;
    private final Map<String, Scenario> exits = new HashMap<>(); // p/ guardar as saídas de cada cenário

    public Scenario(String name, String description, String imagePath) {
        this.name = name;
        this.description = description;
        this.imagePath = imagePath;
        this.objects = new HashMap<>();
    }

    public void addObject(GameObject object) {
        objects.put(object.getName(), object);
    }

    public void removeObject(String name) {
        objects.remove(name);
    }

    public GameObject getObject(String name) {
        return objects.get(name);
    }

    public String getDescription() {
        StringBuilder descriptionWithItems = new StringBuilder(description);
        if (!objects.isEmpty()) {
            descriptionWithItems.append("\nItens neste cenário:");
            for (GameObject obj : objects.values()) {
                descriptionWithItems.append("\n- ").append(obj.getName());
            }
        }
        return descriptionWithItems.toString();
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getName() {
        return name;
    }

    public void addExit(String direction, Scenario scenario) {
        exits.put(direction, scenario); // Adiciona a saída para o MAP atributo da classe
    }

    public Scenario getExit(String direction) {
        return exits.get(direction); // Obtém saída com base na direção
    }
} 