package core.actions.custom.base;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arsen on 12.01.2017.
 */
public abstract class SimpleAction {

    protected boolean isEnabled = true;
    protected List<SimpleAction> actions;

    public SimpleAction() {
        this.actions = new ArrayList<>();
    }

    /**
     * true когда {@link #run(SimpleAction)} успешно выполнен, false когда неудачно
     */
    protected boolean isDone = false;
    protected boolean isChildrenDone = false;

    /**
     * Contract:
     * 1) set isDone to true if success;
     * 2) Call super in the end;
     */
    public boolean run(SimpleAction parentAction) {
        return runChildren();
    }

    public boolean runChildren() {
        if (actions != null && !actions.isEmpty()) {
            for (SimpleAction action : actions) {
                if (!action.run(this)) {
                    isChildrenDone = false;
                    return false;
                }
            }
        }

        isChildrenDone = true;
        return true;
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

}
