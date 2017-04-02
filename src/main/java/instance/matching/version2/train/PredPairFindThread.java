package instance.matching.version2.train;

import instance.matching.version2.unit.CounterPart;
import instance.matching.version2.unit.PredPair;
import instance.matching.version2.unit.PredPairList;
import instance.matching.version2.unit.Triples;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import static instance.matching.version2.nlp.CalSimilarity.calObjSetSim;
import static instance.matching.version2.utility.VariableDef.predPairThreshold;

/**
 * Created by ciferlv on 17-3-29.
 */
public class PredPairFindThread implements Runnable {

    Logger logger = LoggerFactory.getLogger(PredPairFindThread.class);

    private CounterPart cp = null;
    private Map<String, Triples> graph1 = null;
    private Map<String, Triples> graph2 = null;
    private PredPairList predPairList = null;

    public PredPairFindThread(CounterPart cp,
                              Map<String, Triples> graph1,
                              Map<String, Triples> graph2,
                              PredPairList predPairList) {

        this.cp = cp;
        this.graph1 = graph1;
        this.graph2 = graph2;
        this.predPairList = predPairList;
    }

    public void run() {

        String sub1 = cp.getSubject1();
        String sub2 = cp.getSubject2();

        Triples tri1 = graph1.get(sub1);
        Triples tri2 = graph2.get(sub2);

        if (tri1 == null || tri2 == null) {
            logger.info("tri1 or tri2 is null: ");
            logger.info("sub1: " + sub1);
            logger.info("sub2: " + sub2);
            return;
        }

        Map<String, Set<String>> preObj1 = tri1.getPredicateObject();
        Map<String, Set<String>> preObj2 = tri2.getPredicateObject();

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
                if (value > predPairThreshold) {
//                    logger.info(String.valueOf(value));
                    predPairList.add(new PredPair(pre1, pre2));
                }
            }
        }
    }


}
