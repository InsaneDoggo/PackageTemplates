package core.report.models;

import core.actions.custom.base.SimpleAction;
import core.report.enums.ReportType;

/**
 * Created by Arsen on 26.02.2017.
 */
public abstract class BaseReport {

    protected SimpleAction action;
    protected String message;

    public BaseReport(SimpleAction action, String message) {
        this.action = action;
        this.message = message;
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

}
