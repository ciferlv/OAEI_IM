package instance.matching.version2.nlp;

import instance.matching.version2.unit.Value;

import java.util.Set;

import static instance.matching.version2.nlp.EditDistance.editDistance;
import static instance.matching.version2.nlp.I_SUB.I_SUBScore;

/**
 * Created by xinzelv on 17-3-28.
 */
public class CalSimilarity {

    public static double calValSetSim(Set<Value> obj1Set, Set<Value> obj2Set) {

        double max = -1.0;

        for (Value obj1 : obj1Set) {
            for (Value obj2 : obj2Set) {

                double myRes;

                String value1 = obj1.getValue();
                int type1 = obj1.getType();
                String value2 = obj2.getValue();
                int type2 = obj2.getType();

                if (type1 != type2) {
                    myRes = 0;
                    max = Math.max(max, myRes);
                    continue;
                } else {

                }

                if (value1.length() < 2 || value2.length() < 2) {

                    myRes = editDistance(value1, value2);
                } else {
                    myRes = I_SUBScore(value1, value2);
                }

                max = Math.max(max, myRes);
            }
        }
        return max;
    }


}
