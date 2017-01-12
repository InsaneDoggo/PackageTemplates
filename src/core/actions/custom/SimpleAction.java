package core.actions.custom;

/**
 * Created by Arsen on 12.01.2017.
 */
public abstract class SimpleAction {

    protected boolean isEnabled = true;
    protected boolean isDone = false;

    /**
     *  Contract: set isDone to true if success;
     * @return isSuccess
     */
    public abstract boolean run();

    /**
     *  Contract: set isDone to false if success;
     * @return isSuccess
     */
    public boolean undo(){
        isDone = false;
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
}
