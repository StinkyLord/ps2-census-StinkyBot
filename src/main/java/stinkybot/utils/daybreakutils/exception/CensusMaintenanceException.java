package stinkybot.utils.daybreakutils.exception;

import stinkybot.utils.daybreakutils.MaintenanceReport;

public class CensusMaintenanceException extends CensusException {

    private final MaintenanceReport report;

    public CensusMaintenanceException(String errMessage, CensusException ce, MaintenanceReport report) {
        super(errMessage, ce.getUrl());
        this.report = report;
    }

    public MaintenanceReport getReport() {
        return report;
    }
}
