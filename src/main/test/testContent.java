import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static instance.matching.version2.nlp.EditDistance.editDistance;
import static instance.matching.version2.nlp.I_SUB.I_SUBScore;

/**
 * Created by Administrator on 2017/4/12.
 */
public class testContent {

    private static Logger logger = LoggerFactory.getLogger(testContent.class);
    public static void main(String[] args) {

        testCalSim();
    }

    public static void testCalSim() {
//
//        String str1 = "L.A.";
//        String str2 = "At the L.A.";

        String str1 = "store";
        String str2 = "score";

        double value1 = I_SUBScore(str1, str2);
        double value2 = editDistance(str1, str2);

        logger.info(String.valueOf(value1));
        logger.info(String.valueOf(value2));
    }

}
