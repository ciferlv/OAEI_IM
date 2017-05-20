import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static instance.matching.nlp.EditDistance.editDistance;
import static instance.matching.nlp.I_SUB.I_SUBScore;

/**
 * Created by ciferlv on 17-4-1.
 */
public class calSimTest {

    private static Logger logger = LoggerFactory.getLogger(calSimTest.class);

    public static void main(String[] args) {


        String str1 = "20110214t1520163610200";
        String str2 = "20110214t1520163610200" ;

        logger.info("EditDistance: " + editDistance(str1,str2));
        logger.info("I_SUB: " + I_SUBScore(str1,str2));
    }
}
