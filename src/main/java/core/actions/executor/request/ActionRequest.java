package core.actions.executor.request;

import com.intellij.openapi.command.UndoConfirmationPolicy;
import com.intellij.openapi.project.Project;
import core.actions.custom.base.SimpleAction;
import core.actions.executor.AccessPrivileges;

import java.util.List;

/**
 * Created by Arsen on 15.02.2017.
 */
public class ActionRequest {

    public interface ActionFinishListener {
        void onFinish();
        void onFail();
    }

    public Project project;
    public List<SimpleAction> actions;
    public String actionLabel;
    public Object groupId;
    public AccessPrivileges accessPrivileges;
    public UndoConfirmationPolicy confirmationPolicy;
    public boolean isUndoable;
    public ActionFinishListener actionFinishListener;

}
