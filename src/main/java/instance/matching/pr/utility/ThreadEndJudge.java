package instance.matching.pr.utility;

import org.slf4j.Logger;

import java.util.concurrent.ExecutorService;

/**
 * Created by xinzelv on 17-3-29.
 */
public class ThreadEndJudge {

    public static void terminateThread(ExecutorService es, Logger logger) {

        es.shutdown();
        while (true) {
            if (es.isTerminated()) {
                logger.info("All threads have been finished！");
                break;
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                logger.error(e.getMessage());
            }
        }
    }
}
