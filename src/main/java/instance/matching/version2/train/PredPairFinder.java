package instance.matching.version2.train;

import instance.matching.version2.unit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static instance.matching.version2.nlp.CalSimilarity.calObjSetSim;
import static instance.matching.version2.utility.ThreadEndJudge.terminateThread;
import static instance.matching.version2.utility.VariableDef.predPairSize;
import static instance.matching.version2.utility.VariableDef.predPairThreshold;

/**
 * Created by xinzelv on 17-3-28.
 */
public class PredPairFinder {

    private static Logger logger = LoggerFactory.getLogger(PredPairFinder.class);

    public static void findPredPair(Alignment sample,
                                    Document doc1,
                                    Document doc2,
                                    PredPairList ppl) {

        Map<String, Triples> graph1 = doc1.getGraph();
        Map<String, Triples> graph2 = doc2.getGraph();

        List<CounterPart> counterPartList = sample.getCounterPartList();
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

        for (CounterPart cp : counterPartList) {

            Runnable run = new Thread(
                    new PredPairFindThread(cp, graph1, graph2, ppl));
            cachedThreadPool.execute(run);
        }

        terminateThread(cachedThreadPool, logger);

    }

}
