package instance.matching.version2.train;

import instance.matching.version2.unit.Alignment;
import instance.matching.version2.unit.Document;
import instance.matching.version2.unit.PropPairList;
import instance.matching.version2.unit.Instance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static instance.matching.version2.utility.ThreadEndJudge.terminateThread;

/**
 * Created by xinzelv on 17-3-29.
 */
public class AlignmentFinder {

    private static Logger logger = LoggerFactory.getLogger(AlignmentFinder.class);

    public static void findResultAlign(Document doc1,
                                       Document doc2,
                                       PropPairList ppl,
                                       Alignment resultAlign) {

        Map<String, Instance> graph1 = doc1.getGraph();
        Map<String, Instance> graph2 = doc2.getGraph();

        List<String> targetSubject1 = doc1.getTargetSubject();
        List<String> targetSubject2 = doc2.getTargetSubject();

        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

        for (String sub1 : targetSubject1) {

            Instance tri1 = graph1.get(sub1);
            for (String sub2 : targetSubject2) {

                Instance tri2 = graph2.get(sub2);
                Runnable run = new Thread(
                        new AlignmentFinderThread(tri1, tri2, ppl, resultAlign));
                cachedThreadPool.execute(run);
            }
        }
        terminateThread(cachedThreadPool, logger);
    }
}
