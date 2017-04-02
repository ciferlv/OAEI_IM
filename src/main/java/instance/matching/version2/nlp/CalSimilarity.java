package instance.matching.version2.nlp;

import java.util.Set;

import static instance.matching.version2.nlp.EditDistance.editDistance;
import static instance.matching.version2.nlp.I_SUB.I_SUBScore;
import static instance.matching.version2.nlp.Jaccard.jaccardSimi;

/**
 * Created by xinzelv on 17-3-28.
 */
public class CalSimilarity {

    public static double calObjSetSim(Set<String> obj1Set, Set<String> obj2Set) {

        double max = -1.0;

        for (String obj1 : obj1Set) {
            for (String obj2 : obj2Set) {

                double tempResult;
                if (obj1.length() < 2 || obj2.length() < 2) {

                    tempResult = editDistance(obj1, obj2);
                } else {
                    tempResult = I_SUBScore(obj1,obj2);
                }

                max = Math.max(max, tempResult);
            }
        }
        return max;
    }



}
