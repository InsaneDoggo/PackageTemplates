package core.actions.custom.base;

import core.actions.custom.interfaces.IHasWriteRules;
import global.models.WriteRules;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arsen on 12.01.2017.
 */
public abstract class SimpleAction {

    protected boolean isEnabled = true;
    protected List<SimpleAction> actions;
    protected SimpleAction parentAction;

    public SimpleAction() {
        this.actions = new ArrayList<>();
    }

    /**
     * true когда {@link #run()} успешно выполнен, false когда неудачно
     */
    protected boolean isDone = false;
    protected boolean isChildrenDone = false;

    /**
     * Contract:
     * 1) set isDone to true if success;
     * 2) Call super in the end;
     */
    public boolean run() {
        return runChildren();
    }

    public boolean runChildren() {
        if (actions != null && !actions.isEmpty()) {
            for (SimpleAction action : actions) {
                action.parentAction = this;
                if (!action.run()) {
                    isChildrenDone = false;
                    return false;
                }
            }
        }

        isChildrenDone = true;
        return true;
    }


    //=================================================================
    //  Utils
    //=================================================================
    private final WriteRules DEFAULT_WRITE_RULES = WriteRules.ASK_ME;

    protected WriteRules geWriteRulesFromParent(SimpleAction parentSimpleAction) {
        if (parentSimpleAction instanceof IHasWriteRules) {
            WriteRules rules = ((IHasWriteRules) parentSimpleAction).getWriteRules();
            if (rules == WriteRules.FROM_PARENT) {
                return geWriteRulesFromParent(parentSimpleAction.getParentAction());
            } else {
                return rules;
            }
        }

        return DEFAULT_WRITE_RULES;
    }


    //=================================================================
    //  Getter | Setter
    //=================================================================
    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public void addAction(SimpleAction action) {
        actions.add(action);
    }

    public List<SimpleAction> getActions() {
        return actions;
    }

    public SimpleAction getParentAction() {
        return parentAction;
    }
}
