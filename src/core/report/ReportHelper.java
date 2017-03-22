package core.report;

import core.report.enums.ExecutionState;
import core.report.models.BaseReport;
import java.util.HashMap;

/**
 * Created by Arsen on 25.02.2017.
 */
public class ReportHelper {

    private ExecutionState state;
    private HashMap<Integer, BaseReport> reports;
    private int generatedId;

    private ReportHelper() {
        init();
    }

    private void init() {
        state = ExecutionState.NONE;
        reports = new HashMap<>();
        generatedId = 0;
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

    public static void putReport(BaseReport report) {
        getInstance().reports.put(report.getAction().getId(), report);
    }

    public static HashMap<Integer, BaseReport> getReports() {
        return getInstance().reports;
    }

    public static void reset() {
        getInstance().init();
    }

    public static boolean shouldContinue() {
        switch (getState()) {
            case IN_PROGRESS:
                return true;
            default:
                throw new RuntimeException("Unknown ExecutionState!");
            case NONE:
            case SUCCESS:
            case FAILED:
                return false;
        }
    }

    public static int getGenerateId() {
        int id = getInstance().generatedId;
        // update
        getInstance().generatedId++;
        return id;
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
