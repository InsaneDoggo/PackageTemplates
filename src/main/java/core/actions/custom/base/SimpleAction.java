package core.actions.custom.base;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ModalityState;
import core.actions.custom.interfaces.IHasWriteRules;
import core.report.ReportHelper;
import core.report.enums.ExecutionState;
import core.report.models.FailedActionReport;
import core.writeRules.WriteRules;
import global.utils.Logger;
import global.utils.i18n.Localizer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arsen on 12.01.2017.
 */
public abstract class SimpleAction {

    protected List<SimpleAction> actions;
    protected SimpleAction parentAction;
    protected Integer id;

    public SimpleAction() {
        this.actions = new ArrayList<>();
    }

    public void run() {
        try {
            ApplicationManager.getApplication().invokeAndWait(() ->
                    ApplicationManager.getApplication().runReadAction(this::doRun)
            , ModalityState.defaultModalityState());
        } catch (Exception e) {
            ReportHelper.setState(ExecutionState.FAILED);
            ReportHelper.putReport(new FailedActionReport(this, Localizer.get("error.internalError"), "SimpleAction catch: " + e.getMessage()));
            Logger.logAndPrintStack("SimpleAction catch: " + e.getMessage(), e);
        }
        runChildren();
    }

    private void runChildren() {
        if (actions == null || actions.isEmpty()) {
            return;
        }

        for (SimpleAction action : actions) {
            action.parentAction = this;
            action.run();
            if (!ReportHelper.shouldContinue()) {
                return;
            }
        }
    }


    //=================================================================
    //  Abstraction
    //=================================================================
    protected abstract void doRun();


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

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

    //=================================================================
    //  Getter | Setter
    //=================================================================
    public void addAction(SimpleAction action) {
        actions.add(action);
    }

    public List<SimpleAction> getActions() {
        return actions;
    }

    public SimpleAction getParentAction() {
        return parentAction;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
