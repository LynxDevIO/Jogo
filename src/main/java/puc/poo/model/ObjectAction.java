package puc.poo.model;

public class ObjectAction {
    private String actionDescriptionActive;
    private String actionDescriptionInactive;
    private boolean isActive;
    private boolean hasCondition;
    private ObjectCondition condition;

    public ObjectAction() {
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
    }

    // Executar a ação
    public void execute() {
        if (hasCondition && condition != null && !condition.isSatisfied()) {
            System.out.println("A condição para executar esta ação não foi satisfeita.");
            return;
        }

        if (isActive) {
            System.out.println(actionDescriptionActive != null ? actionDescriptionActive : "Ação realizada.");
        } else {
            System.out.println(actionDescriptionInactive != null ? actionDescriptionInactive : "Ação inativa.");
        }
    }
}
