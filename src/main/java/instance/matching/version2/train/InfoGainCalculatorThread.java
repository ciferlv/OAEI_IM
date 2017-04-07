package instance.matching.version2.train;


import instance.matching.version2.unit.*;

import java.util.Map;
import java.util.Set;

import static instance.matching.version2.nlp.CalSimilarity.calValSetSim;
import static instance.matching.version2.train.InfoGainCalculator.calEntropy;
import static instance.matching.version2.utility.VariableDef.PROP_PAIR_THRESHOLD;

/**
 * Created by xinzelv on 17-4-5.
 */
public class InfoGainCalculatorThread implements Runnable {

    private PropPair pp = null;
    private Alignment positives = null, negetives = null;
    private VirtualDoc doc1 = null, doc2 = null;
    private double initialEntropy;

    public InfoGainCalculatorThread(Alignment positives,
                                    Alignment negetives,
                                    VirtualDoc doc1,
                                    VirtualDoc doc2,
                                    PropPair pp,
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

        String prop1 = pp.getProp1();
        String prop2 = pp.getProp2();

        Map<String, Instance> graph1 = doc1.getGraph();
        Map<String, Instance> graph2 = doc2.getGraph();

        for (CounterPart cp : positives.getCounterPartList()) {

            String sub1 = cp.getSub1();
            String sub2 = cp.getSub2();

            Instance inst1 = graph1.get(sub1);
            Instance inst2 = graph2.get(sub2);

            Set<Value> valSet1 = inst1.getPropValue().get(prop1);
            Set<Value> valSet2 = inst2.getPropValue().get(prop2);

            if (valSet1 == null || valSet2 == null) {

                falsePos++;
                continue;
            } else {

                double value = calValSetSim(valSet1, valSet2);
                if (value > PROP_PAIR_THRESHOLD) {
                    truePos++;
                } else falsePos++;
            }
        }

        for (CounterPart cp : negetives.getCounterPartList()) {

            String sub1 = cp.getSub1();
            String sub2 = cp.getSub2();

            Instance inst1 = graph1.get(sub1);
            Instance inst2 = graph2.get(sub2);

            Set<Value> valSet1 = inst1.getPropValue().get(prop1);
            Set<Value> valSet2 = inst2.getPropValue().get(prop2);

            if (valSet1 == null || valSet2 == null) {
                trueNeg++;
            } else {

                double value = calValSetSim(valSet1, valSet2);
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
}
