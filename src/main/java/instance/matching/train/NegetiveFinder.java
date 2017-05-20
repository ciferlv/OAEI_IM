package instance.matching.train;

import instance.matching.unit.Alignment;
import instance.matching.unit.CounterPart;
import instance.matching.unit.VirtualDoc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static instance.matching.utility.ThreadEndJudge.terminateThread;

/**
 * Created by xinzelv on 17-4-2.
 */
public class NegetiveFinder {

    private static Logger logger = LoggerFactory.getLogger(NegetiveFinder.class);

    public static void findNegetives(Alignment positives,
                                     VirtualDoc doc2,
                                     Alignment negetives) {

        List<CounterPart> counterPartList = positives.getCounterPartList();
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

        for (CounterPart cp : counterPartList) {

            Runnable run = new Thread(
                    new NegetiveFinderThread(cp, doc2, positives, negetives));
            cachedThreadPool.execute(run);

        }

        terminateThread(cachedThreadPool, logger);

    }
}
