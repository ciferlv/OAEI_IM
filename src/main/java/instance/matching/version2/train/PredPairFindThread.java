package instance.matching.version2.train;

import instance.matching.version2.unit.CounterPart;
import instance.matching.version2.unit.PropPair;
import instance.matching.version2.unit.PropPairList;
import instance.matching.version2.unit.Instance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import static instance.matching.version2.nlp.CalSimilarity.calObjSetSim;
import static instance.matching.version2.utility.VariableDef.PROP_PAIR_THRESHOLD;

/**
 * Created by ciferlv on 17-3-29.
 */
public class PredPairFindThread implements Runnable {

    Logger logger = LoggerFactory.getLogger(PredPairFindThread.class);

    private CounterPart cp = null;
    private Map<String, Instance> graph1 = null;
    private Map<String, Instance> graph2 = null;
    private PropPairList propPairList = null;

    public PredPairFindThread(CounterPart cp,
                              Map<String, Instance> graph1,
                              Map<String, Instance> graph2,
                              PropPairList propPairList) {

        this.cp = cp;
        this.graph1 = graph1;
        this.graph2 = graph2;
        this.propPairList = propPairList;
    }

    public void run() {

        String sub1 = cp.getSubject1();
        String sub2 = cp.getSubject2();

        Instance tri1 = graph1.get(sub1);
        Instance tri2 = graph2.get(sub2);

        if (tri1 == null || tri2 == null) {
            logger.info("tri1 or tri2 is null: ");
            logger.info("sub1: " + sub1);
            logger.info("sub2: " + sub2);
            return;
        }

        Map<String, Set<String>> preObj1 = tri1.getPropValue();
        Map<String, Set<String>> preObj2 = tri2.getPropValue();

        if (preObj1 == null || preObj2 == null) {
            logger.info("preObj1 or preObj2 is null: ");
            logger.info("sub1: " + sub1);
            logger.info("sub2: " + sub2);
            return;
        }

        Iterator iter1 = preObj1.entrySet().iterator();
        while (iter1.hasNext()) {
            Map.Entry entry1 = (Map.Entry) iter1.next();
            String pre1 = (String) entry1.getKey();
            Set<String> obj1 = (Set<String>) entry1.getValue();

            Iterator iter2 = preObj2.entrySet().iterator();

            while (iter2.hasNext()) {
                Map.Entry entry2 = (Map.Entry) iter2.next();
                String pre2 = (String) entry2.getKey();
                Set<String> obj2 = (Set<String>) entry2.getValue();
                double value = calObjSetSim(obj1, obj2);
                if (value > PROP_PAIR_THRESHOLD) {
//                    logger.info(String.valueOf(value));
                    propPairList.add(new PropPair(pre1, pre2));
                }
            }
        }
    }


}
