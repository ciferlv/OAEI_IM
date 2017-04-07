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
import static instance.matching.version2.utility.VariableDef.PROP_PAIR_SIZE;
import static instance.matching.version2.utility.VariableDef.PROP_PAIR_THRESHOLD;

/**
 * Created by xinzelv on 17-3-28.
 */
public class PredPairFinder {

    private static Logger logger = LoggerFactory.getLogger(PredPairFinder.class);

    public static void findPredPair(Alignment sample,
                                    Document doc1,
                                    Document doc2,
                                    PropPairList ppl) {

        Map<String, Instance> graph1 = doc1.getGraph();
        Map<String, Instance> graph2 = doc2.getGraph();

        List<CounterPart> counterPartList = sample.getCounterPartList();
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

        for (CounterPart cp : counterPartList) {

            Runnable run = new Thread(
                    new PredPairFindThread(cp, graph1, graph2, ppl));
            cachedThreadPool.execute(run);
        }

        terminateThread(cachedThreadPool, logger);
    }

    public static double calEntropy(double posSize, double negSize) {

        double pos = posSize * 1.0 / (posSize + negSize);
        double neg = negSize * 1.0 / (posSize + negSize);


        if (neg == 1 || pos == 1) return 0;

        double res = -1 * pos * Math.log(pos) / Math.log(2.0) - neg * Math.log(neg) / Math.log(2.0);

        return res;
    }


    public static void testInfoGain(Alignment positives,
                                    Alignment negetives,
                                    Document doc1,
                                    Document doc2,
                                    PropPairList ppl) {

        Map<String, Instance> graph1 = doc1.getGraph();
        Map<String, Instance> graph2 = doc2.getGraph();

        int posSize = positives.size();
        int negSize = negetives.size();

        double initialEntropy = calEntropy(posSize, negSize);

        for (PropPair pp : ppl.getPropPairList()) {

            int truePos = 0, falsePos = 0, trueNeg = 0, falseNeg = 0;

            String pre1 = pp.getPred1();
            String pre2 = pp.getPred2();

            for (CounterPart cp : positives.getCounterPartList()) {

                String sub1 = cp.getSubject1();
                String sub2 = cp.getSubject2();

                Instance tri1 = graph1.get(sub1);
                Instance tri2 = graph2.get(sub2);

                Set<String> objSet1 = tri1.getPropValue().get(pre1);
                Set<String> objSet2 = tri2.getPropValue().get(pre2);

                if (objSet1 == null || objSet2 == null) {

                    falsePos++;
                    continue;
                } else {

                    double value = calObjSetSim(objSet1, objSet2);
                    if (value > PROP_PAIR_THRESHOLD) {
                        truePos++;
                    } else falsePos++;
                }
            }

            for (CounterPart cp : negetives.getCounterPartList()) {

                String sub1 = cp.getSubject1();
                String sub2 = cp.getSubject2();

                Instance tri1 = graph1.get(sub1);
                Instance tri2 = graph2.get(sub2);

                Set<String> objSet1 = tri1.getPropValue().get(pre1);
                Set<String> objSet2 = tri2.getPropValue().get(pre2);

                if (objSet1 == null || objSet2 == null) {
                    trueNeg++;
                } else {

                    double value = calObjSetSim(objSet1, objSet2);
                    if (value > PROP_PAIR_THRESHOLD) {
                        falseNeg++;
                    } else trueNeg++;
                }
            }

            double posEntropy = calEntropy(truePos, falseNeg);
            double negEntropy = calEntropy(falsePos, trueNeg);

            double stateEntropy = posEntropy * (truePos + falseNeg) / (posSize + negSize)
                    + negEntropy * (falsePos + trueNeg) / (posSize + negSize);
            pp.setInfoGain(initialEntropy - stateEntropy);
        }

        ppl.sort();
        logger.info(ppl.toString());
        ppl.resize(PROP_PAIR_SIZE);
    }

}
