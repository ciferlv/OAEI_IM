package instance.matching.pr.train;

import instance.matching.pr.unit.Alignment;
import instance.matching.pr.unit.PredPairList;
import instance.matching.pr.unit.Triples;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static instance.matching.pr.utility.ThreadEndJudge.terminateThread;

/**
 * Created by xinzelv on 17-3-29.
 */
public class AlignmentFinder {

    Logger logger = LoggerFactory.getLogger(AlignmentFinder.class);

    private Alignment resultAlignment = new Alignment();

    private Map<String, Triples> graph1 = null;
    private Map<String, Triples> graph2 = null;

    private Set<String> targetSubject1 = null;
    private Set<String> targetSubject2 = null;

    private PredPairList predPairList = null;

    public AlignmentFinder(Map<String, Triples> graph1,
                           Map<String, Triples> graph2,
                           Set<String> tarSub1,
                           Set<String> tarSub2,
                           PredPairList ppl) {

        this.graph1 = graph1;
        this.graph2 = graph2;
        this.predPairList = ppl;
        this.targetSubject1 = tarSub1;
        this.targetSubject2 = tarSub2;
    }

    public void findAlignment() {

        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

        for (String sub1 : targetSubject1) {

            Triples tri1 = graph1.get(sub1);
            for (String sub2 : targetSubject2) {

                Triples tri2 = graph2.get(sub2);
                Runnable run = new Thread(
                        new AlignmentFinderThread(tri1,tri2,predPairList,resultAlignment));
                cachedThreadPool.execute(run);
            }
        }
        terminateThread(cachedThreadPool, logger);
    }

    public Alignment getResultAlignment() {
        return resultAlignment;
    }
}
