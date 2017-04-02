package instance.matching.version2.train;

import instance.matching.version2.unit.*;

import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Created by xinzelv on 17-4-2.
 */
public class NegetiveFinder {

    public static void findNegetives(Alignment positive, Alignment refAlign, Document doc6, Alignment negetive) {

        List<CounterPart> counterPartList = positive.getCounterPartList();

        List<String> subList = doc6.getTargetSubject();

        for (CounterPart cp : counterPartList) {

            String sub1 = cp.getSubject1();

            Random r = new Random();

            boolean flag = false;
            do {
                int index = r.nextInt(subList.size());
                String tarSub = subList.get(index);
                if (!refAlign.contains(new CounterPart(sub1, tarSub))) {
                    flag = true;
                    negetive.addCounterPart(new CounterPart(sub1, tarSub));
                }
            } while (!flag);

        }

    }
}
