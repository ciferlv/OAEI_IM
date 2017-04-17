package instance.matching.version2.train;

import instance.matching.version2.unit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import static instance.matching.version2.nlp.CalSimilarity.calValSetSim;
import static instance.matching.version2.utility.VariableDef.PROP_PAIR_THRESHOLD;

/**
 * Created by ciferlv on 17-3-29.
 */
public class PropPairFindThread implements Runnable {

    Logger logger = LoggerFactory.getLogger(PropPairFindThread.class);

    private CounterPart cp = null;
    private Map<String, Instance> graph1 = null;
    private Map<String, Instance> graph2 = null;
    private PropPairList propPairList = null;

    public PropPairFindThread(CounterPart cp,
                              Map<String, Instance> graph1,
                              Map<String, Instance> graph2,
                              PropPairList propPairList) {

        this.cp = cp;
        this.graph1 = graph1;
        this.graph2 = graph2;
        this.propPairList = propPairList;
    }

    public void run() {

        String sub1 = cp.getSub1();
        String sub2 = cp.getSub2();

        Instance inst1 = graph1.get(sub1);
        Instance inst2 = graph2.get(sub2);

        if (inst1 == null || inst2 == null) {
            logger.info("inst1 or inst2 is null: ");
            logger.info("sub1: " + sub1);
            logger.info("sub2: " + sub2);
            return;
        }

        Map<String, Set<Value>> propValue1 = inst1.getPropValue();
        Map<String, Set<Value>> propValue2 = inst2.getPropValue();

        if (propValue1 == null || propValue2 == null) {
            logger.info("propValue1 or propValue2 is null: ");
            logger.info("sub1: " + sub1);
            logger.info("sub2: " + sub2);
            return;
        }

        Iterator iter1 = propValue1.entrySet().iterator();
        while (iter1.hasNext()) {
            Map.Entry entry1 = (Map.Entry) iter1.next();
            String prop1 = (String) entry1.getKey();

            Set<Value> val1 = (Set<Value>) entry1.getValue();

            Iterator iter2 = propValue2.entrySet().iterator();

            while (iter2.hasNext()) {

                Map.Entry entry2 = (Map.Entry) iter2.next();
                String prop2 = (String) entry2.getKey();
                Set<Value> val2 = (Set<Value>) entry2.getValue();

                double value = calValSetSim(val1, val2);
                if (value > PROP_PAIR_THRESHOLD) {
//                    logger.info(String.valueOf(value));
                    propPairList.add(new PropPair(prop1, prop2));
                }
            }
        }
    }


}
