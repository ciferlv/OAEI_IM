import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static instance.matching.version2.nlp.EditDistance.editDistance;
import static instance.matching.version2.nlp.I_SUB.I_SUBScore;

/**
 * Created by ciferlv on 17-4-1.
 */
public class testCalSim {

    private static Logger logger = LoggerFactory.getLogger(testCalSim.class);

    public static void main(String[] args) {


        String str1 = "ok";
        String str2 = "ofgjjjjjjjjjjjyiygisdgfhjkhd" ;

        logger.info("EditDistance: " + editDistance(str1,str2));
        logger.info("I_SUB: " + I_SUBScore(str1,str2));

    }
}
