package instance.matching.version2.train;

import instance.matching.version2.unit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static instance.matching.version2.utility.ThreadEndJudge.terminateThread;

/**
 * Created by xinzelv on 17-3-28.
 */
public class PredPairFinder {

    private static Logger logger = LoggerFactory.getLogger(PredPairFinder.class);

    public static void findPredPair(Alignment sample, Document doc1, Document doc2,PredPairList ppl) {

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
        ppl.sort();
        ppl.resize(3);
    }

    //    public void findPredPair() {
//
//        List<CounterPart> counterPartList = sample.getCounterPartList();
//
//        for (CounterPart cp : counterPartList) {
//
//            String sub1 = cp.getSubject1();
//            String sub2 = cp.getSubject2();
//
//            Triples tri1 = graph1.get(sub1);
//            Triples tri2 = graph2.get(sub2);
//
//            if (tri1 == null || tri2 == null) {
//                logger.info("tri1 or tri2 is null: ");
//                logger.info("sub1: " + sub1);
//                logger.info("sub2: " + sub2);
//                continue;
//            }
//
//            Map<String, Set<String>> preObj1 = tri1.getPredicateObject();
//            Map<String, Set<String>> preObj2 = tri2.getPredicateObject();
//
//            if (preObj1 == null || preObj2 == null) {
//                logger.info("preObj1 or preObj2 is null: ");
//                logger.info("sub1: " + sub1);
//                logger.info("sub2: " + sub2);
//                continue;
//            }
//
//            Iterator iter1 = preObj1.entrySet().iterator();
//            while (iter1.hasNext()) {
//                Map.Entry entry1 = (Map.Entry) iter1.next();
//                String pre1 = (String) entry1.getKey();
//                Set<String> obj1 = (Set<String>) entry1.getValue();
//
//                Iterator iter2 = preObj2.entrySet().iterator();
//
//                while (iter2.hasNext()) {
//                    Map.Entry entry2 = (Map.Entry) iter2.next();
//                    String pre2 = (String) entry2.getKey();
//                    Set<String> obj2 = (Set<String>) entry2.getValue();
//
//                    if (calObjSetSim(obj1, obj2) > 0.7) {
//                        predPairList.add(new PredPair(pre1, pre2));
//                    }
//                }
//            }
//        }
//        predPairList.sort();
//    }

//    public void findPredPair1() {
//
//        List<CounterPart> counterPartList = sample.getCounterPartList();
//
//        int numOfThreads = 4;
//        Thread[] threads = new Thread[numOfThreads];
//
//        for (CounterPart cp : counterPartList) {
//
//            int indexOfDead = -1;
//            for (int i = 0; i < numOfThreads; i = (++i) % numOfThreads) {
//                if (threads[i] == null || !threads[i].isAlive()) {
//                    indexOfDead = i;
//                    break;
//                }
//            }
//
//            threads[indexOfDead] = new Thread(
//                    new PredPairFindThread(cp, graph1, graph2, predPairList));
//            threads[indexOfDead].start();
//        }
//
//        while (true) {
//
//            boolean allFinished = true;
//            for (int i = 0; i < numOfThreads; i++) {
//
//                if (threads[i].isAlive()) {
//                    allFinished = false;
//                    break;
//                }
//
//            }
//            if( allFinished ) break;
//            else try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//        predPairList.sort();
//    }
}
