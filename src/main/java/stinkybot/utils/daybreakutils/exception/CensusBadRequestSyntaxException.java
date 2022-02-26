package stinkybot.utils.daybreakutils.exception;

public class CensusBadRequestSyntaxException extends CensusException {
    protected CensusBadRequestSyntaxException(String errMessage, String url) {
        super(errMessage, url);
    }
}
