package core.actions.generated;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Created by Arsen on 11.09.2016.
 */
public abstract class BaseAction extends AnAction {

    private String name;
    protected boolean isEnabled = true;


    @Override
    public void update(AnActionEvent e) {
        e.getPresentation().setEnabled(isEnabled);
    }

    //=================================================================
    //  Constructors
    //=================================================================
    public BaseAction(@Nullable String name) {
        super(name);
        this.name = name;
    }

    public BaseAction(@Nullable String text, @Nullable String description, @Nullable Icon icon) {
        super(text, description, icon);
    }

    public BaseAction() {
    }

    public BaseAction(Icon icon) {
        super(icon);
    }


    //=================================================================
    //  Getter | Setter
    //=================================================================
    public String getName() {
        return name;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }
}
