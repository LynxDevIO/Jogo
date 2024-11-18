package puc.poo.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Classe responsável pela criação dos cenários de um jogo.
 * <p>
 * O nome {@link Scenario#name} de qualquer cenário deve ser <b>único</b>.
 * </p>
 *
 * <p>
 * Métodos desta classe:
 * </p>
 * <ul>
 * <li>{@code Scenario.addObject(GameObject object)} - adiciona um objeto do jogo ao cenário</li>
 * <li>{@code Scenario.addExit(String direction, Scenario scenario)} - adiciona uma saída ao cenário</li>
 * <li>{@code Scenario.removeObjectByName(String name)} - remove um objeto do jogo pelo seu nome</li>
 * <li>{@code Scenario.removeObject(GameObject object)} - remove um objeto do jogo pelo seu objeto</li>
 * <li>{@code Scenario.getObject(String name)} - obtém um objeto do jogo no cenário</li>
 * <li>{@code Scenario.getDescription()} - obtém a descrição do cenário</li>
 * <li>{@code Scenario.getName()} - obtém o nome do cenário</li>
 * <li>{@code Scenario.getExit(String direction)} - obtém a saída do cenário a partir da direção fornecida</li>
 * </ul>
 */
public class Scenario {

    private final String description;
    private final String imagePath;
    private final Map<String, GameObject> objects;
    private final String name;
    private final Map<String, Scenario> exits = new HashMap<>(); // p/ guardar as saídas de cada cenário

    /**
     * Construtor para criar um novo cenário.
     *
     * @param name O nome do cenário. Deve ser único.
     * @param description A descrição do cenário.
     * @param imagePath O caminho da imagem representativa do cenário.
     */
    public Scenario(String name, String description, String imagePath) {
        this.name = name;
        this.description = description;
        this.imagePath = imagePath;
        this.objects = new HashMap<>();
    }

    /**
     * Adiciona um objeto ao cenário.
     *
     * @param object O objeto do jogo a ser adicionado.
     */
    public void addObject(GameObject object) {
        objects.put(object.getName(), object);
    }

    /**
     * Remove um objeto do cenário pelo nome.
     *
     * @param name O nome do objeto a ser removido.
     */
    public void removeObjectByName(String name) {
        name = name.toLowerCase();
        for (GameObject obj : objects.values()) {
            if (obj.getName().contains(name)) {
                objects.remove(obj);
            }
        }
    }

    /**
     * Remove um objeto do cenário pelo objeto.
     *
     * @param object O objeto do jogo a ser removido.
     */
    public void removeObject(GameObject object) {
        objects.entrySet()
                .stream()
                .filter(entry -> entry.getValue().equals(object))
                .map(Map.Entry::getKey)
                .findFirst()
                .ifPresent(objects::remove);
    }

    /**
     * Obtém um objeto do cenário pelo nome.
     *
     * @param name O nome do objeto a ser obtido.
     * @return O objeto do jogo, ou {@code null} se não encontrado.
     */
    public GameObject getObject(String name) {
        name = name.toLowerCase();
        for (GameObject obj : objects.values()) {
            if (obj.getName().contains(name)) {
                return obj;
            }
        }
        return null;
    }

    /**
     * Obtém a descrição do cenário.
     *
     * @return A descrição do cenário, incluindo os objetos presentes.
     */
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

    /**
     * Obtém o caminho da imagem do cenário.
     *
     * @return O caminho da imagem do cenário.
     */
    public String getImagePath() {
        return imagePath;
    }

    /**
     * Obtém o nome do cenário.
     *
     * @return O nome do cenário.
     */
    public String getName() {
        return name;
    }

    /**
     * Adiciona uma saída para outro cenário.
     *
     * @param direction A direção da saída (por exemplo, "norte", "sul").
     * @param scenario O cenário para o qual a saída conduz.
     */
    public void addExit(String direction, Scenario scenario) {
        exits.put(direction, scenario);
    }

    /**
     * Obtém uma saída do cenário com base na direção fornecida.
     *
     * @param direction A direção da saída (por exemplo, "norte", "sul").
     * @return O cenário correspondente à saída naquela direção.
     */
    public Scenario getExit(String direction) {
        return exits.get(direction);
    }
}