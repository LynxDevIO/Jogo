package puc.poo.model;

import java.util.ArrayList;

/** A classe GameObject é uma estrutura que pode representar uma variedade de objetos no jogo,
 * desde simples itens até recipientes que podem ser trancados e destrancados,
 * além de permitir a realização de ações definidas pelo desenvolvedor.
 */
public class GameObject {

    private static ArrayList<GameObject> allGameObjects = new ArrayList<>();

    private final int id;
    private String name;
    private String description;
    private boolean isStorage; // É armazenamento?
    private boolean isStorable; // Pode ser armazenado no inventário?
    private int keyId; // ID da chave para destravar, se aplicável
    private boolean isOpenable; // Objeto é abrível?
    private boolean isOpen; // Objeto está aberto/fechado?
    private boolean hasAction; // Objeto possui ação?
    private ObjectAction action; // Ação do objeto
    private ArrayList<GameObject> contents; // Conteúdo armazenado

    /**
     * Construtor genérico que inicializa um objeto com um nome e uma descrição.
     *
     * @param name        O nome do objeto.
     * @param description A descrição do objeto.
     */
    public GameObject(String name, String description) {
        allGameObjects.add(this);
        this.id = allGameObjects.size();
        this.name = name;
        this.description = description;
    }

    /**
     * Marca o objeto como armazenável no inventário.
     */
    public void setAsStorable() {
        this.isStorable = true;
    }

    /**
     * Configura o objeto como um armazenamento que pode conter outros objetos.
     *
     * @param storable Indica se o objeto é um armazenamento.
     */
    public void setAsStorage(boolean storable) {
        this.isStorage = storable;
        if (storable) {
            this.contents = new ArrayList<>();
        } else {
            this.contents = null;
        }
    }

    /**
     * Configura a tranca do objeto e se ele é interativo.
     *
     * @param keyId  ID da chave necessária para destrancar o objeto.
     * @param isOpen Indica se o objeto está aberto.
     */
    public void setLock(int keyId, boolean isOpen) {
        this.keyId = keyId;
        this.isOpen = isOpen;
    }

    /**
     * Define uma ação para o objeto.
     *
     * @param action A ação a ser associada ao objeto.
     */
    public void setAction(ObjectAction action) {
        this.action = action;
        this.hasAction = true;
    }

    /**
     * Verifica se o objeto é interativo (baseado na presença de ação ou tranca).
     *
     * @return true se o objeto for interativo, false caso contrário.
     */
    public boolean isInteractive() {
        return hasAction || keyId != 0;
    }

    public boolean isOpenable() {
        return isOpenable;
    }

    public void setAsOpenable(boolean openable) {
        isOpenable = openable;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getKeyId() {
        return keyId;
    }

    public void setKeyId(int keyId) {
        this.keyId = keyId;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public boolean isStorable() {
        return isStorable;
    }

    public void setStorable(boolean storable) {
        isStorable = storable;
    }

    public boolean isStorage() {
        return isStorage;
    }

    public void setStorage(boolean storage) {
        isStorage = storage;
    }

    public void setContents(ArrayList<GameObject> newContents) {
        contents = newContents;
    }

    public ArrayList<GameObject> getContents() {
        return contents;
    }

    public boolean hasAction() {
        return hasAction;
    }

    public ObjectAction getAction() {
        return action;
    }

    /**
     * Destranca o objeto usando um inventário fornecido.
     *
     * @param inventory O inventário contendo as chaves necessárias.
     */
    public void unlock(ArrayList<GameObject> inventory) {
        // Se o objeto não for abrível
        if (!this.isOpenable()) {
            System.out.println("Não é possível destrancar esse objeto. Ele não possui trancas.");
            return;
        }

        // Se o objeto já estiver destrancado
        if (this.isOpen) {
            System.out.println("Esse objeto já está destrancado.");
            return;
        }

        // Procura um item no inventário que destranque este objeto
        for (GameObject item : inventory) {
            if (item.getId() == this.keyId) {
                this.isOpen = true;
                System.out.println("Você abriu \"" + this.getName().toUpperCase() +
                        "\" com \"" + item.getName().toUpperCase() + "\"!");
                return;  // Sai do metodo após destravar
            }
        }

        if (!this.isOpen()) {
            System.out.printf("O objeto \"%s\" está fechado.\n", this.getName().toUpperCase());
        }

        // Se não encontrou um item válido
        System.out.println("Você não possui um item que destranque \"" + this.getName().toUpperCase() + "\".");
    }

    /**
     * Tranca o objeto usando um inventário fornecido.
     *
     * @param inventory O inventário contendo as chaves necessárias.
     */
    public void lock(ArrayList<GameObject> inventory) {
        // Se o objeto não for abrível
        if (!this.isOpenable()) {
            System.out.println("Não é possível trancar esse objeto. Ele não possui trancas.");
            return;
        }

        // Se o objeto já estiver trancado
        if (!this.isOpen) {
            System.out.println("Esse objeto já está trancado.");
            return;
        }

        // Procura um item no inventário que tranque este objeto
        for (GameObject item : inventory) {
            if (item.getId() == this.keyId) {
                this.isOpen = false;
                System.out.println("Você trancou \"" + this.getName().toUpperCase() +
                        "\" com \"" + item.getName().toUpperCase() + "\"!");
                return;  // Sai do metodo após destravar
            }
        }

        // Se não encontrou um item válido
        System.out.println("Você não possui um item que tranque \"" + this.getName().toUpperCase() + "\".");
    }

    /**
     * Remove um item do conteúdo do objeto e o adiciona ao inventário do jogador.
     *
     * @param gameObject O objeto a ser removido do conteúdo.
     * @param player     O jogador que receberá o objeto.
     */
    public void getContent(GameObject gameObject, Player player) {
        if (gameObject.isStorable()) {
            if (gameObject.isOpen()) {
                if (!gameObject.getContents().isEmpty()) {
                    player.addToInventory(gameObject);
                    player.getCurrentScenario().removeObject(gameObject);
                } else {
                    System.out.println("Esse objeto está vazio.");
                }
            } else {
                System.out.println("Esse objeto está fechado. Abra-o primeiro.");
            }
        } else {
            System.out.println("\"%s\" não pode guardar objetos.".formatted(this.getName().toUpperCase()));
        }
    }

    /**
     * Executa a ação associada a este objeto, se houver.
     */
    public void executeAction() {
        if (action != null) {
            action.execute(); // Chama o execute() da classe ObjectAction
        } else {
            System.out.println("Este objeto não possui uma ação definida.");
        }
    }

    /**
     * Retorna todos os objetos instanciados.
     *
     * @return Um ArrayList contendo todos os objetos instanciados.
     */
    public static ArrayList<GameObject> getAllObjects() {
        return allGameObjects;
    }
}