package instance.matching.pr;

import instance.matching.pr.unit.*;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static instance.matching.pr.nlp.calSimilarity.calculate;

/**
 * Created by xinzelv on 17-3-28.
 */
public class PredPairFinder {

    private Alignment sample = null;
    private Map<String, Triples> graph1 = null;
    private Map<String, Triples> graph2 = null;
    private PredPairList predPairList = null;

    public PredPairFinder(Alignment sample,
                          Map<String, Triples> graph1,
                          Map<String, Triples> graph2 ) {

        this.sample = sample;
        this.graph1 = graph1;
        this.graph2 = graph2;

        predPairList = new PredPairList();
    }

    public void findPredPair() {

        List<CounterPart> counterPartList = sample.getCounterPartList();

        for (CounterPart cp : counterPartList) {

            String sub1 = cp.getSubject1();
            String sub2 = cp.getSubject2();

            Triples tri1 = graph1.get(sub1);
            Triples tri2 = graph2.get(sub2);

            Map<String, Set<String>> preObj1 = tri1.getPredicateObject();
            Map<String, Set<String>> preObj2 = tri2.getPredicateObject();

            Iterator iter1 = preObj1.entrySet().iterator();
            while (iter1.hasNext()) {
                Map.Entry entry1 = (Map.Entry) iter1.next();
                String pre1 = (String) entry1.getKey();
                Set<String> obj1 = (Set<String>) entry1.getValue();

                Iterator iter2 = preObj2.entrySet().iterator();

                while (iter2.hasNext()) {
                    Map.Entry entry2 = (Map.Entry) iter1.next();
                    String pre2 = (String) entry2.getKey();
                    Set<String> obj2 = (Set<String>) entry2.getValue();

                    if (calculate(obj1, obj2) > 0.7) {
                        predPairList.add( new PredPair(pre1, pre2));
                    }
                }
            }
        }
    }

    public PredPairList getPredPairList() {
        return predPairList;
    }
}
