package instance.matching.version2.train;

import instance.matching.version2.unit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static instance.matching.version2.nlp.CalSimilarity.calObjSetSim;
import static instance.matching.version2.utility.ThreadEndJudge.terminateThread;
import static instance.matching.version2.utility.VariableDef.predPairSize;
import static instance.matching.version2.utility.VariableDef.predPairThreshold;

/**
 * Created by xinzelv on 17-4-5.
 */
public class InfoGainCalculator {

    private static Logger logger = LoggerFactory.getLogger(InfoGainCalculator.class);

    public static double calEntropy(double posSize, double negSize) {

        double pos = posSize * 1.0 / (posSize + negSize);
        double neg = negSize * 1.0 / (posSize + negSize);


        if (neg == 1 || pos == 1) return 0;

        double res = -1 * pos * Math.log(pos) / Math.log(2.0) - neg * Math.log(neg) / Math.log(2.0);

        return res;
    }

    public static void calInfoGain(Alignment positives,
                                   Alignment negetives,
                                   Document doc1,
                                   Document doc2,
                                   PredPairList ppl) {

        int posSize = positives.size();
        int negSize = negetives.size();

        double initialEntropy = calEntropy(posSize, negSize);

        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

        for (PredPair pp : ppl.getPredPairList()) {

            Runnable run = new Thread(
                    new InfoGainCalculatorThread(positives, negetives, doc1, doc2, pp, initialEntropy));
            cachedThreadPool.execute(run);

        }

        terminateThread(cachedThreadPool, logger);

        ppl.sort();
        logger.info(ppl.toString());
        ppl.resize(predPairSize);
    }
}
