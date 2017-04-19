package instance.matching.version2.nlp;

import instance.matching.version2.unit.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

import static instance.matching.version2.nlp.EditDistance.editDistance;
import static instance.matching.version2.nlp.I_SUB.I_SUBScore;
import static instance.matching.version2.utility.ParamDef.*;

/**
 * Created by xinzelv on 17-3-28.
 */
public class CalSimilarity {

    private static Logger logger = LoggerFactory.getLogger(CalSimilarity.class);

    //指示函数
    private static double indiFunc(String str1, String str2) {

        if (str1.equals(str2)) {
            return 1;
        } else return 0;
    }

    private static double strFunc(String str1, String str2) {

//        logger.info("str1:"+str1);
//        logger.info("str2:"+str2);
        if (str1.length() < 2 || str2.length() < 2) {

            return editDistance(str1, str2);
        } else {
            return I_SUBScore(str1, str2);
        }
    }


    public static double calValSetSim(Set<Value> val1Set, Set<Value> val2Set) {

        double max = -1.0;

        for (Value val1 : val1Set) {
            for (Value val2 : val2Set) {

                double myRes;

                int val1Type = val1.getType();
                int val2Type = val2.getType();
                String value1 = val1.getValue();
                String value2 = val2.getValue();

                if (value1 == ""
                        || value1 == null
                        || value2 == ""
                        || value2 == null) {
                    continue;
                }

                if (val1Type == val2Type) {

                    if (val1Type == INTEGER_TYPE_INDEX
                            || val1Type == FLOAT_TYPE_INDEX
                            || val1Type == DATETIME_TYPE_INDEX
                            || val1Type == DATE_TYPE_INDEX
                            || val1Type == BOOLEAN_TYPE_INDEX) {

                        myRes = indiFunc(value1, value2);
                        max = Math.max(max, myRes);
                    } else if (val1Type == STRING_TYPE_INDEX) {

                        myRes = strFunc(value1, value2);
                        max = Math.max(max, myRes);
                    } else if (val1Type == URI_TYPE_INDEX) {
                        String val1LocalName = val1.getLocalName();
                        String val2LocalName = val1.getLocalName();

                        if (val1LocalName == ""
                                || val1LocalName == null
                                || val2LocalName == null
                                || val2LocalName == "") {
                            continue;
                        }

                        myRes = strFunc(val1.getLocalName(), val2.getLocalName());
                        max = Math.max(max, myRes);
                    }
                } else if (val1Type == THING_TYPE_INDEX || val2Type == THING_TYPE_INDEX) {

                    if (val1Type == URI_TYPE_INDEX) value1 = val1.getLocalName();
                    if (val2Type == URI_TYPE_INDEX) value2 = val2.getLocalName();

                    if (value1 == ""
                            || value1 == null
                            || value2 == ""
                            || value2 == null) {
                        continue;
                    }

                    myRes = strFunc(value1, value2);
                    max = Math.max(max, myRes);
                } else {
                    myRes = 0;
                    max = Math.max(max, myRes);
                }
            }
        }
        return max;
    }


}
