package puc.poo.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

/// Classe responsável pela criação e gerenciamento de objetos do jogo (GameOject).
public class GameObject implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private static ArrayList<GameObject> allGameObjects = new ArrayList<>();

    private final int id;
    private String name;
    private String description;
    private boolean isStorage; // É armazenamento?
    private boolean isStorable; // Pode ser armazenado no inventário?
    private int keyId; // ID da chave para destravar, se aplicável
    private boolean isOpenable; // Objeto é abrível?
    private boolean isOpen; // Objeto está aberto/fechado?
    // private boolean isLocked; // Objeto está trancado/destrancado?
    private boolean hasAction; // Objeto possui ação?
    private ObjectAction action; // Ação do objeto
    private ArrayList<GameObject> contents; // Conteúdo armazenado

    // Construtor genérico
    public GameObject(String name, String description) {
        allGameObjects.add(this);
        this.id = allGameObjects.size();
        this.name = name;
        this.description = description;
    }

    public void setAsStorable() {
        this.isStorable = true;
    }

    // Configurar como Storage (pode conter outros objetos)
    public void setAsStorage(boolean storable) {
        this.isStorage = storable;
        if (storable) {
            this.contents = new ArrayList<>();
        } else {
            this.contents = null;
        }
    }

    // Cconfigurar tranca e interatividade
    public void setLock(int keyId, boolean isOpen) {
        this.keyId = keyId;
        this.isOpen = isOpen;
    }

    // Configurar ação
    public void setAction(ObjectAction action) {
        this.action = action;
        this.hasAction = true;
    }

    // Verifica se é interativo (baseado em presença de ação ou tranca)
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

    // Métodos para objetos interagíveis e destrancáveis
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

    // Executa a ação, se definida
    public void executeAction() {
        if (action != null) {
            action.execute(); // Chama o execute() da classe ObjectAction
        } else {
            System.out.println("Este objeto não possui uma ação definida.");
        }
    }

    public static ArrayList<GameObject> getAllObjects() {
        return allGameObjects;
    }
}