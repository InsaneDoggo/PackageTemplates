package core.actions.executor.request;

import com.intellij.openapi.command.UndoConfirmationPolicy;
import com.intellij.openapi.project.Project;
import core.actions.custom.base.SimpleAction;
import core.actions.executor.AccessPrivileges;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arsen on 15.02.2017.
 */
public class ActionRequestBuilder {

     private ActionRequest request;

    public ActionRequestBuilder() {
        //defaults
        request = new ActionRequest();
        request.project = null;
        request.actions = new ArrayList<>();
        request.actionLabel = "actionLabel";
        request.groupId = null;
        request.accessPrivileges = AccessPrivileges.NONE;
        request.confirmationPolicy = UndoConfirmationPolicy.DEFAULT;
        request.isUndoable = true;
    }

    public ActionRequest build(){
        return request;
    }

    //=================================================================
    //  Setters
    //=================================================================
    public ActionRequestBuilder setProject(Project project) {
        request.project = project;
        return this;
    }

    public ActionRequestBuilder setActions(List<SimpleAction> actions) {
        request.actions = actions;
        return this;
    }

    public ActionRequestBuilder setSingleAction(SimpleAction action) {
        ArrayList<SimpleAction> list = new ArrayList<>();
        list.add(action);
        request.actions = list;
        return this;
    }

    public ActionRequestBuilder setActionLabel(String actionLabel) {
        request.actionLabel = actionLabel;
        return this;
    }

    public ActionRequestBuilder setGroupId(Object groupId) {
        request.groupId = groupId;
        return this;
    }

    public ActionRequestBuilder setAccessPrivileges(AccessPrivileges accessPrivileges) {
        request.accessPrivileges = accessPrivileges;
        return this;
    }

    public ActionRequestBuilder setConfirmationPolicy(UndoConfirmationPolicy confirmationPolicy) {
        request.confirmationPolicy = confirmationPolicy;
        return this;
    }

    public ActionRequestBuilder setUndoable(boolean undoable) {
        request.isUndoable = undoable;
        return this;
    }

    public ActionRequestBuilder setActionListener(ActionRequest.ActionFinishListener actionFinishListener) {
        request.actionFinishListener = actionFinishListener;
        return this;
    }

}
