package instance.matching.version2.train;

import instance.matching.version2.unit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static instance.matching.version2.utility.ThreadEndJudge.terminateThread;

/**
 * Created by xinzelv on 17-3-28.
 */
public class PropPairFinder {

    private static Logger logger = LoggerFactory.getLogger(PropPairFinder.class);

    public static void findPredPair(Alignment sample,
                                    VirtualDoc doc1,
                                    VirtualDoc doc2,
                                    PropPairList ppl) {

        Map<String, Instance> graph1 = doc1.getGraph();
        Map<String, Instance> graph2 = doc2.getGraph();

        List<CounterPart> counterPartList = sample.getCounterPartList();
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

        for (CounterPart cp : counterPartList) {

            Runnable run = new Thread(
                    new PropPairFindThread(cp, graph1, graph2, ppl));
            cachedThreadPool.execute(run);
        }

        terminateThread(cachedThreadPool, logger);
    }

}
