package instance.matching.version2.train;


import instance.matching.version2.unit.*;

import java.util.Map;
import java.util.Set;

import static instance.matching.version2.nlp.CalSimilarity.calObjSetSim;
import static instance.matching.version2.train.InfoGainCalculator.calEntropy;
import static instance.matching.version2.utility.VariableDef.predPairThreshold;

/**
 * Created by xinzelv on 17-4-5.
 */
public class InfoGainCalculatorThread implements Runnable {

    private PredPair pp = null;
    private Alignment positives = null, negetives = null;
    private Document doc1 = null, doc2 = null;
    private double initialEntropy;

    public InfoGainCalculatorThread(Alignment positives,
                                    Alignment negetives,
                                    Document doc1,
                                    Document doc2,
                                    PredPair pp,
                                    double initialEntropy) {

        this.pp = pp;
        this.doc1 = doc1;
        this.doc2 = doc2;
        this.positives = positives;
        this.negetives = negetives;
        this.initialEntropy = initialEntropy;
    }

    public void run() {

        int truePos = 0, falsePos = 0, trueNeg = 0, falseNeg = 0;

        int posSize = positives.size();
        int negSize = negetives.size();

        String pre1 = pp.getPred1();
        String pre2 = pp.getPred2();

        Map<String, Triples> graph1 = doc1.getGraph();
        Map<String, Triples> graph2 = doc2.getGraph();

        for (CounterPart cp : positives.getCounterPartList()) {

            String sub1 = cp.getSubject1();
            String sub2 = cp.getSubject2();

            Triples tri1 = graph1.get(sub1);
            Triples tri2 = graph2.get(sub2);

            Set<String> objSet1 = tri1.getPredicateObject().get(pre1);
            Set<String> objSet2 = tri2.getPredicateObject().get(pre2);

            if (objSet1 == null || objSet2 == null) {

                falsePos++;
                continue;
            } else {

                double value = calObjSetSim(objSet1, objSet2);
                if (value > predPairThreshold) {
                    truePos++;
                } else falsePos++;
            }
        }

        for (CounterPart cp : negetives.getCounterPartList()) {

            String sub1 = cp.getSubject1();
            String sub2 = cp.getSubject2();

            Triples tri1 = graph1.get(sub1);
            Triples tri2 = graph2.get(sub2);

            Set<String> objSet1 = tri1.getPredicateObject().get(pre1);
            Set<String> objSet2 = tri2.getPredicateObject().get(pre2);

            if (objSet1 == null || objSet2 == null) {
                trueNeg++;
            } else {

                double value = calObjSetSim(objSet1, objSet2);
                if (value > predPairThreshold) {
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
}
