package puc.poo.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/// Classe responsável pela criação dos cenários.
/// <p>O nome {@link Scenario#name} de qualquer cenário deve ser <b>único</b>.</p>
/// <p>Métodos desta classe:</p>
/// <p>{@code Scenario.addObject} -> adiciona objeto do jogo ao cenário</p>
/// <p>{@code Scenario.addExit} -> adiciona saída ao cenário
/// <p>{@code Scenario.removeObjectByName} -> remove objeto do jogo pelo seu nome
/// <p>{@code Scenario.removeObject} -> remove objeto do jogo pelo seu objeto
/// <p>{@code Scenario.getObject} -> obtém objeto do jogo no cenário
/// <p>{@code Scenario.getDescription} -> obtém descrição do cenário
/// <p>{@code Scenario.getName} -> obtém o nome do cenário
/// <p>{@code Scenario.getExit} -> obtém a saída do cenário a partir da direção fornecida
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

    public void removeObjectByName(String name) {
        name = name.toLowerCase();
        for (GameObject obj : objects.values()) {
            if (obj.getName().contains(name)) {
                objects.remove(obj);
            }
        }
    }

    public void removeObject(GameObject object) {
        objects.entrySet()
                .stream()
                .filter(entry -> entry.getValue().equals(object))
                .map(Map.Entry::getKey)
                .findFirst()
                .ifPresent(objects::remove);
    }

    public GameObject getObject(String name) {
        name = name.toLowerCase();
        for (GameObject obj : objects.values()) {
            if (obj.getName().contains(name)) {
                return obj;
            }
        }
        return null;
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