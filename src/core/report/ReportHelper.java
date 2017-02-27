package core.report;

import core.report.enums.ExecutionState;
import core.report.models.BaseReport;
import global.utils.Logger;

import java.util.ArrayList;

/**
 * Created by Arsen on 25.02.2017.
 */
public class ReportHelper {

    private ExecutionState state;
    private ArrayList<BaseReport> reports;

    private ReportHelper() {
        init();
    }

    private void init() {
        state = ExecutionState.NONE;
        reports = new ArrayList<>();
    }


    //=================================================================
    //  API
    //=================================================================
    public static void setState(ExecutionState state) {
        getInstance().state = state;
    }

    public static ExecutionState getState() {
        return getInstance().state;
    }

    public static void addReport(BaseReport report) {
        getInstance().reports.add(report);
    }

    public static ArrayList<BaseReport> getReports() {
        return getInstance().reports;
    }

    public static void clear() {
        getInstance().init();
    }

    public static boolean shouldContinue() {
        switch (getState()) {
            case IN_PROGRESS:
                return true;
            default:
                Logger.log("Unknown ExecutionState!");
            case NONE:
            case SUCCESS:
            case FAILED:
                return false;
        }
    }


    //=================================================================
    //  Singleton
    //=================================================================
    private static class LazyHolder {
        private static final ReportHelper INSTANCE = new ReportHelper();
    }

    private static ReportHelper getInstance() {
        return LazyHolder.INSTANCE;
    }


}
