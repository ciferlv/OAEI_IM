package instance.matching.version2.train;

import instance.matching.version2.unit.Alignment;
import instance.matching.version2.unit.CounterPart;
import instance.matching.version2.unit.Document;

import java.util.List;
import java.util.Random;

/**
 * Created by xinzelv on 17-4-2.
 */
public class NegetiveFinder {

    public static void findNegetives(Alignment positives,
                                     Alignment refAlign,
                                     Document doc2,
                                     Alignment negetives) {

        List<CounterPart> counterPartList = positives.getCounterPartList();

        List<String> subList = doc2.getTargetSubject();

        Random r = new Random();

        for (CounterPart cp : counterPartList) {

            String sub1 = cp.getSubject1();

            boolean flag = false;
            do {
                int index = r.nextInt(subList.size());
                String tarSub = subList.get(index);
                if (!refAlign.contains(new CounterPart(sub1, tarSub))) {
                    flag = true;
                    negetives.addCounterPart(new CounterPart(sub1, tarSub));
                }
            } while (!flag);

        }

    }
}
