package stinkybot.utils.daybreakutils.exception;

public class CensusRedirectException extends CensusException {
    protected CensusRedirectException(String errMessage, String url) {
        super(errMessage, url);
    }
}
