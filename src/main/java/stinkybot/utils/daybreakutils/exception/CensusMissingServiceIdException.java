package stinkybot.utils.daybreakutils.exception;

public class CensusMissingServiceIdException extends CensusException {
    protected CensusMissingServiceIdException(String errMessage, String url) {
        super(errMessage, url);
    }
}
