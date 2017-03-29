package instance.matching.pr.nlp;

import java.util.Set;

import static instance.matching.pr.nlp.EditDistance.editDistance;

/**
 * Created by xinzelv on 17-3-28.
 */
public class CalSimilarity {

    public static double calculate(Set<String> obj1Set, Set<String> obj2Set) {

        double max = -1.0;

        for (String obj1 : obj1Set) {
            for (String obj2 : obj2Set) {

                double tempResult = editDistance(obj1, obj2);

                max = Math.max(max, tempResult);
            }
        }
        return max;
    }


}
