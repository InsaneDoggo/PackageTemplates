package core.actions.generated;

import com.intellij.openapi.actionSystem.AnAction;
import org.jetbrains.annotations.Nullable;

/**
 * Created by Arsen on 11.09.2016.
 */
public abstract class BaseAction extends AnAction {

    private String name;

    public BaseAction(@Nullable String name) {
        super(name);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
