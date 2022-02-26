package stinkybot.utils.daybreakutils.exception;

public class CensusServiceUnavailableException extends CensusException {

    protected CensusServiceUnavailableException(String errMessage, String url) {
        super(errMessage, url);
    }
}
