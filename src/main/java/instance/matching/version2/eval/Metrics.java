package instance.matching.version2.eval;

import instance.matching.version2.unit.Alignment;
import instance.matching.version2.unit.CounterPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by ciferlv on 17-3-30.
 */
public class Metrics {

    private static Logger logger = LoggerFactory.getLogger(Metrics.class);

    public static void calMetrics(Alignment refAlign, Alignment resultAlign) {

        double precision, recall, f1_score;

        Alignment correctAlign = new Alignment();
        Alignment wrongAlign = new Alignment();
        Alignment unfoundAlign = new Alignment();

        int correctNum = 0, wrongNum = 0;
        int refSize, resultSize;
        List<CounterPart> refCPL = refAlign.getCounterPartList();
        List<CounterPart> resultCPL = resultAlign.getCounterPartList();

        refSize = refAlign.size();
        resultSize = resultAlign.size();

        for (CounterPart cp : resultCPL) {
            if (refCPL.contains(cp)) {

                correctNum++;
                correctAlign.addCounterPart(cp);
            } else {
                wrongNum++;
                wrongAlign.addCounterPart(cp);
            }
        }

        for (CounterPart cp : refCPL) {

            if (!resultCPL.contains(cp)) {

                unfoundAlign.addCounterPart(cp);
            }
        }

        precision = 1.0 * correctNum / resultSize;
        recall = 1.0 * correctNum / refSize;
        f1_score = (2.0 * precision * recall) / (precision + recall);

        logger.info("precision: " + precision);
        logger.info("recall: " + recall);
        logger.info("f1_score: " + f1_score);

//        logger.info("UNFOUND:");
//        logger.info(unfoundAlign.toString());

    }


}
