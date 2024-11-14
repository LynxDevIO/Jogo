package puc.poo.model;

public class ObjectAction {
    public enum ActionType {
        RIFLE,
        BAIT,
        GENERIC
    }

    private String actionDescriptionActive;
    private String actionDescriptionInactive;
    private boolean isActive;
    public boolean isRepeatable;

    private boolean hasCondition;
    private ObjectCondition condition;

    private Player player;
    private Dove dove;
    private ActionType actionType;

    public ObjectAction(Player player, Dove dove, ActionType actionType) {
        this.player = player;
        this.dove = dove;
        this.actionType = actionType;
    }

    public String getActionDescriptionActive() {
        return actionDescriptionActive;
    }

    public void setActionDescriptionActive(String actionDescriptionActive) {
        this.actionDescriptionActive = actionDescriptionActive;
    }

    public String getActionDescriptionInactive() {
        return actionDescriptionInactive;
    }

    public void setActionDescriptionInactive(String actionDescriptionInactive) {
        this.actionDescriptionInactive = actionDescriptionInactive;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isRepeatable() {
        return isRepeatable;
    }

    public void setRepeatable(boolean repeatable) {
        isRepeatable = repeatable;
    }

    public boolean isHasCondition() {
        return hasCondition;
    }

    public void setHasCondition(boolean hasCondition) {
        this.hasCondition = hasCondition;
    }

    public ObjectCondition getCondition() {
        return condition;
    }

    public void setCondition(ObjectCondition condition) {
        this.condition = condition;
        this.hasCondition = true;
    }

    public void execute() {
        if (hasCondition && condition != null && !condition.checkCondition(player)) {
            System.out.println("A condição para executar esta ação não foi satisfeita.");
            return;
        }

        if (isActive) {
            if (actionType == ActionType.RIFLE) {
                if (dove.isAlive()) {
                    boolean killed = dove.tryToKill();
                    if (killed) {
                        dove.setAlive(false);
                        System.out.println("Você mirou e atirou! O veado foi morto.");
                    } else {
                        System.out.println("Você mirou e atirou, mas o veado escapou!");
                    }
                } else {
                    System.out.println("O veado já está morto.");
                }
            } else if (actionType == ActionType.BAIT) {
                System.out.println(actionDescriptionActive);
            } else {
                System.out.println(actionDescriptionActive);
            }
        } else {
            System.out.println(actionDescriptionInactive != null ? actionDescriptionInactive : "Ação inativa.");
        }
    }
}
