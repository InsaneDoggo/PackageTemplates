package core.report.models;

import core.actions.custom.base.SimpleAction;
import core.report.enums.ReportType;

/**
 * Created by Arsen on 26.02.2017.
 */
public class PendingActionReport extends BaseReport {

    public PendingActionReport(SimpleAction action) {
        super(action, "");
    }

    @Override
    public ReportType getType() {
        return ReportType.PENDING_ACTION;
    }

}
