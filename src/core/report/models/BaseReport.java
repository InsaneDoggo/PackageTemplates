package core.report.models;

import core.actions.custom.base.SimpleAction;
import core.report.enums.ReportType;

/**
 * Created by Arsen on 26.02.2017.
 */
public abstract class BaseReport {

    protected SimpleAction action;
    protected String message;
    protected String debugMessage;

    public BaseReport(SimpleAction action, String message) {
        this(action, message, message);
    }

    public BaseReport(SimpleAction action, String message, String debugMessage) {
        this.action = action;
        this.message = message;
        this.debugMessage = debugMessage;
    }


    //=================================================================
    //  Abstract
    //=================================================================
    public abstract ReportType getType();


    //=================================================================
    //  get | set
    //=================================================================
    public SimpleAction getAction() {
        return action;
    }

    public void setAction(SimpleAction action) {
        this.action = action;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDebugMessage() {
        return debugMessage;
    }

    public void setDebugMessage(String debugMessage) {
        this.debugMessage = debugMessage;
    }
}
