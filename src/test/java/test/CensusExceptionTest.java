package test;

import stinkybot.utils.daybreakutils.MaintenanceReport;
import stinkybot.utils.daybreakutils.Query;
import stinkybot.utils.daybreakutils.anatomy.Collection;
import stinkybot.utils.daybreakutils.anatomy.Constants;
import stinkybot.utils.daybreakutils.logging.LoggingInterceptor;
import stinkybot.utils.daybreakutils.exception.CensusException;
import okhttp3.*;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

class CensusExceptionTest {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Test
    void testForMaintenance() {
        MaintenanceReport report = null;
        report = MaintenanceReport.createReport(Constants.EXAMPLE_SERVICE_ID.toString());
        logger.info(report.toString());
    }

    @Test
    void testExceptions() {
        Query q = new Query(Collection.EVENT)
                .filter("type", "FACILITY");
        try {
            q.get();
        } catch (CensusException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        q.filter("world_id", "13");
        try {
            q.get();
        } catch (CensusException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testSomething(){
        OkHttpClient client = new OkHttpClient().newBuilder()
                .followRedirects(false)
                .addInterceptor(new LoggingInterceptor())
                .build();

        Request request = new Request.Builder()
                .url("http://t.co/I5YYd9tddw")
                .build();

        Call call = client.newCall(request);
        try {
            Response response = call.execute();

            logger.debug("Response: " + response.code() + " " + response.isRedirect());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
