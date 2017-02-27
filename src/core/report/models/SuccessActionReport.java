package core.report.models;

import core.actions.custom.base.SimpleAction;
import core.report.enums.ReportType;

/**
 * Created by Arsen on 26.02.2017.
 */
public class SuccessActionReport extends BaseReport {

    public SuccessActionReport(SimpleAction action, String message) {
        super(action, message);
    }

    public SuccessActionReport(SimpleAction action, String message, String debugMessage) {
        super(action, message, debugMessage);
    }

    @Override
    public ReportType getType() {
        return ReportType.SUCCESS_ACTION;
    }

}
