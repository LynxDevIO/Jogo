package puc.poo.model;

/**
 * Esta classe representa uma ação de objeto GameObject que pode ser realizada por um jogador num cenário num jogo.
 * Uma ação pode ser de diferentes tipos, como RIFLE, MUNIÇÃO, ou GENÉRICA.
 * A ação pode ter vários atributos, como descrição quando ativa ou inativa,
 * se ela é repetível, a contagem de ações restantes e as condições para execução.
 */
public class ObjectAction {
    /**
     * Enum para os diferentes tipos de ações.
     */
    public enum ActionType {
        RIFLE,
        AMMO,
        GENERIC
    }

    private String actionDescriptionActive;
    private String actionDescriptionInactive;
    private boolean isActive;
    private int actionCount;
    public boolean isRepeatable;

    private boolean hasCondition;
    private ObjectCondition condition;

    private Player player;
    private Stag stag;
    private ActionType actionType;

    /**
     * Construtor para a classe ObjectAction.
     *
     * @param player Jogador executando a ação.
     * @param stag Cenário no qual a ação está sendo executada.
     * @param actionType Tipo da ação.
     */
    public ObjectAction(Player player, Stag stag, ActionType actionType) {
        this.player = player;
        this.stag = stag;
        this.actionType = actionType;
    }

    /**
     * Retorna a contagem de ações restantes.
     *
     * @return Número de ações restantes.
     */
    public int getActionCount() {
        return actionCount;
    }

    /**
     * Define a contagem de ações restantes.
     *
     * @param actionCount Número de ações para definir.
     */
    public void setActionCount(int actionCount) {
        this.actionCount = actionCount;
    }

    /**
     * Retorna a descrição da ação quando ela está ativa.
     *
     * @return Descrição ativa da ação.
     */
    public String getActionDescriptionActive() {
        return actionDescriptionActive;
    }

    /**
     * Define a descrição da ação quando ela está ativa.
     *
     * @param actionDescriptionActive Descrição ativa da ação.
     */
    public void setActionDescriptionActive(String actionDescriptionActive) {
        this.actionDescriptionActive = actionDescriptionActive;
    }

    /**
     * Retorna a descrição da ação quando ela está inativa.
     *
     * @return Descrição inativa da ação.
     */
    public String getActionDescriptionInactive() {
        return actionDescriptionInactive;
    }

    /**
     * Define a descrição da ação quando ela está inativa.
     *
     * @param actionDescriptionInactive Descrição inativa da ação.
     */
    public void setActionDescriptionInactive(String actionDescriptionInactive) {
        this.actionDescriptionInactive = actionDescriptionInactive;
    }

    /**
     * Verifica se a ação está ativa.
     *
     * @return Verdadeiro se a ação estiver ativa, falso caso contrário.
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * Define se a ação está ativa.
     *
     * @param active Verdadeiro para definir a ação como ativa, falso caso contrário.
     */
    public void setActive(boolean active) {
        isActive = active;
    }

    /**
     * Verifica se a ação é repetível.
     *
     * @return Verdadeiro se a ação for repetível, falso caso contrário.
     */
    public boolean isRepeatable() {
        return isRepeatable;
    }

    /**
     * Define se a ação é repetível.
     *
     * @param repeatable Verdadeiro para definir a ação como repetível, falso caso contrário.
     */
    public void setRepeatable(boolean repeatable) {
        isRepeatable = repeatable;
    }

    /**
     * Verifica se a ação tem uma condição associada.
     *
     * @return Verdadeiro se a ação tiver uma condição, falso caso contrário.
     */
    public boolean getHasCondition() {
        return hasCondition;
    }

    /**
     * Define se a ação tem uma condição associada.
     *
     * @param hasCondition Verdadeiro para definir que a ação tem uma condição, falso caso contrário.
     */
    public void setHasCondition(boolean hasCondition) {
        this.hasCondition = hasCondition;
    }

    /**
     * Retorna a condição associada à ação.
     *
     * @return Condição da ação.
     */
    public ObjectCondition getCondition() {
        return condition;
    }

    /**
     * Define a condição associada à ação.
     *
     * @param condition Condição da ação.
     */
    public void setCondition(ObjectCondition condition) {
        this.condition = condition;
        this.hasCondition = true;
    }

    /**
     * Executa a ação.
     */
    public void execute() {
        if (actionType == ActionType.GENERIC) {
            if (isRepeatable && actionCount > 0) {
                System.out.println(actionDescriptionActive);
                actionCount--;
            } else if (isRepeatable && actionCount == 0) {
                System.out.println(actionDescriptionInactive);
                return;
            }
        }

        if (actionType == ActionType.GENERIC && hasCondition && condition != null && !condition.checkCondition(player)) {
            System.out.println("A condição para executar esta ação não foi satisfeita.");
            return;
        }

        if (isActive) {
            if (actionType == ActionType.RIFLE) {
                if (!player.getCurrentScenario().equals("Floresta")) {
                    System.out.println("Não devo usar isso aqui.");
                } else {
                    if (stag.isAlive()) {
                        boolean killed = stag.tryToKill();
                        if (killed) {
                            try {
                                stag.setAlive(false);
                                System.out.println("Você mirou e atirou!");
                                Thread.sleep(1000);
                                System.out.println("O veado foi morto.");
                                Thread.sleep(1000);
                                System.out.println("(Devo coletar seus chifres.)");
                                GameObject forest_antlers = new GameObject("chifres do veado", "Chifres do veado que você coletou. Serve como um troféu");
                                forest_antlers.setAsStorable();
                                player.getCurrentScenario().addObject(forest_antlers);
                            } catch (Exception e) {}

                        } else {
                            System.out.println("Você mirou e atirou, mas o veado escapou!");
                        }
                    } else {
                        System.out.println("O veado já está morto.");
                    }
                }
            } else if (actionType == ActionType.AMMO) {
                boolean hasRifle = player.getFromInventory("rifle") != null;
                this.condition.setSatisfied(hasRifle);
                
                if (this.getHasCondition() && this.condition.isSatisfied()) {
                    this.setActionCount(5);
                    System.out.println("Rifle recarregado com cinco balas.");
                } else {
                    System.out.println("Preciso do rifle para usar isso.");
                }
            } else {
                System.out.println(actionDescriptionActive);
            }
        } else {
            System.out.println(actionDescriptionInactive != null ? actionDescriptionInactive : "Ação inativa.");
        }
    }
}
